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

FRUMPY BONERS

public class LuceneSearcher {
    public final IndexSearcher searcher;
    boolean isCustom;

    public LuceneSearcher(String indexLoc) {
        searcher = SearchUtils.createIndexSearcher(indexLoc);
        isCustom = false;
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

    public void custom () throws IOException {
        isCustom = true;
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
    
    public void makeRunFile(String cborLoc) throws IOException {
		
		String rank;
		if(!isCustom)
			rank = "default";
		else
			rank = "custom";
		
		String outFileName = rank + "-runfile.txt";
		PrintWriter writer = new PrintWriter(outFileName, "UTF-8");
		
        for (Data.Page p : SearchUtils.createPageIterator(cborLoc)) {
			
			String keywordQuery = p.getPageName();
			String queryId = p.getPageId();
			TopDocs topDocs = query(keywordQuery, 100);
			
			for (ScoreDoc sd : topDocs.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				String paragraphId = doc.get("id");
				String out = queryId + " " + paragraphId + " " + 
							 rank  + " " + sd.score + " group2-?";		 
				writer.println(out);
			}
        }
		writer.close();
    }
       
}
