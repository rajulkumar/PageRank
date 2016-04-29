#Set the jdk bin path of the system at <...> 
export PATH=$PATH:<Path of jdk bin on the system>
export CLASSPATH=$CLASSPATH:./src/com/pagerank:./src:.

#Set the file name here
fileName=src/com/pagerank/test_WT2g.txt

javac ./src/com/pagerank/PageRankOnPerplexity.java

java com/pagerank/PageRankOnPerplexity $fileName
