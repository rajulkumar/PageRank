:set jdk bin path of the system here
set path=%PATH%;C:\Program Files\Java\jdk1.8.0_05\bin
set CLASSPATH=%CLASSPATH%;./src/com/pagerank;./src

:set input file name
set fileName=src/com/pagerank/test_WT2g.txt

javac ./src/com/pagerank/PageRankOnPerplexity.java

java com/pagerank/PageRankOnPerplexity %fileName%
