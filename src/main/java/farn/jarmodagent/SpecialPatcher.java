package farn.jarmodagent;

import farn.jarmodagent.legacyfix.LF3Transformer;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class SpecialPatcher {
    public static final List<BytecodeTransformer> specialTransformer = new ArrayList<>();
    public static JarFile legacyFixJar;

    public static boolean handleSpecialMod(JarFile jar) {
        try {
            Manifest mf = jar.getManifest();
            if(mf == null) return false;

            String premain = mf.getMainAttributes().getValue("Premain-Class");
            if(premain == null) return false;

            if(premain.startsWith("uk.betacraft.legacyfix")) {
                legacyFixJar = jar;
                return true;
            }
            return false;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static void postHandle(Instrumentation inst) {
        if(legacyFixJar != null) {
            System.out.println("[JarModAgent] Found LegacyFix, Initialize LegacyFix..");
            inst.appendToSystemClassLoaderSearch(legacyFixJar);
            specialTransformer.add(new LF3Transformer());
        }
    }
}
