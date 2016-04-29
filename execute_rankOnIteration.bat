:set jdk bin path of the system here
set path=%PATH%;C:\Program Files\Java\jdk1.8.0_05\bin
set CLASSPATH=%CLASSPATH%;./src/com/pagerank;./src

:set input file name
set fileName=src/com/pagerank/test_graph.txt

javac ./src/com/pagerank/PageRankOnIteration.java

java com/pagerank/PageRankOnIteration %fileName%
