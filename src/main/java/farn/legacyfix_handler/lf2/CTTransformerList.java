package farn.legacyfix_handler.lf2;

import javassist.CtClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CTTransformerList {
    public static HashMap<String, List<CtClass>> ctClassList;

    public static void add(CtClass ctClass) {
        if(ctClassList == null) ctClassList = new HashMap<>();
        List<CtClass> theList =
                ctClassList.computeIfAbsent(ctClass.getName(), k -> new ArrayList<>());
        theList.add(ctClass);
    }

    public static List<CtClass> getCTTransformer(String className) {
        return ctClassList.get(className);
    }
}
