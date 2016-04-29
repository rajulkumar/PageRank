PageRank
---------------
This is an implementation of a PageRank alogrithm to find the PageRank of the pages in the in-link file given as input.
There are two varients of the implementation:
- Ranking is done for all the pages till 100 iterations (rank on iteration)
- Ranking is done till the difference of last 4 subsequent perpelxities are less than 1 (rank on perplexity)

Build
-----
Developed in Java using jdk1.8.0_05.
Compiled for jre8.
No third party/external libraries used.

Compile and Run
----------------
For ranking till 100 iterations, use execute_rankOnIteration.bat or execute_rankOnIteration.sh
The response files generated are rank_after_1.txt, rank_after_10.txt and rank_after_100.txt

For ranking on perplexity convergence, use execute_rankOnPerplexity.bat or execute_rankOnPerplexity.sh
The response files generated are perplexity.txt, pagerank.txt, inlink.txt and proportions.txt.

For windows:
1. Open execute_rankOn...bat
2. Set the path of jdk bin on this system at the path variable.
3. Set the file path of the input file for fileName variable.
4. Save and execute .bat
5. Response files are generated in output folder here.

For linux/unix*:
1. Open execute_rankOn...sh
2. Set the path of jdk bin on this system at the path variable.
3. Set the file path of the input file for fileName variable.
4. Save and execute .sh
5. Response files are generated in output directory here.

* not tested for linux/unix 

Alternatively,
The java files PageRankOnPerplexity.java and PageRankOnIteration.java could be compiled from here by:
javac src/com/pagerank/<file name>.java
e.g. javac src/com/pagerank/PageRankOnPerplexity.java

This should be executed from this location by:
java com.pagerank.<file name> <location of the input file> 
e.g. java com.pagerank.PageRankOnPerplexity ./src/com/pagerank/test_WT2g.txt

Results/response of execution
-----------------------------
1. Response files for 1,10 and 100 iterations containing the PageRanks of the given nodes on execution of 'rank on iteration' version are in the files below:
- PageRanks of all nodes on 1st iteration: ./output/rank_after_1.txt
- PageRanks of all nodes after 10 interation:  ./output/rank_after_10.txt
- PageRanks of all nodes after 100 interation:  ./output/rank_after_100.txt

2. Response files on the execution of 'ranks on perplexity convergence' for the PageRanks of all the nodes in the given input file are as below:
- list of the perplexity values obtained in each round until convergence: ./output/perplexity.txt

3. Response files after sorting the collection obtained from running 'ranks on perplexity convergence' version of the imolementation are as below:
- list of the top 50 pages as sorted by PageRank having docIDs and pagerank: ./output/pagerank.txt
- list of the top 50 pages by in-link count having docIDs and in-link count: ./output/inlink.txt
- proportion of pages with no in-links, proportion of pages with no out-links and  proportion of pages whose PageRank is less than their initial, uniform values: ./output/proportions.txt

4. File for analysis of the PageRank results for the top 10 pages by PageRank and by in-link count is at the path below: 
./analysis of Pagerank.pdf

References
-----------
1.JavaSE Documentation: https://docs.oracle.com/javase/8/docs/
2. Stackoverflow forum: http://stackoverflow.com
