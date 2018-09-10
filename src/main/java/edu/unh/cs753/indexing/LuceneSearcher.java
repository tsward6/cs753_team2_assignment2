package edu.unh.cs753.indexing;

import edu.unh.cs753.utils.SearchUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

import java.io.IOException;

import java.util.ArrayList;
import edu.unh.cs.treccar_v2.Data;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class LuceneSearcher {
	
    public final IndexSearcher searcher;
    private String methodName;


	/** 
	 * Construct a Lucene Searcher.
	 * @param indexLoc: the path containing the index.
    */
    public LuceneSearcher(String indexLoc) {
        searcher = SearchUtils.createIndexSearcher(indexLoc);
        methodName = "default";
    }

    /**
     * Function: query
     * Desc: Queries Lucene paragraph corpus using a standard similarity function.
     *       Note that this uses the StandardAnalyzer.
     * @param queryString: The query string that will be turned into a boolean query.
     * @param nResults: How many search results should be returned
     * @return TopDocs (ranked results matching query)
     */
    public TopDocs query(String queryString, Integer nResults) {
        Query q = SearchUtils.createStandardBooleanQuery(queryString, "text");
        try {
            return searcher.search(q, nResults);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


	/**
     * Function: custom
     * Desc: A custom scoring function which is the sum of hits within a document.
     */
    public void custom() throws IOException {
        methodName = "custom";
        SimilarityBase mysimilarity= new SimilarityBase() {
            @Override
            protected float score(BasicStats basicStats, float v, float v1) {
                float sum1 = 0.0f;
                sum1 += v;
                return sum1;
            }

            @Override
            public String toString() {
                return null;
            }
        };
        searcher.setSimilarity(mysimilarity);
    }
    
    
    /**
     * Function: makeRunFile
     * Desc: Retrieve page name queries and create the run files.
     * @param cborLoc: The path where the page cbor file exists.
     */
    public void makeRunFile(String cborLoc) throws IOException {
		
		final int topN = 100;
		
		if(!Files.exists(Paths.get("./run_files"))) 
			(new File("run_files")).mkdir(); // make a directory for run files
		
		String outFileName = "run_files/" + methodName + "-runfile.txt";
		PrintWriter writer = new PrintWriter(outFileName, "UTF-8");
		// iterate through each page
        for (Data.Page p : SearchUtils.createPageIterator(cborLoc)) {
			String queryId = p.getPageId();
			String keywordQuery = p.getPageName();
			TopDocs topDocs = query(keywordQuery, topN);
			int rank = topDocs.scoreDocs.length;
			// iterate through n (<= 100) top docs and output to run file  
			for (ScoreDoc sd : topDocs.scoreDocs) { 
				Document doc = searcher.doc(sd.doc);
				String paraId = doc.get("id");
				String score = Float.toString(sd.score);
				String out = queryId + " Q0 " + paraId + " " + rank--
				             + " " + score + " group2-" + methodName;		 
				writer.println(out);
			}
        }
		writer.close();
    }
    
    
       
}
