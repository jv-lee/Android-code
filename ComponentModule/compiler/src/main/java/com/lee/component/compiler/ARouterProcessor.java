package com.lee.component.compiler;

import com.google.auto.service.AutoService;
import com.lee.component.annotation.ARouter;
import com.lee.component.annotation.model.RouterBean;
import com.lee.component.compiler.utils.Constants;
import com.lee.component.compiler.utils.EmptyUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author jv.lee
 * @date 2019/7/11.
 * @description ARouter注解处理器
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({Constants.AROUTER_ANNOTATION_TYPES})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions({Constants.MODULE_NAME, Constants.APT_PACKAGE})
public class ARouterProcessor extends AbstractProcessor {

    /**
     * 操作Element工具类
     */
    private Elements elementUtils;

    /**
     * type(类信息)工具类
     */
    private Types typeUtils;

    /**
     * 用来输出警告、错误等日志
     */
    private Messager messager;

    /**
     * 文件生成器
     */
    private Filer filer;

    /**
     * 子模块名，类/资源，Filter用来创建新类文件，class文件以及辅助文件
     */
    private String moduleName;

    /**
     * 包名，用于存放APT生成的类文件
     */
    private String packageNameForAPT;

    /**
     * 临时map存储，用来存放路由组Group对应的详细Path类对象，生成路由路径类文件时遍历
     * key：组名"app" value："app"组的路由路径"ARouter$$Path$$app.class
     */
    private Map<String, List<RouterBean>> tempPathMap = new HashMap<>();

    /**
     * 临时map存储，用来存放路由组Group信息，生成路由组类文件时遍历
     * key：组名"app" value：类名"ARouter$$Path$$app.class"
     */
    private Map<String, String> tempGroupMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();

        //通过ProcessingEnvironment去获取对应的参数
        Map<String, String> options = processingEnvironment.getOptions();
        if (!EmptyUtils.isEmpty(options)) {
            moduleName = options.get(Constants.MODULE_NAME);
            packageNameForAPT = options.get(Constants.APT_PACKAGE);
            messager.printMessage(Diagnostic.Kind.NOTE, "moduleName>>>" + moduleName);
            messager.printMessage(Diagnostic.Kind.NOTE, "packageName>>>" + packageNameForAPT);
        }

        //必传参数盘空（乱码问题：添加java控制台输出中文乱码）
        if (EmptyUtils.isEmpty(moduleName) && EmptyUtils.isEmpty(packageNameForAPT)) {
            throw new RuntimeException("注解处理器需要的参数module&packageName为空，请在对应build.gradle配置参数");
        }

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //一旦有类上使用了@ARouter注解
        if (!EmptyUtils.isEmpty(set)) {
            //获取所有被@ARouter注解 的元素集合
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ARouter.class);

            if (!EmptyUtils.isEmpty(elements)) {
                //解析元素
                try {
                    parseElements(elements);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return true;
        }
        return false;
    }

    /**
     * 解析所有被@ARouter注解的元素集合
     *
     * @param elements 元素
     */
    private void parseElements(Set<? extends Element> elements) throws IOException {

        //通过Element工具类，获取Activity类型
        TypeElement activityType = elementUtils.getTypeElement(Constants.ACTIVITY);
        TypeElement callType = elementUtils.getTypeElement(Constants.CALL);
        //显示类的信息
        TypeMirror activityMirror = activityType.asType();

        for (Element element : elements) {
            //获取每个元素的类信息
            TypeMirror elementMirror = element.asType();
            messager.printMessage(Diagnostic.Kind.NOTE, "遍历的元素信息为：" + elementMirror.toString());

            //获取每个类上@ARouter注解，对应的path值
            ARouter aRouter = element.getAnnotation(ARouter.class);

            //路由详细信息，封装到实体类
            RouterBean bean = new RouterBean.Builder()
                    .setGroup(aRouter.group())
                    .setPath(aRouter.path())
                    .setElement(element)
                    .build();

            //高级判断 @ARouter注解只能作用在类之上，并且是规定的Activity
            if (typeUtils.isSubtype(elementMirror, activityMirror)) {
                bean.setType(RouterBean.Type.ACTIVITY);
            } else {
                throw new RuntimeException("@ARouter注解目前仅限于Activity之上");
            }

            //赋值临时map存储以上信息，用来遍历时生成代码
            valueOfPathMap(bean);
        }
        // ARouterLoadGroup和ARouterLoadPath类型，用来生成类文件时实现接口
        TypeElement groupLoadType = elementUtils.getTypeElement(Constants.AROUTER_GROUP);
        TypeElement pathLoadType = elementUtils.getTypeElement(Constants.AROUTER_PATH);

        //1、生成路由的详细Path类文件，如：ARouter$$Path$$app
        createPathFile(pathLoadType);

        //2、生成路由组Group类文件（没有Path类文件，取不到），如：ARouter$$Group$$app
        createGroupFile(groupLoadType, pathLoadType);
    }

