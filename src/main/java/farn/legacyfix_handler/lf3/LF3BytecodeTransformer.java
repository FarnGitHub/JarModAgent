package farn.legacyfix_handler.lf3;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import net.lenni0451.classtransform.transformer.IBytecodeTransformer;
import uk.betacraft.legacyfix.patch.Patcher;
import uk.betacraft.legacyfix.patch.api.CtTransformer;
import uk.betacraft.legacyfix.patch.api.Transformer;

import java.util.List;

public class LF3BytecodeTransformer implements IBytecodeTransformer {

    private final Patcher patcher;
    private final ClassPool mainPool;

    public LF3BytecodeTransformer() {
        ClassPool pool = new ClassPool(true);
        this.mainPool = new ClassPool(pool);
        ClassPool ctPool = new ClassPool(ClassPool.getDefault());
        ctPool.childFirstLookup = true;
        patcher = new Patcher(ctPool);
        patcher.apply();
    }

    @Override
    public byte[] transform(String name, byte[] bytecode, boolean calculateStackMapFrames) {
        if (bytecode == null) {
            return bytecode;
        } else if (name.startsWith("javassist")) {
            return bytecode;
        } else {
            List<CtTransformer> ctTransformers = this.patcher.getCtTransformers().get(name);
            if (ctTransformers != null && !ctTransformers.isEmpty()) {
                try {
                    ClassPool ctPool = new ClassPool(this.mainPool);
                    ctPool.childFirstLookup = true;
                    ctPool.insertClassPath(new ByteArrayClassPath(name, bytecode));
                    CtClass ctClass = ctPool.get(name);

                    for(CtTransformer ctTransformer : ctTransformers) {
                        ctTransformer.transform(ctClass);
                    }

                    if (ctClass.isModified()) {
                        bytecode = ctClass.toBytecode();
                    }

                    ctClass.detach();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to apply CtTransformer on class \"" + name + "\"", e);
                }
            }

            for(Transformer transformer : this.patcher.getTransformers()) {
                try {
                    byte[] transformed = transformer.transform(name, bytecode);
                    if (transformed != null) {
                        bytecode = transformed;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to apply transformer on class \"" + name + "\"", e);
                }
            }

            return bytecode;
        }
    }
}
