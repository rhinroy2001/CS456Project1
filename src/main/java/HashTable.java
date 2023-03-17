import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class HashTable {
    int slots;
    LinkedList[] hashTable;

    public HashTable(int slots){
        this.slots = slots;
        hashTable = new LinkedList[slots];
        for(int i = 0; i < slots; i++){
            hashTable[i] = new LinkedList<Node>();
        }
    }

    // adds a node to the hash table
    public void put(char value){
        Node character = new Node(value);
        int key = Math.abs(character.hashCode()%slots);
        boolean found = false;
        LinkedList<Node> bucket = hashTable[key];
        ListIterator<Node> bucketIterator = bucket.listIterator();
        while(bucketIterator.hasNext()){
            Node c = bucketIterator.next();
            if(character.equals(c)) {
                c.increaseOccurrences();
                found = true;
            }
        }
        if(!found){
            character.increaseOccurrences();
            hashTable[key].add(character);
        }
    }

    // finds a node in the hash table
    public Node find(char value){
        Node character = new Node(value);
        int key = Math.abs(character.hashCode()%slots);
        LinkedList<Node> bucket = hashTable[key];
        ListIterator<Node> bucketIterator = bucket.listIterator();
        while(bucketIterator.hasNext()){
            Node c = bucketIterator.next();
            if(character.equals(c)){
                return c;
            }
        }
        return null;
    }

}
