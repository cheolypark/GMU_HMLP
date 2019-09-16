/*
 * HML Core
 * Copyright (C) 2017 Cheol Young Park
 * 
 * This file is part of HML Core.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mebn_rm.util;

import java.io.File; 

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
            boolean result = false;
            try {
                theDir.mkdir();
                result = true;
            }
            catch (SecurityException var3_3) {
                // empty catch block
            } 
        }
    }
}

