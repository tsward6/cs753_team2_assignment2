#!/bin/bash
#
# CS873 Programming Assignment2 main script Team2
#
# This script runs the necessary steps for parts 1 and 2 of programming 
# assignment 2. 
# 
# 1) maven compile
# 2) run the jar file maven created
# 3) run the python script that uses pytrec_eval to get results for part 2
#
#


# remove program created directories (if they exist)
rm -rf ./run_files
rm -rf ./paragraphs

# create a run_files directory
mkdir ./run_files

# maven compile command
mvn clean compile assembly:single
if [ $? -eq 1 ]; then
	echo "Maven compile failed"
	exit 1
fi

# run the jar file maven created
java -jar target/team2_1-1.0-SNAPSHOT-jar-with-dependencies.jar
if [ $? -eq 1 ]; then
	echo "Error accessing maven SNAPSHOT jar file"
	exit 1
fi

# run the python script that uses pytrec_eval to get results for part 2
python3 part2.py
if [ $? -eq 1 ]; then
	echo "Error using pytrec_eval script (for part2)"
	exit 1
fi
