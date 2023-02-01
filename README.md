# File Type Analyzer
The Analyzer project is a Java application that analyzes a file or a directory structure to determine 
the type of files contained within it. The project uses the KMP (Knuth-Morris-Pratt) algorithm to match the contents
of a file against a set of predefined file types to determine the type of file it is.

---
## How to Use the Project
The Analyzer project is a command-line application that takes two arguments:
1. The path to the file or directory to be analyzed
2. The path to a file containing the list of file types to be recognized
	
The file containing the list of file types should contain one file type per line, 
with each line consisting of the following values separated by semicolons:
1. A unique identifier for the file type
2. The name of the file type
3. A pattern (regular expression) to match against the contents of a file

---      
## Main Classes
- Analyzer : The main class of the project. It takes the arguments passed to the application, reads the list of file
	types from the specified file, and starts the analysis process.
- KMP: A concrete implementation of the Strategy interface that uses the KMP algorithm to match the contents of a 
	file against the file type patterns.
- FileType: A data class that holds information about a file type.
- Strategy: An interface that defines the strategy to be used to match the contents of a file against a set of file 
	type patterns.
	
---  
## Technical Details
The Analyzer project uses the Java Executor framework to allow for concurrent processing of multiple files. It also
uses the Java NIO library to read the contents of a file into memory. The project requires a Java 11 or later runtime environment.

---		
### Author
*Junior Javier Brito Perez*
