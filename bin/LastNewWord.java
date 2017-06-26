import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import ca.gedge.radixtree.*;

public class LastNewWord {
	
	static boolean caseSensitive = true,
	printNumbers = false;
	static String lastWord;
	static int lastWordCount = 0,
	wordCount = 0;
	
	/*
		RadixTreeUtil.largestPrefixLength("abcdefg", "abcdexyz")
	
		RadixTree<value> tree = new RadixTree<>();
	
		tree.size(); -> int
		tree.put(string, value); -> value?
		tree.containsKey(string); -> bool
		tree.get(string); -> entrée
		tree.getValuesWithPrefix(string) -> ArrayList<value>
		tree.values() -> ArrayList<value>
		tree.keySet() -> ArrayList<String>
		tree.remove(string);
	*/
	
	/* Add str in the tree */
	private static void treeAdd(RadixTree<Integer> tree, String str) {
		Integer tmp = tree.get(str);
		
		/* str is not in the tree so we add it */
		if (tmp == null) {
			tree.put(str, 0);
			lastWord = str;
			lastWordCount = wordCount;
		}
		/* str is already in the tree so we don't do nothing */

	}
	
	/* Read a stream (tokenised) and print back only words that are NOT in tree */
	private static void readStream(RadixTree<Integer> tree, String path) {
		
		Scanner in = null;
	
		if (path == null) {
			in = new Scanner(System.in);
			
		} else {
			try {
				in = new Scanner(new File(path));
			} catch (IOException e) {
				System.err.println("Fichier " + path + " introuvable.");
				return;
			}
		}
		
		while (in.hasNextLine()) {
			String line = in.nextLine();
			
			for (String word : line.split(" ")) {
				String tmp = word;
				if (!caseSensitive) {
					tmp = word.toLowerCase();
				}
			
				treeAdd(tree, word);
				wordCount++;
			}
		}
		
		wordCount--;
		System.out.println(lastWord);
		
		if (printNumbers) {
			System.out.println(lastWordCount + " " + wordCount);
		}
		
	}
	
	static void displayUsage() {
		System.err.print("Usage: java LastNewWord [-s] path_to_file\n"+
						 "Print on stdin the last new word and it's position\n"+
						 "Options :\n"+
						 "-s : makes the program case insensitive\n"+
						 "-n : prints the number of the last new word and the word count\n"+
						 " If path_to_file is '-' then programs reads from stdin\n");
	
		System.exit(0);
	}
	
	public static void main(String[] args) {
	
		RadixTree<Integer> tree = new RadixTree<>();
	
		if (args.length == 0)
			displayUsage();
		
		for (String i : args) {
			if (i.equals("-h") || i.equals("--help"))
				displayUsage();
		}
	
	
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			if (arg.equals("-s")) {
				caseSensitive = false;
				
			} else if (arg.equals("-n")) {
				printNumbers = true;
				
			} else if (arg.equals("-")) {
				System.err.println("Lecture de stdin");
				readStream(tree, null);
				
			} else if (arg.length() > 0){
				System.err.println("Lecture de " + args[i]);
				readStream(tree, arg);
			}
		
		}
	
	}
	
}
