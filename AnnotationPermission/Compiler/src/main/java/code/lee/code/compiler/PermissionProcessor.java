package code.lee.code.compiler;

import com.google.auto.service.AutoService;

import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

import code.lee.code.annotation.OnNeverAskAgain;
import code.lee.code.annotation.OnPermission;
import code.lee.code.annotation.OnPermissionDenied;
import code.lee.code.annotation.OnShowRationale;

/**
 * @author jv.lee
 * @date 2019/4/13
 */
@AutoService(Process.class)
public class PermissionProcessor extends AbstractProcessor {

    /**
     * 用来报告错误信息，警告，提示
     */
    private Messager messager;

    /**
     * 用来创建新的源文件 class文件
     */
    private Filer filer;
    /**
     * 包含用于操作TypeMirror工具方法
     */
    private Types typeUtils;
    /**
     * 包含了Element操作工具方法
     */
    private Elements elementsUtils;

    /**
     * 初始化成员变量
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        typeUtils = processingEnvironment.getTypeUtils();
        elementsUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    /**
     * 获取支持注解的类型
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(OnPermission.class.getCanonicalName());
        types.add(OnPermissionDenied.class.getCanonicalName());
        types.add(OnShowRationale.class.getCanonicalName());
        types.add(OnNeverAskAgain.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //返回一个注解处理器 支持的最新源版本 JDK
        return SourceVersion.latest();
    }

    /**
     * 注解处理器的核心方法，处理具体的注解实现，生成java文件
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        HashMap<String, List<ExecutableElement>> permissionsMap = getElementMap(roundEnvironment, OnPermission.class);
        HashMap<String, List<ExecutableElement>> neverAskAgainMap = getElementMap(roundEnvironment, OnNeverAskAgain.class);
        HashMap<String, List<ExecutableElement>> permissionDeniedMap = getElementMap(roundEnvironment, OnPermissionDenied.class);
        HashMap<String, List<ExecutableElement>> showRationaleMap = getElementMap(roundEnvironment, OnShowRationale.class);

        //创建class类 获取Activity完整字符串类名 （包名+类名）
        for (String activityName : permissionsMap.keySet()) {
            //获取所有方法集合
            List<ExecutableElement> permissionElements = permissionsMap.get(activityName);
            List<ExecutableElement> neverAskAgainElements = neverAskAgainMap.get(activityName);
            List<ExecutableElement> permissionDeniedElements = permissionDeniedMap.get(activityName);
            List<ExecutableElement> showRationaleElements = showRationaleMap.get(activityName);

            final String CLASS_SUFFIX = "$Permissions";
            //创建java源文件 class ，并返回一个对象 允许写入（Activity$Permissions）
            try {
                JavaFileObject javaFileObject = filer.createSourceFile(activityName + CLASS_SUFFIX);
                String packageName = getPackageName(permissionDeniedElements.get(0));

                //定义Writer对象，开始写入代码
                Writer writer = javaFileObject.openWriter();

                //类名:Activity$Permission,不是com.lee.code.MainActivity$Permissions
                //通过属性元素获取它所属的Activity类名，在拼接后结果为:Activity$Permissions
                String activitySimpleName = permissionElements.get(0).getEnclosingElement().getSimpleName().toString() + CLASS_SUFFIX;

                //生成包
                writer.write("package "+packageName+";\n");
                //生成要导入的接口类
                writer.write("import code.lee.code.library.listener.RequestPermission;\n");
                writer.write("import code.lee.code.library.listener.PermissionRequest;\n");
                writer.write("import code.lee.code.library.utils.PermissionUtils;\n");
                writer.write("import android.support.v7.app.AppCompatActivity;\n");
                writer.write("import android.support.v7.app.ActivityCompat;\n");
                writer.write("import android.support.annotation.NonNull;\n");
                writer.write("import java.lang.ref.WeakReference;\n");

                //生成类
                writer.write("public class "+activitySimpleName+" implements RequestPermission<"+activityName+"> {\n");

                //生成常量属性
                writer.write("private static final int REQUEST_SHOWCAMERA = 666;\n");
                writer.write("private static String[] PERMISSION_SHOWCAMERA;\n");

                //生成requestPermission方法
                writer.write("public void requestPermission("+activityName+" target,String[] permissions){\n");
                writer.write("PERMISSION_SHOWCAMERA = permissions;\n");
                writer.write("if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {\n");

                // 循环生成MainActivity每个权限申请方法
                for (ExecutableElement executableElement : permissionElements) {
                    // 获取方法名
                    String methodName = executableElement.getSimpleName().toString();
                    // 调用申请权限方法
                    writer.write("target." + methodName + "();\n");
                }

                writer.write("} else if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {\n");

                // 循环生成MainActivity每个提示用户为何要开启权限方法
                if (showRationaleElements != null && !showRationaleElements.isEmpty()) {
                    for (ExecutableElement executableElement : showRationaleElements) {
                        // 获取方法名
                        String methodName = executableElement.getSimpleName().toString();
                        // 调用提示用户为何要开启权限方法
                        writer.write("target." + methodName + "(new PermissionRequestImpl(target));\n");
                    }
                }

                writer.write("} else {\n");
                writer.write("ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);\n}\n}\n");

                // 生成onRequestPermissionsResult方法
                writer.write("public void onRequestPermissionsResult(" + activityName + " target, int requestCode, @NonNull int[] grantResults) {");
                writer.write("switch(requestCode) {\n");
                writer.write("case REQUEST_SHOWCAMERA:\n");
                writer.write("if (PermissionUtils.verifyPermissions(grantResults)) {\n");

                // 循环生成MainActivity每个权限申请方法
                for (ExecutableElement executableElement : permissionElements) {
                    // 获取方法名
                    String methodName = executableElement.getSimpleName().toString();
                    // 调用申请权限方法
                    writer.write("target." + methodName + "();\n");
                }

                writer.write("} else if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {\n");

                // 循环生成MainActivity每个不再询问后的提示
                if (neverAskAgainElements != null && !neverAskAgainElements.isEmpty()) {
                    for (ExecutableElement executableElement : neverAskAgainElements) {
                        // 获取方法名
                        String methodName = executableElement.getSimpleName().toString();
                        // 调用不再询问后的提示
                        writer.write("target." + methodName + "();\n");
                    }
                }

                writer.write("} else {\n");

                // 循环生成MainActivity每个拒绝时的提示方法
                if (permissionDeniedElements != null && !permissionDeniedElements.isEmpty()) {
                    for (ExecutableElement executableElement : permissionDeniedElements) {
                        // 获取方法名
                        String methodName = executableElement.getSimpleName().toString();
                        // 调用拒绝时的提示方法
                        writer.write("target." + methodName + "();\n");
                    }
                }

                writer.write("}\nbreak;\ndefault:\nbreak;\n}\n}\n");

                // 生成接口实现类：PermissionRequestImpl implements PermissionRequest
                writer.write("private static final class PermissionRequestImpl implements PermissionRequest {\n");
                writer.write("private final WeakReference<" + activityName + "> weakTarget;\n");
                writer.write("private PermissionRequestImpl(" + activityName + " target) {\n");
                writer.write("this.weakTarget = new WeakReference(target);\n}\n");
                writer.write("public void proceed() {\n");
                writer.write(activityName + " target = (" + activityName + ")this.weakTarget.get();\n");
                writer.write("if (target != null) {\n");
                writer.write("ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);\n}\n}\n}\n");


                writer.write("\n");
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private HashMap<String,List<ExecutableElement>> getElementMap(RoundEnvironment roundEnvironment , Class<? extends Annotation> clazz){
        //获取Activity中所有设置注解的方法
        Set<? extends Element> permissionsSet = roundEnvironment.getElementsAnnotatedWith(clazz);
        //保存起来，键值对：key com.xxx.Activity value 所有的带注解的方法
        HashMap<String, List<ExecutableElement>> permissionsMap = new HashMap<>();
        //遍历所有注解方法
        for (Element element : permissionsSet) {
            //转换成原始方法元素 （结构体元素）
            ExecutableElement executableElement = (ExecutableElement) element;
            //通过方法元素获取它所属的Activity类名 如：com.lee.code.MainActivity
            String activityName = getActivityName(executableElement);
            //从缓存集合中获取MainActivity所有带OnPermission注解的方法集合
            List<ExecutableElement> list = permissionsMap.get(activityName);
            if (list == null) {
                list = new ArrayList<>();
                //先加入map集合，引用变量list可以动态改变值
                permissionsMap.put(activityName, list);
            }
            //将Activity所有带OnPermission注解的方法计入到list集合
            list.add(executableElement);
        }
        return permissionsMap;
    }

    /**
     * 获取activity包名加完整路径名称
     * @param executableElement
     * @return
     */
    private String getActivityName(ExecutableElement executableElement) {
        //通过方法标签获取类名标签，再通过类名标签获取包名标签
        String packageName = getPackageName(executableElement);
        //通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        //完整字符串拼接
        return packageName + "." + typeElement.getSimpleName().toString();
    }

    /**
     * 返回传入元素的的包名路径
     * @param executableElement
     * @return
     */
    private String getPackageName(ExecutableElement executableElement) {
        //通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        //通过类名标签获取包名标签
        return elementsUtils.getPackageOf(typeElement).getQualifiedName().toString();
    }
}
