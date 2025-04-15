import java.io.Serializable;

public class HuffmanNode implements Comparable<HuffmanNode>, Serializable {
    public int frequency;
    public byte data;
    public HuffmanNode left, right;

    public HuffmanNode(byte data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(HuffmanNode o) {
        return Integer.compare(this.frequency, o.frequency);
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}
