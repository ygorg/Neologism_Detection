import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import ca.gedge.radixtree.*;

public class ExistingWord {
	
	static boolean caseSensitive = true;
	
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
	
	/* Add str to tree if exists, else increment value of str in tree */
	private static Integer treeAddCount(RadixTree<Integer> tree, String str) {
		Integer tmp = tree.get(str);
		return tree.put(str, tmp == null ? 1 : tmp + 1);
	}
	
	
	/* Create a RadixTree of a tokenised corpus */
	private static RadixTree<Integer> treeOfCorpus(String path) {
		BufferedReader br = null;

		try {
			br = new BufferedReader(
					new InputStreamReader(
						new FileInputStream(path),
						"UTF-8"));
						
			final RadixTree<Integer> tree = new RadixTree<>();
			
			String line = null;
			while((line = br.readLine()) != null) {
				line = line.trim();
				for(String word : line.split(" ")) {
					treeAddCount(tree, word);
				}
			}
			
			return tree;

		} catch (IOException e) {
			System.err.println("Erreur dans la lecture du corpus");
		} finally {
			if(br != null) {
				try {br.close();}
				catch (IOException e) {return null;}
			}
			return null;
		}
	
	}
	
	
	/* Create a RadixTree of a dictonnary (first word of each line) */
	private static RadixTree<Integer> treeOfDictionnary(String path) {
		BufferedReader br = null;
		final RadixTree<Integer> tree = new RadixTree<>();
		String line = null;

		try {
			br = new BufferedReader(
					new InputStreamReader(
						new FileInputStream(path),
						"UTF-8"
					)
				);
			
			while((line = br.readLine()) != null) {
				line = line.trim();
				if(line.length() > 0) {
					treeAddCount(tree, line.split(" ")[0].toLowerCase());
				}
			}
			
			br.close();
			
		} catch (IOException e) {
			System.err.println("Erreur dans la lecture du dictionnaire");
		}
		
		return tree;
		
	}
	
	/* Read a stream (tokenised) and print back only words that are NOT in tree */
	private static void readStream(RadixTree<Integer> tree, String path) {
	
		if (tree == null) {
			System.err.println("tree est null");
			return;
		}
	
		Scanner in = null;
	
		if (path == null) {
			in = new Scanner(System.in);
		}
		else {
			try {
				in = new Scanner(new File(path));
			} catch (IOException e) {
				System.err.println("Fichier " + path + " introuvable.");
				return;
			}
		}
		
		while (in.hasNextLine()) {
			String line = in.nextLine(),
					new_line = new String();
					
			for (String word : line.split(" ")) {
				String tmp = word;
				if (!caseSensitive) {
					tmp = word.toLowerCase();
				}
			
				if (!tree.containsKey(tmp)) {
					new_line = new_line.concat(word + " ");
				}
			}
			if (!new_line.equals("")) {
				System.out.println(new_line);
			}
		}
	}
	
	static void displayUsage() {
		System.err.print("Usage: java ExistingWord [-s] [-d path_to_dictionnary] path_to_file\n"+
						 " Remove existing word from a file. Existing words are contained in a dictionnary\n"+
						 "Options :\n"+
						 "-s : makes the program case insensitive\n"+
						 "-d : Use a dictionnary to remove\n"+
						 " If path_to_file is '-' then programs reads from stdin\n");
	
		System.exit(0);
	}
	
	public static void main(String[] args) {
	
		RadixTree<Integer> tree = null;
	
		if (args.length == 0)
			displayUsage();
		for (String i : args) {
			if (i.equals("-h") || i.equals("--help"))
				displayUsage();
		}
	
	
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			if (arg.equals("-d")) {
				i++;
				if (i < args.length) {
					System.err.println("Creation du tree a partir d'un dictionnaire : " + args[i]);
					tree = treeOfDictionnary(args[i]);
				}
				
			} else if (arg.equals("-c")) {
				i++;
				if (i < args.length) {
					System.err.println("Creation du tree a partir d'un corpus : " + args[i]);
					tree = treeOfCorpus(args[i]);
				}
			
			} else if (arg.equals("-s")) {
				caseSensitive = false;
			
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
