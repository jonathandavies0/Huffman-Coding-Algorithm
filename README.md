# Huffman-Coding-Algorithm

AUTHORS: Jonathan Davies


Program Description:
        Implements the Huffman Coding Algorithm to compress data.
        
How to use the program:

        -COMPILATION: javac -d bin src/huffman/*.java
        
        -EXECUTION: java -cp bin huffman.Driver
        
  ![image](https://user-images.githubusercontent.com/95953481/161443104-7e6c57e4-bfc5-40e7-a6b3-f24937beeaf6.png)


Arugments: 
       
       The program takes a text file and method calls by using 
       correponding numbers 
 ![image](https://user-images.githubusercontent.com/95953481/161443290-4a57d225-7c2d-41df-8aeb-2f5c3e2946ce.png)
 

Program structure: 
        
        After the user enters a textfile containing a string 
        of characters, a menu opens.  
                 1. makeSortedList: orders the chars by frequency
                 2. makeTree: creates Huffman Tree
                 3. makeEncodings: writes down key value pairs
                 4. encode: encodes string 
                 5. decode: decodes encoded text and displays message
