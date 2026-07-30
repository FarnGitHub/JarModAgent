package farn.legacyfix_handler.lf2;

import javassist.CannotCompileException;
import javassist.CtClass;
import net.lenni0451.classtransform.annotations.CTransformer;
import net.lenni0451.classtransform.annotations.injection.COverride;
import uk.betacraft.legacyfix.patch.Patch;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

@CTransformer(Patch.class)
public class PatchTransformer {

    @COverride
    protected void redefineClass(Instrumentation inst, CtClass clazz) throws IOException, CannotCompileException, ClassNotFoundException, UnmodifiableClassException {
        CTTransformerList.add(clazz);
    }
}
