package org.jetbrains.research.smtlib.utils;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

import com.intellij.openapi.diagnostic.Logger;

public class ClassLoaderUtils {
    private ClassLoaderUtils() {
    }

    public static void ensureLibraryPath(@NotNull final String path) {
        final String libraryPath = System.getProperty(LD_PATH_PROPERTY);
//        if (libraryPath.contains(path)) return;
        final String updatedPath = String.join(";", path, libraryPath);
        System.setProperty(LD_PATH_PROPERTY, updatedPath);
        resetClassLoaderPaths();
    }

    private static void resetClassLoaderPaths() {
        try {
            final MethodHandles.Lookup cl = MethodHandles.privateLookupIn(ClassLoader.class, MethodHandles.lookup());
            if (!resetClassLoaderPathsJBRRuntime(cl)) {
                resetClassLoaderPathsNormalRuntime(cl);
            }
        } catch (Throwable e) {
            LOG.warn("Error while resetting library path", e);
        }
    }

    private static boolean resetClassLoaderPathsJBRRuntime(MethodHandles.Lookup cl) throws Throwable {
        final MethodHandle pathInitializer;
        try {
            pathInitializer = cl.findStatic(ClassLoader.class, "initLibraryPaths", MethodType.methodType(void.class));
        } catch (NoSuchMethodException e) {
            return false;
        }
        pathInitializer.invoke();
        return true;
    }

    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    private static void resetClassLoaderPathsNormalRuntime(MethodHandles.Lookup cl) throws Throwable {
        final VarHandle sysPath = cl.findStaticVarHandle(ClassLoader.class, "sys_paths", String[].class);
        sysPath.set(null);
    }

    private static final String LD_PATH_PROPERTY = "java.library.path";
    private static final Logger LOG = Logger.getInstance(ClassLoaderUtils.class);
}
