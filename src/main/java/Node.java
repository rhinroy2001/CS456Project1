import java.util.Objects;

public class Node {
    char value;
    int occurrences = 0;
    Node left;
    Node right;

    public Node(char character){
        this.value = character;
    }
    public Node(){

    }

    public char getValue(){
        return value;
    }

    public int getOccurrences(){
        return occurrences;
    }

    public void increaseOccurrences(){
        occurrences++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node char1 = (Node) o;
        return Objects.equals(value, char1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
