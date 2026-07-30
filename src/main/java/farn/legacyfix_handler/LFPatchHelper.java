package farn.legacyfix_handler;

import farn.legacyfix_handler.lf3.LF3BytecodeTransformer;
import net.lenni0451.classtransform.TransformerManager;

public class LFPatchHelper {
    public static int hasLF = 0;

    public static void patch(TransformerManager manager) {
        switch (hasLF) {
            case 1:
                throw new RuntimeException("Legacyfix 2.0 is not supported");
            case 2:
                manager.addBytecodeTransformer(new LF3BytecodeTransformer());
                break;
            default:
                System.out.println("[JarModAgent] No LegacyFix install");
        }

    }

}
