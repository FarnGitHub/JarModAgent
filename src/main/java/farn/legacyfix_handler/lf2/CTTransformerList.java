package farn.legacyfix_handler.lf2;

import javassist.CtClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CTTransformerList {
    public static HashMap<String, CtClass> ctClassList;

    public static void add(CtClass ctClass) {
        if(ctClassList == null) ctClassList = new HashMap<>();
        ctClassList.put(ctClass.getName(), ctClass);
    }

    public static CtClass getCTTransformer(String className) {
        return ctClassList.get(className);
    }
}
