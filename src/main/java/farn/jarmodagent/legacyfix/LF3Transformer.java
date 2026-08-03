package farn.jarmodagent.legacyfix;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import net.lenni0451.classtransform.transformer.IBytecodeTransformer;
import uk.betacraft.legacyfix.patch.Patcher;
import uk.betacraft.legacyfix.patch.api.CtTransformer;
import uk.betacraft.legacyfix.patch.api.Transformer;

import java.util.List;

public class LF3Transformer implements IBytecodeTransformer {

    private final Patcher patcher;
    private final ClassPool mainPool;

    public LF3Transformer() {
        ClassPool pool = new ClassPool(true);
        this.mainPool = new ClassPool(pool);
        ClassPool ctPool = new ClassPool(ClassPool.getDefault());
        ctPool.childFirstLookup = true;
        patcher = new Patcher(ctPool);
        patcher.apply();
    }

    @Override
    public byte[] transform(String name, byte[] bytecode, boolean calculateStackMapFrames) {
        if (bytecode == null || name.startsWith("javassist")) {
            return null;
        } else {
            boolean modified = false;
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
                        modified = true;
                    }

                    ctClass.detach();
                } catch (Exception e) {
                    throw new RuntimeException("[LegacyFix] Failed to apply CtTransformer on class \"" + name + "\"", e);
                }
            }

            for(Transformer transformer : this.patcher.getTransformers()) {
                try {
                    byte[] transformed = transformer.transform(name, bytecode);
                    if (transformed != null) {
                        bytecode = transformed;
                        modified = true;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("[LegacyFix] Failed to apply transformer on class \"" + name + "\"", e);
                }
            }

            return modified ? bytecode : null;
        }
    }
}
