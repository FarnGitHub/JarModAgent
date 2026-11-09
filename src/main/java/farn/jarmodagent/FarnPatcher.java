package farn.jarmodagent;

import farn.jarmodagent.legacyfix.LF3Transformer;
import net.lenni0451.classtransform.transformer.IBytecodeTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class FarnPatcher {
    public static final List<IBytecodeTransformer> specialTransformer = new ArrayList<>();

    public static boolean handleSpecialMod(JarFile jar) {
        try {
            Manifest mf = jar.getManifest();
            if(mf == null) return false;

            String premain = mf.getMainAttributes().getValue("Premain-Class");
            if(premain == null) return false;

            if(premain.startsWith("uk.betacraft.legacyfix")) {
                System.out.println("[JarModAgent] Found LegacyFix, Initialize LegacyFix..");
                specialTransformer.add(new LF3Transformer());
                return true;
            }
            return false;
        } catch (Exception ignored) {
            return false;
        }
    }
}
