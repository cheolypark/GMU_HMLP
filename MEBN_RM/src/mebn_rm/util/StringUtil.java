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
import java.io.FileNotFoundException; 
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
 
  
public class StringUtil {
	HashSet<String> dictionary = new HashSet<String>();
	
	public static StringUtil stringUtil = new StringUtil();
    
    public static StringUtil This() {
        return stringUtil;
    }     
	
    public String getLeft(String s) {
        String left = s.substring(0, s.indexOf("."));
        return left;
    }

    public String getRight(String s) {
        String right = s.substring(s.indexOf(".") + 1, s.length());
        return right;
    }
  
    public String createAbbreviation(String input) { 
		String str = "";
		List<List<String>> results = this.searchWordsInDictionary(input);
		if (!results.isEmpty()) {
		    List<String> list = results.get(results.size() - 1);
		    
		    Iterator<String> iterator = list.iterator();
		    while (iterator.hasNext()) {
		        String s = iterator.next();
		        s = s.substring(0, 1);
		        str = String.valueOf(str) + s.toUpperCase();
		    }
		} else {
			String s1 = input.substring(0, 1);
			String s2 = input.substring((int)(input.length()/2), (int)(input.length()/2) + 1);
			String s3 = input.substring(input.length()-1, input.length());
			str = s1.toUpperCase() + s2.toUpperCase() + s3.toUpperCase();
		}
		
		return str;
    }

    public List<List<String>> searchWordsInDictionary(String input) {
    	if (dictionary.isEmpty()) {
	        try {
	            Scanner filescan = new Scanner(new File("util/dictionary.txt"));
	            while (filescan.hasNext()) {
	                dictionary.add(filescan.nextLine().toLowerCase());
	            }
	        }
	        catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
    	}
        input = input.replace("_", "");
        input = input.replace(" ", "");
        ArrayList<List<String>> results = new ArrayList<List<String>>(); 
        Stack<String> words = new Stack<String>();
        this.search(input, dictionary, words, results); 
        
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
 
    // remove redundant items
    // e.g) heateractuator_item, slabinput_item, slabinput_item, slabinput_item
    // => heateractuator_item, slabinput_item
    public String removeRedundantItem (String sc) {
    	String s = "";
    	
    	List<String> items = Arrays.asList(sc.split("\\s*,\\s*"));
    	List<String> new_items = new ArrayList<String>(); 
    	
    	for (String i : items){
    		if (!new_items.contains(i)) {
    			new_items.add(i);
    		}
    	}
    	
    	for (String i : new_items){
    		s += i + ", ";
    	}
    	
    	s = s.substring(0, s.length() - 2);
    	   
    	return s;
    }
    
    public static void main(String[] args) {
    	StringUtil u = new StringUtil();
    	String list = u.createAbbreviation("finishingmill"); 
    }
}

