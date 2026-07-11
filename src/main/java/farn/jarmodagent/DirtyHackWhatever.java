package farn.jarmodagent;

import net.lenni0451.classtransform.TransformerManager;
import net.lenni0451.classtransform.transformer.AnnotationHandler;
import net.lenni0451.classtransform.transformer.types.RemovingAnnotationHandler;
import net.lenni0451.classtransform.utils.MethodInliner;
import net.lenni0451.classtransform.utils.annotations.AnnotationUtils;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DirtyHackWhatever extends AnnotationHandler {
    public static final List<Class<?>> theWhatever = new ArrayList<>();

    @Override
    public void transform(TransformerManager transformerManager, ClassNode transformedClass, ClassNode transformer) {
        List<MethodNode> methodsToInline = transformedClass.methods
                .stream()
                .filter(methodNode -> hasInlinableAnnotation(transformerManager, methodNode))
                .collect(Collectors.toList());
        for (MethodNode methodNode : methodsToInline) MethodInliner.wrappedInline(transformedClass, methodNode, transformedClass.name);
    }

    private boolean hasInlinableAnnotation(TransformerManager transformerManager, MethodNode methodNode) {
        for(Class<?> classEs : getInjectableAnnotations(transformerManager)) {
            if(AnnotationUtils.hasAnnotation(methodNode, classEs))
                return true;
        }
        return false;
    }

    public static List<Class<?>> getInjectableAnnotations(TransformerManager transformerManager) {
        if(theWhatever.isEmpty()) {
            List<AnnotationHandler> annotationHandlers = (List<AnnotationHandler>)getField("annotationHandler", TransformerManager.class, transformerManager);
            for(AnnotationHandler annotationHandler : annotationHandlers){
                if(annotationHandler instanceof RemovingAnnotationHandler) {
                    Class<?> classa = (Class<?>)getField("annotationClass", RemovingAnnotationHandler.class, annotationHandler);
                    theWhatever.add(classa);
                }
            }
        }
        return theWhatever;
    }

    private static Object getField(String fieldName, Class<?> clazz, Object instance) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
