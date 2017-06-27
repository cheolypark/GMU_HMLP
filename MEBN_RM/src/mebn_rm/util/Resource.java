/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.util;

import java.io.File;
import java.io.PrintStream;

public class Resource {
    public static String getCSVPath(String strProject) {
        return Resource.createPath(strProject, "csv");
    }

    public static String getSQLPath(String strProject) {
        return Resource.createPath(strProject, "sql");
    }

    public static String getlearningOutputPath(String strProject) {
        return Resource.createPath(strProject, "Output");
    }

    public static String getlearningMTheoriesPath(String strProject) {
        return Resource.createPath(strProject, "MTheories");
    }

    public static String createPath(String strProject, String subPath) {
        Resource.createPath("projects");
        Resource.createPath("projects//" + strProject);
        Resource.createPath("projects//" + strProject + "//" + subPath);
        return "projects//" + strProject + "//" + subPath + "//";
    }

    public static void createPath(String strPath) {
        File theDir = new File(strPath);
        if (!theDir.exists()) {
            System.out.println("creating directory: " + strPath);
            boolean result = false;
            try {
                theDir.mkdir();
                result = true;
            }
            catch (SecurityException var3_3) {
                // empty catch block
            }
            if (result) {
                System.out.println("DIR created");
            }
        }
    }
}

