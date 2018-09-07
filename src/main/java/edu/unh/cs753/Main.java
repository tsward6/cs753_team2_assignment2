package edu.unh.cs753;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.unh.cs753.indexing.LuceneSearcher;
import edu.unh.cs753.indexing.LuceneIndexer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
import kotlin.jvm.functions.Function3;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

public class Main {

	// Main class for project
	public static void main(String[] args) throws IOException {
		
		System.setProperty("file.encoding", "UTF-8");		

		String paraFilePath = "cbor_files/train.pages.cbor-paragraphs.cbor";
		String pageFilePath = "cbor_files/train.pages.cbor-outlines.cbor";
		String indexFilesPath = "paragraphs"; 

		// run the indexer 
		System.out.println("Creating the index...");
        LuceneIndexer indexer  = new LuceneIndexer(indexFilesPath); // The directory that will be mad
		indexer.doIndex(paraFilePath);
		
		// run the searcher and make runfiles
		System.out.println("Making run files...");
		LuceneSearcher searcher= new LuceneSearcher(indexFilesPath);
		searcher.makeRunFile(pageFilePath); // Lucene default ranking function runfile 
		searcher.custom(); // custom scoring function runfile
		searcher.makeRunFile(pageFilePath);
		

	}

}
