package xyz.wagyourtail.unimined.jarmodagent;

import xyz.wagyourtail.unimined.jarmodagent.transformer.JarModder;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class JarModAgent {
    public static final boolean DEBUG = Boolean.getBoolean("jma.debug");
    public static final String VERSION = JarModAgent.class.getPackage().getImplementationVersion();

    /**
     * property for META-INF/MANIFEST.MF to specify the refmaps to load.
     * @since 0.1.3
     */
    public static final String JMA_REFMAPS_PROPERTY = "JarModAgent-Refmaps";

    /**
     * property for META-INF/MANIFEST.MF to specify the transforms to load.
     */
    public static final String JMA_TRANSFORMS_PROPERTY = "JarModAgent-Transforms";

    /**
     * property for META-INF/MANIFEST.MF to specify any {@link farn.jarmodagent.ExtraTransformerRegister}'s to load.
     * @since 0.1.4
     */
    public static final String JMA_TRANSFORMER_REGISTER_PROPERTY = "JarModAgent-ExtraTransformerRegister";

    /**
     * File.pathSeparator separated list of transformers to load. these are files containing
     * a list of classes for ClassTransform to load, separated by newlines.
     */
    public static final String TRANSFORMERS = "jma.transformers";

    /**
     * File.pathSeparator separated list of refmaps to load.
     * @since 0.1.3
     */
    public static final String REFMAPS = "jma.refmaps";

    /**
     * File.pathSeparator separated list of {@link farn.jarmodagent.ExtraTransformerRegister}'s to load.
     * @since 0.1.4
     */
    public static final String TRANSFORMER_REGISTER = "jma.extra.transformers.register";

    /**
     * File.pathSeparator separated list of files that make up the "priority classpath".
     * this is the classpath that will be searched first for classes. and takes priority over classes
     * with the same name that aren't on this list
     */
    public static final String PRIORITY_CLASSPATH = "jma.priorityClasspath";

    /**
     * Folder location to automatically search and append all files in it to the priority classpath.
     */
    public static final String MODS_FOLDER = "jma.modsFolder";

    /**
     * Disable the mods folder. this will prevent the mods folder from being searched and appended to the priority classpath.
     * @since 0.1.3
     */
    public static final String DISABLE_MODS_FOLDER = "jma.disableModsFolder";

    /**
     * Load the mods folder to the system classloader. this will load all classes in the mods folder to the system classloader
     * this should be set to false if another thing is loading the mods folder to a classloader
     * @since 0.1.3
     */
    public static final String DISABLE_INSERT_INTO_SYSTEM_CL = "jma.disableInsertIntoSystemCL";

    /**
     * Don't search in sub-folders of the mods folder.
     * @since 0.1.3
     */
    public static final String DISABLE_MODS_FOLDER_RECURSIVE = "jma.disableModsFolderRecursive";

    public static void agentmain(String agentArgs, Instrumentation inst) throws IOException, ClassNotFoundException {
        premain(agentArgs, inst);
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) throws IOException, ClassNotFoundException {
        System.out.println("[JarModAgent] Starting agent");
        System.out.println("[JarModAgent] Version: " + VERSION);
        JarModder jarModder = new JarModder(instrumentation);
        jarModder.register();
        instrumentation.addTransformer(jarModder);
        System.out.println("[JarModAgent] Agent started");
    }

}
