/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package util;
import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * @author Phil Zoio
 */
public class MemoryUtils {

    /**
     * Returns used memory in MB
     */
    public double usedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return usedMemory(runtime);
    }

    /**
     * Returns max memory available MB
     */
    public double maxMemory() {
        Runtime runtime = Runtime.getRuntime();
        return maxMemory(runtime);
    }
    
    double usedMemory(Runtime runtime) {
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        double usedMemory = (double)(totalMemory - freeMemory) / (double)(1024 * 1024);
        return usedMemory;
    }

    double maxMemory(Runtime runtime) {
        long maxMemory = runtime.maxMemory();
        double memory = (double)maxMemory / (double)(1024 * 1024);
        return memory;
    }

    public void printMemoryInfo() {
        StringBuffer buffer = getMemoryInfo();
    }

    public StringBuffer getMemoryInfo() {
        StringBuffer buffer = new StringBuffer();
        
        freeMemory();

        Runtime runtime = Runtime.getRuntime();
        double usedMemory = usedMemory(runtime);
        double maxMemory = maxMemory(runtime);

        NumberFormat f = new DecimalFormat("###,##0.0");
        
        String lineSeparator = System.getProperty("line.separator");
        buffer.append("Used memory: " + f.format(usedMemory) + "MB").append(lineSeparator);
        buffer.append("Max available memory: " + f.format(maxMemory) + "MB").append(lineSeparator);
        return buffer;
    }

    public void freeMemory() {
        System.gc();
        System.runFinalization();
    }
}
