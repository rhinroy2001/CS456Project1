import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String littleWomen = "src/main/resources/LittleWomen.txt";
        String littleWomenEncodedFile = "src/main/resources/LittleWomenEncodedFile.txt";
        String littleWomenDecodedFile = "src/main/resources/LittleWomenDecodedFile.txt";
        String greatGatsby = "src/main/resources/GreatGatsby.txt";
        String greatGatsbyEncodedFile = "src/main/resources/GreatGatsbyEncodedFile.txt";
        String greatGatsbyDecodeFile = "src/main/resources/GreatGatsbyDecodedFile.txt";
        Node littleWomenRoot = huffmanEncode(countOccurrences(littleWomen));
        Node greatGatsbyRoot = huffmanEncode(countOccurrences(greatGatsby));
        Map<Character, String> littleWomenEncodeMap = new HashMap<>();
        Map<Character, String> greatGatsbyEncodeMap = new HashMap<>();
        storeCodes(littleWomenRoot, "", littleWomenEncodeMap);
        storeCodes(greatGatsbyRoot, "", greatGatsbyEncodeMap);
        compress(littleWomen, littleWomenEncodedFile, littleWomenEncodeMap);
        compress(greatGatsby,greatGatsbyEncodedFile, greatGatsbyEncodeMap);
        huffmanDecode(littleWomenRoot, littleWomenEncodedFile, littleWomenDecodedFile);
        huffmanDecode(greatGatsbyRoot, greatGatsbyEncodedFile, greatGatsbyDecodeFile);
        System.out.println("Little Women encoded contains: " + countBits(littleWomenEncodedFile) + " bits");
        System.out.println("Great Gatsby encoded contains: " + countBits(greatGatsbyEncodedFile) + " bits");
    }

    public static int countBits(String inputFile) throws IOException{
        int bits = 0;
        File file = new File(inputFile);
        Scanner reader = new Scanner(file);
        while(reader.hasNext()){
            String word = reader.next();
            bits += word.length();
        }
        return bits;
    }

    public static Node[] countOccurrences(String inputFile) throws IOException {
        // file IO
        File file = new File(inputFile);
        Scanner fileReader = new Scanner(file);
        ArrayList<Character> characterList = new ArrayList<>(); // keep track of each unique character in the text file
        HashTable ht = new HashTable(1000); // keep track of the number of times each character occurs
        while(fileReader.hasNext()){
            String word = fileReader.next();
            for(int i = 0; i < word.length(); i++){
                ht.put(word.charAt(i)); // put each character in the hash table
//                if(!characterList.contains(word.charAt(i))) {
//                    characterList.add(word.charAt(i)); // put each unique character in the character list
//                }
            }
        }
        Node[] arr = new Node[ht.size()];
        int i = 0;
        for(LinkedList<Node> bucket: ht.hashTable) {
            ListIterator<Node> bucketIterator = bucket.listIterator();
            while(bucketIterator.hasNext()){
                arr[i] = bucketIterator.next();
                i++;
            }
            // arr[i] = ht.find(characterList.get(i)); // add nodes to the node array
        }
        fileReader.close();
        return arr;
    }

    // compresses the file into strings of 1's and 0's
    public static void compress(String inputFile, String outputFile, Map<Character, String> map) throws IOException{
        File file = new File(inputFile);
        Scanner fileReader = new Scanner(file);
        FileWriter writer = new FileWriter(outputFile);
        while(fileReader.hasNext()){
            String word = fileReader.next();
            for(int i = 0; i < word.length(); i++){
                writer.write(map.get(word.charAt(i)));
            }
            writer.write(" ");
        }
        fileReader.close();
        writer.close();
    }

    // huffman encoding method to turn a character into bits
    public static Node huffmanEncode(Node[] nodes){
        PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.occurrences - o2.occurrences;
            }
        });
        // add each node to the priority queue
        for (Node node : nodes) {
            pq.add(node);
        }
        Node root = null; // instantiate the root node
        for(int i = 0; i < nodes.length - 1; i++){
            Node node3 = new Node(); // allocate new node
            Node node1 = pq.poll(); // extract min node
            Node node2 = pq.poll(); // extract min node
            // assign children
            node3.left = node1;
            node3.right = node2;
            node3.occurrences = node1.occurrences + node2.occurrences; // update occurrences of new node
            root = node3; // make the new node the root
            pq.add(node3); // add new node the priority queue
        }
        return root; // return the root of tree
    }

    // decodes a file that was compressed using huffman encoding
    public static void huffmanDecode(Node root, String fileIn, String fileOut) throws IOException{
        File encodedFile = new File(fileIn);
        File decodedFile = new File(fileOut);
        Scanner reader = new Scanner(encodedFile);
        FileWriter writer = new FileWriter(decodedFile);
        Map<String, Character> map = new HashMap<>();
        getCodes(root, "", map);
        while(reader.hasNext()){
            String word = reader.next();
            String code = "";
            for(int i = 0; i < word.length(); i++){
                code = code + word.charAt(i);
                if(map.containsKey(code)){
                    writer.write(map.get(code));
                    code = "";
                }
            }
            writer.write(" ");
        }
        reader.close();
        writer.close();
    }

    // creates a map with the character as the key and the huffman code as the value
    public static void storeCodes(Node node, String s, Map<Character, String> map){
        if(isLeaf(node)){
            map.put(node.value, s);
            return;
        }
        storeCodes(node.left, s + "0", map);
        storeCodes(node.right, s + "1", map);
    }

    // creates a map with the huffman code as the key and the character as the value
    public static void getCodes(Node node, String s, Map<String, Character> map){
        if(isLeaf(node)){
            map.put(s, node.value);
            return;
        }
        getCodes(node.left, s + "0", map);
        getCodes(node.right, s + "1", map);
    }

    // checks if a node is a leaf node
    public static boolean isLeaf(Node node){
        if(node.left == null && node.right == null){
            return true;
        }
        return false;
    }
}
