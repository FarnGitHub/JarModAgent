package farn.jarmodagent;

import net.lenni0451.classtransform.TransformerManager;

import java.util.List;
import java.util.jar.JarFile;

public interface ExtraTransformerRegister {

    void registerTransformer(List<JarFile> jars, TransformerManager manager);
}
