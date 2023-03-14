import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String fileToEncode = "src/main/resources/LittleWomen.txt";
        String encodedFile = "src/main/resources/EncodedFile.txt";
        String decodedFile = "src/main/resources/DecodedFile.txt";
        Node root = huffmanEncode(countOccurrences(fileToEncode));
        Map<Character, String> map = new HashMap<>();
        storeCodes(root, "", map);
/*        for (Map.Entry<String, Character> mapElement : map.entrySet()) {
            String key = mapElement.getKey();
            int value = mapElement.getValue();
            System.out.println(key + ":" + value);
        }*/
        compress(fileToEncode, encodedFile, map);
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
                if(!characterList.contains(word.charAt(i))) {
                    characterList.add(word.charAt(i)); // put each unique character in the character list
                }
            }
        }
        Node[] arr = new Node[characterList.size()];
        for(int i = 0; i < arr.length; i++) {
            arr[i] = ht.find(characterList.get(i)); // add nodes to the node array
        }
        fileReader.close();
        return arr;
    }

    public static void compress(String inputFile, String outputFile, Map<Character, String> map) throws IOException{
        File file = new File(inputFile);
        Scanner fileReader = new Scanner(file);
        FileWriter writer = new FileWriter(outputFile);
        while(fileReader.hasNext()){
            String word = fileReader.next();
            for(int i = 0; i < word.length(); i++){
                writer.write(map.get(word.charAt(i)));
            }
        }
        fileReader.close();
        writer.close();
    }

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
            System.out.println(node.value + ":" + node.occurrences); // delete this
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
        return root;


    }

    public static void storeCodes(Node node, String s, Map<Character, String> map){
        if(isLeaf(node)){
            map.put(node.value, s);
            System.out.println(node.value + ":" + s); // delete this
            return;
        }
        storeCodes(node.left, s + "0", map);
        storeCodes(node.right, s + "1", map);
    }

    public static boolean isLeaf(Node node){
        if(node.left == null && node.right == null){
            return true;
        }
        return false;
    }
}
