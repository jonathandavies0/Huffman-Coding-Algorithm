package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 * @author Jonathan Davies
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }
    //checks characters from makesortedlist
    private void doesExist(ArrayList<CharFreq> x){
        for(int i = 0; i < x.size(); i++){
        CharFreq b = x.get(i);
            System.out.println(b.getCharacter());
        }
    }
    // Create the Huffman Coding Algorithm
    // compare and dequeue/enqueue nodes
    private void compareProb(double t, double s, TreeNode small, TreeNode smaller, Queue<TreeNode> tar, Queue<TreeNode> sour){
        // case 1: source has smallest #1 and source has smallest #2 
        System.out.println(t + ": prob of target node, " + s + ": prob of source node");
        if(s <= t){
            smaller = sour.dequeue();
            System.out.println("source is smaller #1");
            if(s <= t){
                System.out.println("source is smaller #2");
                small = sour.dequeue();
            }
            //case 2: source has smallest #1 and target smallest #2
            //already have the smallest set from above 
            if(s > t){
                System.out.println("target is smaller #2");
                small = tar.dequeue();
            }
        }
        //case 3: target has smallest #1 and target smallest #2
        if(s > t){
            System.out.println("target is smaller #1");
            smaller = tar.dequeue();   
            if(s > t){
                System.out.println("target is smaller #2");
                small = tar.dequeue();
            }
            //case 4: target smallest #1 and source smallest #2
            //already have the smallest set from above 
            if(s <= t){
                System.out.println("source is smaller #2");
                small = sour.dequeue();
            }
        }
    }
    //checks if the node given is a leafnode and returns true or false
    private boolean isLeafNode(TreeNode node){
        if(node.getLeft() == null && node.getRight() == null){
            return true;
        }
        else return false;
    }
    //Recursively create encodings from huffmanroot
    private void recurseEncoding(TreeNode root, String code){
        if(root != null){

            // if it is a root node then I store the encoding I have in the encodings array
            if(isLeafNode(root) == true){
                //add the bitstring to encoding array at the ascii value for that char
                encodings[(int)root.getData().getCharacter()] = code; //set the finished encoded bitstring for index (int)char
            }
            //add 0 to bitstring value "code"
            recurseEncoding(root.getLeft(), code+"0");

            //add 1 to bitstring value "code"
            recurseEncoding(root.getRight(), code+"1");
        }
    }
    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {

        StdIn.setFile(fileName);
        sortedCharFreqList = new ArrayList<>();
        int[] asciiFreq = new int[128];
        int length = 0;

        while(StdIn.hasNextChar() == true){
            Character x = StdIn.readChar();
            asciiFreq[(int)x]++; //increments the value at given index
            length++;
        }

        // need to create CharFreq objs to hold the data from finding frequency of chars
        for(int i = 0; i < asciiFreq.length; i++){
            
            // only add the characters present in the file
            if(asciiFreq[i] > 0){
                Character x = (char)i;
                CharFreq n = new CharFreq(x, (double)(asciiFreq[(int)x])/length);
                sortedCharFreqList.add(n);
            }
        }
        if(sortedCharFreqList.size() == 1){
            //character value from the file
            Character x = sortedCharFreqList.get(0).getCharacter();
            // if the character is last in the ascii table store the first ascii value
            if((int)x == 127){
                int nextChar = 0;
                Character y = (char)nextChar;
                CharFreq n = new CharFreq(y, 0.0);
                sortedCharFreqList.add(n);
            }
            else{
                int nextChar = (int)x + 1;
                Character y = (char)nextChar;
                CharFreq n = new CharFreq(y, 0.0);
                sortedCharFreqList.add(n);
            }
            
        }
        Collections.sort(sortedCharFreqList); // sort the list of CharFreqs
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */

// makeTree|Problems| I ran into issues with null pointer exceptions. 
    //Details|
        //I was making a reference variable to null treeNodes. 
        //This occurs because a null value does not have a place in memory so my reference variables 
        //were pointing to basicallly nothing. 
        //Which is impossible, because computers are white and black. 
            //Solution|
                //Thus to fix the issues I started checking if the queues were empty or not. 
                //I would intialize the reference variables to the treeNodes that actually existed.

//Questions?| Would the Huffman Coding Algorithm still function the same if the list I gave was from highest to greatest?
    public void makeTree() {

        //huffmanRoot = new TreeNode(); // initialize root node 
        Queue<TreeNode> source = new Queue<>();
        Queue<TreeNode> target = new Queue<>();

        // fill source with TreeNodes Nodes
        for(int i = 0; i < sortedCharFreqList.size(); i++){
            //convert CharFreqs to TreeNodes
            TreeNode n = new TreeNode();
            n.setData(sortedCharFreqList.get(i));
            source.enqueue(n);
        }

        // Huffman Coding Algorithm
        while(!source.isEmpty() || target.size() != 1){
            // Instantiate variables
            TreeNode sourceNode1;
            TreeNode targetNode1;
            double probSource1;
            double probTarget1;

            //create the nodes to hold the "smallest" and secound biggest "bigger"
            TreeNode smallest = new TreeNode();
            TreeNode bigger = new TreeNode();

            // if target queue is empty then dequeue source that has more than one node
            if(target.isEmpty()){
                smallest = source.dequeue(); // remove the smallest node from the source 
                bigger = source.dequeue(); // remove the second smallest node from source
            }
            // if source is empty and target has more than one node
            else if(source.isEmpty() && target.size() > 1){
                smallest = target.dequeue();
                bigger = target.dequeue();
            }
            // if source and target both have nodes  
            else if(!source.isEmpty() && !target.isEmpty()){
                // store probabilities of smallest nodes from source and target queues
                sourceNode1 = source.peek();
                targetNode1 = target.peek();
                probSource1 = sourceNode1.getData().getProbOcc();
                probTarget1 = targetNode1.getData().getProbOcc();

                // compare probability and dequeue respectfully
                if(probSource1 <= probTarget1){
                    smallest = source.dequeue();
                    if(source.isEmpty()){
                        probSource1 = 1.0;
                    }
                    else{
                        probSource1 = source.peek().getData().getProbOcc();
                    }
                    if(probSource1 <= probTarget1){
                        bigger = source.dequeue();
                    }
                    //case 2: source has smallest #1 and target smallest #2
                    //already have the smallest set from above 
                    if(probSource1 > probTarget1){
                        bigger = target.dequeue();
                    }
                }
                //case 3: target has smallest #1 and target smallest #2
                else if(probSource1 > probTarget1){
                    smallest = target.dequeue();   
                    if(target.isEmpty()){
                        probTarget1 = 1.0;
                    }
                    else{
                        probTarget1 = target.peek().getData().getProbOcc();
                    }
                    if(probSource1 > probTarget1){
                        bigger = target.dequeue();
                    }
                    //case 4: target smallest #1 and source smallest #2
                    //already have the smallest set from above 
                    if(probSource1 <= probTarget1){
                        bigger = source.dequeue();
                    }
                }

                //call compareProb to compare nodes and create the Huffman Coding Tree
                //compareProb(probTarget1, probSource1, bigger, smallest, target, source);
            }
            // create root nodes and initialize the values
                //set a new node to the sum of the two probs and enqueue in the target queue
                double prob = smallest.getData().getProbOcc() + bigger.getData().getProbOcc(); //sums the probability
                CharFreq n = new CharFreq(); //creates a CharFreq to store null and then store the probability after
                TreeNode sum = new TreeNode(); //node to hold the sum and no character
                // store probability in CharFreq n 
                n.setProbOcc(prob);
                // set the data for sum as CharFreq n 
                sum.setData(n);
                //initialize left and right nodes: smallest: left and second smallest: right
                sum.setLeft(smallest);
                sum.setRight(bigger);
                //enqueue to target queue
                target.enqueue(sum);
            
        }
        huffmanRoot = target.peek(); // store the final huffmanRoot Node
    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
        // create a reference variable to HuffmanRoot to increment through the tree to check for children nodes
        TreeNode HF = huffmanRoot;
        // Array of strings to hold the bitstring encodings for chracters
        String bit = "";
        // Initialize encodings array 
        encodings = new String[128];

        //calls isLeafNode: return true/false if node is a leafnode
        recurseEncoding(HF, bit);
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
        String bit = "";
        while(StdIn.hasNextChar()==true){
            bit += encodings[(int)StdIn.readChar()];
        }
        writeBitString(encodedFile, bit);
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);
        TreeNode HF = huffmanRoot;
        int counter = 0;
        String bitString = readBitString(encodedFile);
        //take the endcodedfile and use the huffmanroot to change bits to characters
        while(bitString.length() > counter){

            if(bitString.charAt(counter) == '0'){
                HF = HF.getLeft();
            }
            else if(bitString.charAt(counter) == '1'){
                HF = HF.getRight();
            }
            //reached the leafnode so I print the character there
            if(HF.getLeft() == null && HF.getRight() == null){
                StdOut.print(HF.getData().getCharacter()); //prints the character out to the decodedFile 
                HF = huffmanRoot; //reset the value of HF to the huffmanroot to continue printing
            }
            counter++;
        }
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
