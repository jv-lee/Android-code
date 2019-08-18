package lee.eventbus.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import lee.eventbus.annotation.Subscribe;
import lee.eventbus.annotation.mode.EventBeans;
import lee.eventbus.annotation.mode.SubscriberInfo;
import lee.eventbus.annotation.mode.SubscriberMethod;
import lee.eventbus.annotation.mode.ThreadMode;
import lee.eventbus.compiler.utils.Constants;
import lee.eventbus.compiler.utils.EmptyUtils;

/**
 * @author jv.lee
 * @date 2019-08-17
 * @description 订阅事件注解处理器
 * @AutoService 设置注解处理器自动工作 必须，否则注解处理器无法工作
 * @SupportedAnnotationTypes 允许支持注解类型，让注解处理器处理该包名下的注解
 * @SupportedSourceVersion 指定JDK编译版本
 * @SupportedOptions 注解处理器接收的参数
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({Constants.SUBSCRIBE_ANNOTATION_TYPES})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions({Constants.PACKAGE_NAME, Constants.CLASS_NAME})
public class SubscribeProcessor extends AbstractProcessor {

    /**
     * 操作Element 工具类（类、函数、属性都是Element）
     */
    private Elements elementUtils;

    /**
     * type（类信息）工具类，包含用于操作TypeMirror的工具方法
     */
    private Types typeUtils;

    /**
     * Messager 用来报告错误，警告和其他提示信息
     */
    private Messager messager;

    /**
     * 文件生成器 类/资源：Filter用来创建新的类文件，class文件以及辅助文件
     */
    private Filer filer;

    /**
     * APT包名
     */
    private String packageName;

    /**
     * APT类名
     */
    private String className;

    /**
     * 临时map存储，用来存放订阅方法信息，生成路由组类文件时遍历
     * key：组名"MainActivity",value:MainActivity中订阅方法集合
     */
    private final Map<TypeElement, List<ExecutableElement>> methodsByClass = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        messager.printMessage(Diagnostic.Kind.NOTE, "init >>>>>>>>>>>>");
        //通过ProcessingEnvironment 去获取对应的参数
        Map<String, String> options = processingEnvironment.getOptions();
        if (!EmptyUtils.isEmpty(options)) {
            packageName = options.get(Constants.PACKAGE_NAME);
            className = options.get(Constants.CLASS_NAME);

            //输出打印获取的参数
            messager.printMessage(Diagnostic.Kind.NOTE,
                    Constants.PACKAGE_NAME + ">" + packageName
                            + "/ " + Constants.CLASS_NAME + ">" + className);
        }

        //必传参数判空 （乱码问题：添加java控制台输出中文乱码）
        if (EmptyUtils.isEmpty(packageName) || EmptyUtils.isEmpty(className)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "注解处理器需要的参数为空，请在对应build.gradle配置参数");
        }

    }

    /**
     * 注解处理器的核心方法，处理具体的注解，生成Java文件
     *
     * @param set              使用了支持处理注解的节点集合
     * @param roundEnvironment 当前或是之前运行环境，可以通过该对象查找的注解
     * @return true 表示后续处理器不会再处理（以及处理完毕）
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //一但有方法之上使用@Subscribe注解
        if (!EmptyUtils.isEmpty(set)) {
            //获取所有被 @Subscribe 注解的元素集合
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Subscribe.class);

            if (!EmptyUtils.isEmpty(elements)) {
                try {
                    //解析元素
                    parseElements(elements);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 解析所有被注解的类元素集合
     *
     * @param elements @Subscribe 注解的 类元素集合
     */
    private void parseElements(Set<? extends Element> elements) {
        //遍历节点
        for (Element element : elements) {
            //@Subscribe注解只能在方法之上，尽量避免使用instanceof 进行判断
            if (element.getKind() != ElementKind.METHOD) {
                messager.printMessage(Diagnostic.Kind.ERROR, "仅解析@Subscribe注解在方法上的元素");
                return;
            }

            //强转方法元素
            ExecutableElement method = (ExecutableElement) element;
            //检查方法，条件：订阅方法必须是非静态的，公开的，参数只能有一个
            if (checkHasNoError(method)) {
                //获取封装订阅方法的类（方法上一个节点）
                TypeElement classElement = (TypeElement) method.getEnclosingElement();

                //以类名为key，保存订阅方法
                List<ExecutableElement> methods = methodsByClass.get(classElement);
                if (methods == null) {
                    methods = new ArrayList<>();
                    methodsByClass.put(classElement, methods);
                }
                methods.add(method);
            }

            messager.printMessage(Diagnostic.Kind.NOTE, "遍历注解方法：" + method.getSimpleName().toString());
        }

        //通过Element工具类，获取SubscribeInfoIndex类型
        TypeElement subscriberInfoIndexType = elementUtils.getTypeElement(Constants.SUBSCRIBER_INFO_INDEX);
        //生成类文件
        try {
            crateFile(subscriberInfoIndexType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建类文件
     *
     * @param subscriberInfoIndexType 类文件创建
     */
    private void crateFile(TypeElement subscriberInfoIndexType) throws Exception {
        //添加静态块代码：SUBSCRIBER_INDEX = new HashMap<Class,SubscriberInfo>();
        CodeBlock.Builder codeBlock = CodeBlock.builder();
        codeBlock.addStatement("$N = new $T<$T,$T>()",
                Constants.FIELD_NAME,
                HashMap.class,
                Class.class,
                SubscriberInfo.class)
        .add("\n");

        //双层循环，第一层遍历被@Subcribe注解的方法所属类，第二层遍历每个类中所有订阅的方法
        for (Map.Entry<TypeElement, List<ExecutableElement>> entry : methodsByClass.entrySet()) {
            //此处不能使用codeBlock，会造成错误嵌套  contentBlock 为最内层对象数组的new 对象 代码块
            // new SubscriberMethod(MainActivity.class,"onEvent",UserInfo.class,ThreadMode.POSTING,0,false)}
            CodeBlock.Builder contentBlock = CodeBlock.builder();
            //此处为最外层代码块 putIndex(new EventBeans(MainActivity.class,new SubscriberMethod[]{
            CodeBlock contentCode;
            //转译拼接多个数组对象中的 new Object
            String format;

            for (int i = 0; i < entry.getValue().size(); i++) {
                ExecutableElement element = entry.getValue().get(i);
                //获取每个方法上的@Subscribe注解中的注解值
                Subscribe subscribe = element.getAnnotation(Subscribe.class);
                //获取订阅事件方法所有参数
                List<? extends VariableElement> parameters = element.getParameters();
                //获取订阅事件方法名
                String methodName = element.getSimpleName().toString();
                //此处继续做检查空座：参数类型必须是类或接口
                TypeElement parameterElement = (TypeElement) typeUtils.asElement(parameters.get(0).asType());
                //如果是最后一个添加，则无需逗号结尾
                if (i == entry.getValue().size() - 1) {
                    format = "\nnew $T($T.class,$S,$T.class,$T.$L,$L,$L)";
                } else {
                    format = "\nnew $T($T.class,$S,$T.class,$T.$L,$L,$L),";
                }

                //创建每一个类中所有的事件方法代码块
                contentBlock
                        .add(format,
                                ClassName.get(SubscriberMethod.class),
                                ClassName.get(entry.getKey()),
                                methodName,
                                ClassName.get(parameterElement),
                                ClassName.get(ThreadMode.class),
                                subscribe.threadMode(),
                                subscribe.priority(),
                                subscribe.sticky());
            }

            //根据每一个注册事件的类创建EventBean代码块
            contentCode = CodeBlock.builder()
                    .add(Constants.PUT_INDEX_METHOD_NAME + "(new $T($T.class,new $T[]{",
                            ClassName.get(EventBeans.class),
                            ClassName.get(entry.getKey()),
                            ClassName.get(SubscriberMethod.class))
                    .add(contentBlock.build())
                    .add("}));\n\n")
                    .build();

            codeBlock.add(contentCode);
        }

        //全局属性：Map<Class<?>,SubscriberMethod>
        TypeName fieldType = ParameterizedTypeName.get(
                //Map
                ClassName.get(Map.class),
                //Map<Class,
                ClassName.get(Class.class),
                //Map<Class,SubscriberInfo>
                ClassName.get(SubscriberInfo.class)
        );

        //putIndex方法参数：putIndex(SubscriberInfo info)
        ParameterSpec putIndexParameter = ParameterSpec.builder(
                ClassName.get(SubscriberInfo.class),
                Constants.PUT_INDEX_PARAMETER_NAME)
                .build();

        //putIndex 方法配置： private static void putIndex(SubscriberMethod info)
        MethodSpec.Builder putIndexBuilder = MethodSpec
                //添加放名
                .methodBuilder(Constants.PUT_INDEX_METHOD_NAME)
                //添加方法修饰符
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                //添加方法参数
                .addParameter(putIndexParameter);

        //putIndex方法内容：SUBSCRIBER_INDEX.put(info.getSubscriberClass(),int)
        putIndexBuilder.addStatement("$N.put($N.getSubscriberClass(),$N)",
                //占位符第一个 参数名
                Constants.FIELD_NAME,
                //占位符第二个 put键
                Constants.PUT_INDEX_PARAMETER_NAME,
                //占位符第三个 put值
                Constants.PUT_INDEX_PARAMETER_NAME);
        //不填写return 默认返回void

        //getSubscriberInfo 方法参数：Class subscriberClass
        ParameterSpec getSubscriberInfoParameter = ParameterSpec.builder(
                ClassName.get(Class.class),
                Constants.GET_SUBSCRIBER_INFO_PARAMETER_NAME)
                .build();

        //getSubscriberInfo 方法配置：public SubscriberInfo getSubscriberInfo(Class<?> subscriberClass)
        MethodSpec.Builder getSubscriberInfoBuilder = MethodSpec
                //方法名
                .methodBuilder(Constants.GET_SUBSCRIBER_INFO_METHOD_NAME)
                //重写方法注解
                .addAnnotation(Override.class)
                //类修饰符
                .addModifiers(Modifier.PUBLIC)
                //方法参数
                .addParameter(getSubscriberInfoParameter)
                //方法返回值
                .returns(SubscriberInfo.class);

        //getSubscriberInfo 方法内容：{return SUBSCRIBER_INDEX.get(subscriberClass);}
        getSubscriberInfoBuilder.addStatement("return $N.get($N)",
                Constants.FIELD_NAME,
                Constants.GET_SUBSCRIBER_INFO_PARAMETER_NAME);

        //构建类
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
                //实现SubscriberInfoIndex接口
                .addSuperinterface(ClassName.get(subscriberInfoIndexType))
                //该类的修饰符
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                //添加静态快
                .addStaticBlock(codeBlock.build())
                //全局属性：private static final Map<Class<?>,SubscriberMethod> SUBSCRIBER_INDEX
                .addField(fieldType, Constants.FIELD_NAME, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                //第一个方法：加入全局Map集合
                .addMethod(putIndexBuilder.build())
                //第二个方法：通过订阅者对象（MainActivity.class）获取所有订阅方法
                .addMethod(getSubscriberInfoBuilder.build())
                .build();

        JavaFile.builder(packageName,
                typeSpec)
                .build()
                .writeTo(filer);
    }

    /**
     * 检查方法，条件：订阅方法必须是非晶态的，公开的，参数只能有一个
     *
     * @param method 方法元素
     * @return 检查是否通过
     */
    private boolean checkHasNoError(ExecutableElement method) {
        //不能为静态方法
        if (method.getModifiers().contains(Modifier.STATIC)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "订阅事件方法不能是static静态方法", method);
            return false;
        }

        //必须是public修饰的方法
        if (!method.getModifiers().contains(Modifier.PUBLIC)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "订阅事件方法必须是public修饰的方法", method);
            return false;
        }

        //订阅事件方法必须只有一个参数
        List<? extends VariableElement> parameter = ((ExecutableElement) method).getParameters();
        if (parameter.size() != 1) {
            messager.printMessage(Diagnostic.Kind.ERROR, "订阅事件方法有且仅有一个参数", method);
            return false;
        }

        //校验成功
        return true;
    }


}
