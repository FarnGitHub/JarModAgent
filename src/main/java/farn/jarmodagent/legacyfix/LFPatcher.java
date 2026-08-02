package farn.jarmodagent.legacyfix;

import farn.jarmodagent.ExtraTransformerRegister;
import net.lenni0451.classtransform.TransformerManager;

import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

@SuppressWarnings("unused")
public class LFPatcher implements ExtraTransformerRegister {

    @Override
    public void registerTransformer(List<JarFile> jars, TransformerManager manager) {
        for(JarFile jar : jars) {
            try {
                Manifest mf = jar.getManifest();
                if (mf != null) {
                    String premain = mf.getMainAttributes().getValue("Premain-Class");
                    if(premain != null) {
                        if(premain.equals("uk.betacraft.legacyfix.Agent")) {
                            System.out.println("[JarModAgent] Found LegacyFix, Apply Patch");
                            manager.addBytecodeTransformer(new LF3Transformer());
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }
}
