package farn.legacyfix_handler.lf2;

import javassist.CtClass;
import net.lenni0451.classtransform.TransformerManager;
import net.lenni0451.classtransform.transformer.IBytecodeTransformer;
import uk.betacraft.legacyfix.LegacyFixAgent;

import java.util.List;

public class LF2ByteCodeTransformer implements IBytecodeTransformer {

    public LF2ByteCodeTransformer(TransformerManager manager) {
        manager.addTransformer("farn.legacyfix_handler.lf2.PatchTransformer");
        LegacyFixAgent.premain("", manager.getInstrumentation());
    }

    @Override
    public byte[] transform(String className, byte[] bytecode, boolean calculateStackMapFrames) {
        CtClass ctClass = CTTransformerList.getCTTransformer(className);
        if(ctClass != null) {
            try {
                bytecode = ctClass.toBytecode();
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        return bytecode;
    }
}
