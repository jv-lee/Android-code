package com.lee.component.compiler;

import com.google.auto.service.AutoService;
import com.lee.component.annotation.Parameter;
import com.lee.component.compiler.factory.ParameterFactory;
import com.lee.component.compiler.utils.Constants;
import com.lee.component.compiler.utils.EmptyUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

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
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author jv.lee
 * @date 2019/7/15.
 * @description
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({Constants.PARAMETER_ANNOTATION_TYPES})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ParameterProcessor extends AbstractProcessor {

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
     * 临时map存储，用来存放被@Parameter注解的属性集合，生成类文件时遍历
     * key:类节点，value:被@Parameter注解的属性集合
     */
    private Map<TypeElement, List<Element>> tempParameterMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //@Parameter注解存在
        if (!EmptyUtils.isEmpty(set)) {
            //获取所有被@Parameter注解 元素集合
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Parameter.class);
            if (!EmptyUtils.isEmpty(elements)) {
                //临时map存储，用来遍历生成代码
                valueOfParameterMap(elements);
                //生成类文件
                try {
                    createParameterFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    private void createParameterFile() throws IOException {
        if (EmptyUtils.isEmpty(tempParameterMap)) {
            return;
        }
        //通过Element工具类，获取Parameter类型
        TypeElement parameterType = elementUtils.getTypeElement(Constants.PARAMETER_LOAD);

        //参数体配置（Object target)
        ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.OBJECT, Constants.PARAMETER_NAME).build();
        for (Map.Entry<TypeElement, List<Element>> entry : tempParameterMap.entrySet()) {
            //Map集合中的Key是类名，如：MainActivity
            TypeElement typeElement = entry.getKey();

            //获取类名
            ClassName className = ClassName.get(typeElement);
            //方法体内容构建
            ParameterFactory factory = new ParameterFactory.Builder(parameterSpec)
                    .setMessager(messager)
                    .setElementUtils(elementUtils)
                    .setTypeUtils(typeUtils)
                    .setClassName(className)
                    .build();

            //添加方法体内容的的第一行 : {MainActivity t = (MainActivity) target; }
            factory.addFirstStatement();

            //遍历类里所有的属性 : { t.name = t.getIntent().getStringExtra("name"); }
            for (Element fieldElement : entry.getValue()) {
                factory.buildStatement(fieldElement);
            }

            //最终生成的类文件名（类名$$Parameter)
            String finalClassName = typeElement.getSimpleName() + Constants.PARAMETER_FILE_NAME;
            messager.printMessage(Diagnostic.Kind.NOTE, "APT生产获取参数类文件：" + className.packageName() + "." + finalClassName);

            //MainActivity$$Parameter
            JavaFile.builder(className.packageName(),
                    TypeSpec.classBuilder(finalClassName)
                            .addSuperinterface(ClassName.get(parameterType))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(factory.build())
                            .build())
                    .build()
                    .writeTo(filer);

        }
    }

    private void valueOfParameterMap(Set<? extends Element> elements) {
        for (Element element : elements) {
            //注解的属性，父节点是类节点
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            //如果map集合中有这个类节点
            if (tempParameterMap.containsKey(typeElement)) {
                tempParameterMap.get(typeElement).add(element);
            } else {
                List<Element> fields = new ArrayList<>();
                fields.add(element);
                tempParameterMap.put(typeElement, fields);
            }
        }
    }

}
