#Compiles and executes WordCount
export BATCHD_REPO=~/gitviews/batchadoop
cd $BATCHD_REPO/application/WordCount
mvn package 
hadoop jar $BATCHD_REPO/application/WordCount/target/WordCount-1.0-SNAPSHOT.jar com.diplomadobd.wordcount.WordCount /user/$USER/wordcount/input /user/$USER/wordcount/output
cd ~
