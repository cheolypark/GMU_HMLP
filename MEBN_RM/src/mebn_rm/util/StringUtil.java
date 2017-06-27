/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class StringUtil {
    public String getLeft(String s) {
        String left = s.substring(0, s.indexOf("."));
        return left;
    }

    public String getRight(String s) {
        String right = s.substring(s.indexOf(".") + 1, s.length());
        return right;
    }

    public String createAbbreviation(String input) { 
        return input;
    }
    
//    public String createAbbreviation(String input) {
//    	System.out.println(input);
//        List<List<String>> results = this.searchWordsInDictionary(input);
//        if (results.isEmpty())
//        	return input;
//        
//        List<String> list = results.get(results.size() - 1);
//        String str = "";
//        Iterator<String> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            String s = iterator.next();
//            s = s.substring(0, 1);
//            str = String.valueOf(str) + s.toUpperCase();
//        }
//        
//        return str;
//    }

    public List<List<String>> searchWordsInDictionary(String input) {
        HashSet<String> dictionary = new HashSet<String>();
        try {
            Scanner filescan = new Scanner(new File("util/dictionary.txt"));
            while (filescan.hasNext()) {
                dictionary.add(filescan.nextLine().toLowerCase());
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        input = input.replace("_", "");
        input = input.replace(" ", "");
        ArrayList<List<String>> results = new ArrayList<List<String>>();
        long time = System.currentTimeMillis();
        Stack<String> words = new Stack<String>();
        this.search(input, dictionary, words, results);
        time = System.currentTimeMillis() - time;
        System.out.println(words);
        for (List<String> result : results) {
            for (String word : result) {
                System.out.print(String.valueOf(word) + " ");
            }
            System.out.println("(" + result.size() + " words)");
        }
        System.out.println();
        System.out.println("Took " + time + "ms");
        return results;
    }

    void search(String input, Set<String> dictionary, Stack<String> words, List<List<String>> results) {
        int i = 0;
        while (i < input.length()) {
            String substring = input.substring(0, i + 1);
            if (dictionary.contains(substring)) {
                words.push(substring);
                if (i == input.length() - 1) {
                    results.add(new ArrayList<String>(words));
                } else {
                    this.search(input.substring(i + 1), dictionary, words, results);
                }
                words.pop();
            }
            ++i;
        }
    }
 
    public static void main(String[] args) {
    	StringUtil u = new StringUtil();
    	String list = u.createAbbreviation("finishingmill");
    	System.out.println(list);
    }
}

