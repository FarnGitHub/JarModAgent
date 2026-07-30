package farn.legacyfix_handler;

import farn.legacyfix_handler.lf2.LF2ByteCodeTransformer;
import farn.legacyfix_handler.lf3.LF3BytecodeTransformer;
import net.lenni0451.classtransform.TransformerManager;

public class LFPatchHelper {
    public static int hasLF = 0;

    public static void patch(TransformerManager manager) {
        switch (hasLF) {
            case 1:
                manager.addBytecodeTransformer(new LF2ByteCodeTransformer(manager));
                break;
            case 2:
                manager.addBytecodeTransformer(new LF3BytecodeTransformer());
                break;
            default:
                System.out.println("[JarModAgent] No LegacyFix install");
        }

    }

}
