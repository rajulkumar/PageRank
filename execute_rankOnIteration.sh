#Set the jdk bin path of the system at <...> 
export PATH=$PATH:<Path of jdk bin on the system>
export CLASSPATH=$CLASSPATH:./src/com/pagerank:./src:.

#Set the file name here
fileName=src/com/pagerank/test_graph.txt

javac ./src/com/pagerank/PageRankOnIteration.java

java com/pagerank/PageRankOnIteration $fileName
