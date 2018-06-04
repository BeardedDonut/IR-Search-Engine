# IR-Search-Engine
Minimal search engine application for information retrieval course 

# Description
This is a minimal search engine application project.
for more details please check the documents in the `project documents` directory.

# Technologies used
   - Java 1.8
   - Lucene 6.6
   - maven 3.3.9
   
   
# Detailed Description

### Main components:
 - Searcher: searcher component main task is to search through indexes provided the path to the index files and the path to the query file then it prepares a list of query results.
 
 - Indexer: indexer component main task is to index the documents in the given path and write the results in the given directory.
 
 - Decomposer: this component takes the path to the corpus file and decompose it in to separated text files so it would be much easier to index and retrieve and save the decompostion result in the given path.
 
 - NewTFIDF: this is a new tf-idf similarity scoring strategy which computes the tf-idf measurement for a term. 
 
### How to define a new TF-IDF scoring strategy:
 In order to define a new td-idf similarity scoring strategy I've created a base class called `BaseTFIDFScoringStrategy` which extends from the `lucene.search.similarities.ClassicSimilarity` and declares the `tf` and `idf` methods as abstracts, then I extended my own desired algorithms from `BaseTFIDFScoringStrategy` and override the `tf` and `idf` methods as desired.
 
### How to use:
   1. navigate to the directory where `pom.xml` is located
   2. run the command `mvn package`
   3. navigate to the `target` directory
   4. run the `IR-Search-Engine-1.0-SNAPSHOT-jar-with-dependencies.jar` file using `java -jar IR-Search-Engine-1.0-SNAPSHOT-jar-with-dependencies.jar` command
   5. have fun!
   
 

## Note: 
   - You have to have `maven` installed if you want to generate jar file following the instructions in `how to use` section.
   - This project is `as is` so there would be no support in future.
 
    
### Author : 
 **Navid Alipour** - *Simple Search Engine* - [Navid Alipour](https://github.com/navid9675)