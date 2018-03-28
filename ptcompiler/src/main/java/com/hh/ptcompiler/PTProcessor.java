package com.hh.ptcompiler;

import com.google.auto.service.AutoService;
import com.hh.ptannotation.PTRoutRule;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class PTProcessor  extends AbstractProcessor {
    private static final boolean DEBUG = true;
    private Messager messager;
    private Filer filer;

    /**
     * init()方法会被注解处理工具调用，并输入ProcessingEnviroment参数。
     * ProcessingEnviroment提供很多有用的工具类Elements, Types 和 Filer
     * @param processingEnvironment 提供给 processor 用来访问工具框架的环境
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    /**
     * 这里必须指定，这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称
     * @return  注解器所支持的注解类型集合，如果没有这样的类型，则返回一个空集合
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<>();
        ret.add(PTRoutRule.class.getCanonicalName());
        return ret;
    }

    /**
     * 指定使用的Java版本，通常这里返回SourceVersion.latestSupported()，默认返回SourceVersion.RELEASE_6
     * @return  使用的Java版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 这相当于每个处理器的主函数main()，你在这里写你的扫描、评估和处理注解的代码，以及生成Java文件。
     * 输入参数RoundEnviroment，可以让你查询出包含特定注解的被注解元素
     * @param annotations   请求处理的注解类型
     * @param roundEnvironment  有关当前和以前的信息环境
     * @return  如果返回 true，则这些注解已声明并且不要求后续 Processor 处理它们；
     *          如果返回 false，则这些注解未声明并且可能要求后续 Processor 处理它们
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        debug("process start------------" + annotations.toString());
        if (annotations.isEmpty()) {
            return false;
        }

        //添加init函数
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .addCode("\n");


        // process PTRoutRule
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(PTRoutRule.class);
        for (Element element : elements) {
            PTRoutRule router = element.getAnnotation(PTRoutRule.class);

            String url = null;
            for (String urlValue : router.value()) {
                debug(urlValue); //暂时支持一个url
                url = urlValue;
            }

            //添加注解的类
            ClassName className = null;
            if (element.getKind() == ElementKind.CLASS) {
                className = ClassName.get((TypeElement) element);
                debug(className.simpleName());
            }

            if(url == null){
                debug("url is null");
            }else if(className == null){
                debug("className is null");
            }else{
                if (element.getKind() == ElementKind.CLASS) {
                    debug("com.hh.ptrouter.PTRouter.Register:" + " url = " + url + " className = " + className);
                    methodBuilder.addStatement("com.hh.ptrouter.PTRouter.Register($S, $T.class)", url, className);
                }
            }
            methodBuilder.addCode("\n");
        }

        //创建类
        TypeSpec routerMapping = TypeSpec.classBuilder("PTRouterProcessor")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodBuilder.build())
                .build();
        //写入文件
        try {
            JavaFile.builder("com.hh.ptrouter", routerMapping)
                    .build()
                    .writeTo(filer);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        debug("process end------------" + annotations.toString());
        return true;
    }

    private void error(String error) {
        messager.printMessage(Diagnostic.Kind.ERROR, error);
    }

    private void debug(String msg) {
        if (DEBUG) {
            messager.printMessage(Diagnostic.Kind.NOTE, msg);
        }
    }
}
