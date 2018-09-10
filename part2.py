#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
#  part2.py
#  
#  

import pytrec_eval
import json



def display_scoring_fnc_avgs(scoring_fnc, run_file_loc): 
    """ Retrieve and display the results (measure averages) for each scoring function """   
    qrel_file_loc = "cbor_files/train.pages.cbor-article.qrels"
    with open(qrel_file_loc, 'r') as f_qrel:
        qrel = pytrec_eval.parse_qrel(f_qrel)
    with open(run_file_loc, 'r') as f_run:
        run = pytrec_eval.parse_run(f_run)
    evaluator = pytrec_eval.RelevanceEvaluator(
    qrel, {'Rprec', 'map', 'ndcg_cut'})
    rPrec_cnt = 0
    rPrec_total = 0
    map_cnt = 0
    map_total = 0
    ndcg_20_cnt = 0
    ndcg_20_total = 0
    for query_id, query_measures in sorted(evaluator.evaluate(run).items()):
        for measure, value in sorted(query_measures.items()):    
            if(measure[:4] == 'ndcg' and measure[9:] != "20"):
                continue
            if(measure == 'Rprec'):
                rPrec_cnt += 1
                rPrec_total += value	
            elif(measure == 'map'):
                map_cnt += 1
                map_total += value 
            elif(measure[:4] == 'ndcg'):
                ndcg_20_cnt += 1
                ndcg_20_total += value
    display = "Result: " + scoring_fnc + "\n" 
    display += "\tRPrec: " + str(rPrec_total/rPrec_cnt) + "\n"
    display += "\tmap: " + str(map_total/map_cnt) + "\n"
    display += "\tndcg@20: " + str(ndcg_20_cnt/ndcg_20_total)
    print(display)


def display_document_results(scoring_fnc, run_file_loc): 
    """ Retrieve and display the results for each document (paragraph) """   
    qrel_file_loc = "cbor_files/train.pages.cbor-article.qrels"
    with open(qrel_file_loc, 'r') as f_qrel:
        qrel = pytrec_eval.parse_qrel(f_qrel)
    with open(run_file_loc, 'r') as f_run:
        run = pytrec_eval.parse_run(f_run)
    evaluator = pytrec_eval.RelevanceEvaluator(
    qrel, {'Rprec', 'map', 'ndcg_cut'})
    for query_id, query_measures in sorted(evaluator.evaluate(run).items()):
        display = "Result: " + query_id + ", scoring function: " + scoring_fnc + "\n"
        for measure, value in sorted(query_measures.items()):    
            if(measure[:4] == 'ndcg' and measure[9:] != "20"):
                continue
            if(measure == 'Rprec'):
                display += "\tRprec: " + str(value) + "\n"	
            elif(measure == 'map'):
                display += "\tmap: " + str(value) + "\n"
            elif(measure[:4] == 'ndcg'):
                display += "\tndcg@20: " + str(value)
        print(display)


def main(args):
    """ Get the averages for each scoring function """
    '''
    try:
        display_document_results("default", "run_files/default-runfile.txt")
        display_document_results("custom", "run_files/custom-runfile.txt")
    except:
        return 1
    print("----")
    '''
    try:
        display_scoring_fnc_avgs("default", "run_files/default-runfile.txt")
        display_scoring_fnc_avgs("custom", "run_files/custom-runfile.txt")
    except:
        return 1
        
    print("======================================")
    return 0


if __name__ == '__main__':
    import sys
    sys.exit(main(sys.argv))
