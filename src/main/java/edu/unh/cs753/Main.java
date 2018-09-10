
/**
 * Main.java
 * 
 * UNH CS753
 * Progrmming Assignment 2
 * Group 2
 * 
 * This is the Main file for our second programming assignment.
 * 
**/



package edu.unh.cs753;


import java.io.IOException;

import edu.unh.cs753.indexing.LuceneSearcher;
import edu.unh.cs753.indexing.LuceneIndexer;



public class Main {
	
	
	// Main class for project
	public static void main(String[] args) throws IOException {
		
		System.setProperty("file.encoding", "UTF-8");		

		final String paraFilePath = "cbor_files/train.pages.cbor-paragraphs.cbor";
		final String indexPath = "paragraphs";

		System.out.println("\n\n===Programming Assignment 2 Team 2===");
		System.out.println("--Part1");

		long startTime = 0;
		float secondsElapsed;
		LuceneIndexer indexer = new LuceneIndexer(indexPath);

		// run the indexing mechanism
		System.out.println("Creating index...");
		startTime = System.currentTimeMillis();
		indexer.doIndex(paraFilePath);
		secondsElapsed = (System.currentTimeMillis() - startTime) / 1000.0f;
		System.out.format("   index created (%.2f seconds)\n", secondsElapsed);

		// create the searcher
		LuceneSearcher searcher = new LuceneSearcher(indexPath);
		
		// create run files (part 1)
		System.out.println("Creating run files...");
		startTime = System.currentTimeMillis();
		createRunFiles(searcher);
		secondsElapsed = (System.currentTimeMillis() - startTime) / 1000.0f;
		System.out.format("   run files created (%.2f seconds)\n", secondsElapsed);
		
		// the bash script will call the python trec_eval script
		System.out.println("--Part2");
		System.out.println("Using trec_eval to find RPrec, map and ndcg@20 values...");

	}
	
	
	private static void createRunFiles(LuceneSearcher searcher) throws IOException {
		
		final String pageFilePath = "cbor_files/train.pages.cbor-outlines.cbor";
		
		// use the searcher to make runfiles
		searcher.makeRunFile(pageFilePath); // default ranking function 
		searcher.custom(); // custom scoring function
		searcher.makeRunFile(pageFilePath);
		
	}

}