    /**
     * 生成路由组成Group对应详细Path，如：ARouter$$Path$$app
     *
     * @param pathLoadType ARouterLoadPath接口信息
     */
    private void createPathFile(TypeElement pathLoadType) throws IOException {
        if (EmptyUtils.isEmpty(tempPathMap)) {
            return;
        }
        // 方法的返回值Map<String,RouterBean>
        TypeName methodReturns = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterBean.class));

        //遍历分组，每一分组创建一个路径类文件，如：ARouter$$Path$$app
        for (Map.Entry<String, List<RouterBean>> entry : tempPathMap.entrySet()) {
            //方法提构造public Map<String,RouterBean> loadPath()
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.PATH_METHOD_NAME)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(methodReturns);

            //不循环部分Map<String,RouterBean> pathMap = new HashMap<>();
            methodBuilder.addStatement("$T<$T,$T> $N = new $T<>()",
                    ClassName.get(Map.class),
                    ClassName.get(String.class),
                    ClassName.get(RouterBean.class),
                    Constants.PATH_PARAMETER_NAME,
                    ClassName.get(HashMap.class));

            //app/MainActivity,app/...
            List<RouterBean> pathList = entry.getValue();
            //方法内容的循环部分 { pathMap.put(".app/MainActivity",RouterBean.create(RouterBean.Type.ACTIVITY,MainActivity.class,"/app/MainActivity","app")); }
            for (RouterBean bean : pathList) {
                methodBuilder.addStatement("$N.put($S,$T.create($T.$L,$T.class,$S,$S))",
                        Constants.PATH_PARAMETER_NAME,
                        bean.getPath(),
                        ClassName.get(RouterBean.class),
                        ClassName.get(RouterBean.Type.class),
                        bean.getType(),
                        ClassName.get((TypeElement) bean.getElement()),
                        bean.getPath(),
                        bean.getGroup()
                );
            }

            //遍历过后，最后return pathMap
            methodBuilder.addStatement("return $N", Constants.PATH_PARAMETER_NAME);

            //生成类文件,如：ARouter$$Path$$app
            String finalClassName = Constants.PATH_FILE_NAME + entry.getKey();
            messager.printMessage(Diagnostic.Kind.NOTE, "APT生成路由PATH类文件为：" + packageNameForAPT + "." + finalClassName);

            JavaFile.builder(packageNameForAPT,
                    TypeSpec.classBuilder(finalClassName)
                            .addSuperinterface(ClassName.get(pathLoadType))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(methodBuilder.build())
                            .build())
                    .build()
                    .writeTo(filer);

            //临时租中根据path路径存放group组数据 方便生成组文件
            tempGroupMap.put(entry.getKey(), finalClassName);
        }


    }

    /**
     * 生成路由组Group文件，如：ARouter$$Group$$app
     *
     * @param groupLoadType ARouterLoadGroup接口信息
     * @param pathLoadType  ARouterLoadPath接口信息
     */
    private void createGroupFile(TypeElement groupLoadType, TypeElement pathLoadType) throws IOException {
        //判断是否有需要生成的类文件
        if (EmptyUtils.isEmpty(tempGroupMap) || EmptyUtils.isEmpty(tempPathMap)) {
            return;
        }

        // Map/Map<String,第二个参数：Class<? extends ARouterLoadPath>> , 某Class是否属于ARouterLoadPath接口的实现类
        TypeName methodReturns = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathLoadType)))
        );

        //方法配置：{ public Map<String,Class<? extends ARouterLoadPath>> loadGroup() }
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.GROUP_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(methodReturns);

        //遍历之前：{ Map<String,Class<? extends ARouterLoadPath>> groupMap = new HashMap<>(); }
        methodBuilder.addStatement("$T<$T,$T> $N = new $T<>()",
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathLoadType))),
                Constants.GROUP_PARAMETER_NAME,
                HashMap.class);

        //方法内容配置
        for (Map.Entry<String, String> entry : tempGroupMap.entrySet()) {
            //类似String.format("hello %s code %d","code",123)通配符
            // { groupMap.put("main",ARouter$$Path$$app.class); }
            methodBuilder.addStatement("$N.put($S,$T.class)",
                    Constants.GROUP_PARAMETER_NAME,
                    entry.getKey(),
                    ClassName.get(packageNameForAPT, entry.getValue()));
        }

        //遍历之后：return groupMap;
        methodBuilder.addStatement("return $N", Constants.GROUP_PARAMETER_NAME);

        //最终生成的类文件名
        String finalClassName = Constants.GROUP_FILE_NAME + moduleName;
        messager.printMessage(Diagnostic.Kind.NOTE, packageNameForAPT + "." + finalClassName);

        //生成类文件：ARouter$$Group$$app
        JavaFile.builder(packageNameForAPT,
                TypeSpec.classBuilder(finalClassName)
                        .addSuperinterface(ClassName.get(groupLoadType))
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(methodBuilder.build())
                        .build())
                .build()
                .writeTo(filer);

    }

    /**
     * 赋值临时map存储，用来存放路由组Group的详细Path类对象，生成路由路径类文件时遍历
     *
     * @param bean 路由详细信息，最终实体封装类
     */
    private void valueOfPathMap(RouterBean bean) {
        if (checkRouterPath(bean)) {
            messager.printMessage(Diagnostic.Kind.NOTE, "RouterBean >>>" + bean.toString());

            //开始赋值
            List<RouterBean> routerBeans = tempPathMap.get(bean.getGroup());
            //如果从Map中找不到key
            if (EmptyUtils.isEmpty(routerBeans)) {
                routerBeans = new ArrayList<>();
                routerBeans.add(bean);
                tempPathMap.put(bean.getGroup(), routerBeans);
            } else {
                routerBeans.add(bean);
            }
        } else {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范，如：app/MainActivity");
        }
    }

    /**
     * 校验@ARouter注解的值，如果group未天蝎就从必填项path中截取数据
     *
     * @param bean 路由详细信息，最终实体封装类
     * @return 校验结果
     */
    private boolean checkRouterPath(RouterBean bean) {
        String group = bean.getGroup();
        String path = bean.getPath();
        //@ARouter注解的path值，必须以/开头（模仿阿里ARouter路由框架
        if (EmptyUtils.isEmpty(path) || !path.startsWith("/")) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范，如：/app/MainActivity");
            return false;
        }

        //比如开发者代码未：path = "/MainActivity"
        if (path.lastIndexOf("/") == 0) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范，如：/app/MainActivity");
            return false;
        }

        //从第一个 / 到第二个 / 中间截取出组名
        String finalGroup = path.substring(1, path.indexOf("/", 1));
        //比如开发者代码为：path = "/MainActivity/MainActivity/MainActivity"
        if (finalGroup.contains("/")) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范，如：/app/MainActivity");
            return false;
        }

        //@ARouter注解中有group赋值
        if (!EmptyUtils.isEmpty(group) && !group.equals(moduleName)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解中的group值必须和当前子模块名相同");
            return false;
        } else {
            bean.setGroup(finalGroup);
        }

        return true;
    }

}
