import java.io.*;
import java.util.*;

public class HuffmanCompressor {

    private Map<Byte, String> huffmanCodes = new HashMap<>();
    private HuffmanNode root;

    public void compress(File inputFile, File outputFile) throws IOException {
        byte[] inputData = FileUtils.readFile(inputFile);
        Map<Byte, Integer> freqMap = buildFrequencyMap(inputData);
        root = buildHuffmanTree(freqMap);
        buildCodes(root, "");

        StringBuilder encodedStr = new StringBuilder();
        for (byte b : inputData) {
            encodedStr.append(huffmanCodes.get(b));
        }

        int bitLength = encodedStr.length();
        byte[] compressedBytes = getBytesFromBitString(encodedStr.toString());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            oos.writeObject(root);              // Write Huffman Tree
            oos.writeInt(bitLength);            // Number of encoded bits
            oos.writeInt(inputData.length);     // Original data length
            oos.writeInt(compressedBytes.length); // Compressed byte length
            oos.write(compressedBytes);         // Compressed bytes
        }
    }

    public void decompress(File inputFile, File outputFile) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile))) {
            root = (HuffmanNode) ois.readObject();
            int bitLength = ois.readInt();
            int originalLength = ois.readInt();
            int byteLength = ois.readInt();

            byte[] compressedBytes = new byte[byteLength];
            ois.readFully(compressedBytes);

            String bitString = getBitStringFromBytes(compressedBytes, bitLength);
            byte[] decompressed = decode(bitString, root, originalLength);
            FileUtils.writeFile(outputFile, decompressed);
        }
    }

    private Map<Byte, Integer> buildFrequencyMap(byte[] data) {
        Map<Byte, Integer> freqMap = new HashMap<>();
        for (byte b : data) {
            freqMap.put(b, freqMap.getOrDefault(b, 0) + 1);
        }
        return freqMap;
    }

    private HuffmanNode buildHuffmanTree(Map<Byte, Integer> freqMap) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
        for (Map.Entry<Byte, Integer> entry : freqMap.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode parent = new HuffmanNode((byte) 0, left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;
            pq.offer(parent);
        }

        return pq.poll();
    }

    private void buildCodes(HuffmanNode node, String code) {
        if (node.isLeaf()) {
            huffmanCodes.put(node.data, code);
            return;
        }
        buildCodes(node.left, code + "0");
        buildCodes(node.right, code + "1");
    }

    private byte[] getBytesFromBitString(String bitString) {
        int byteLength = (bitString.length() + 7) / 8;
        byte[] result = new byte[byteLength];

        for (int i = 0; i < bitString.length(); i++) {
            if (bitString.charAt(i) == '1') {
                result[i / 8] |= (1 << (7 - i % 8));
            }
        }
        return result;
    }

    private String getBitStringFromBytes(byte[] bytes, int bitLength) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            for (int i = 7; i >= 0; i--) {
                sb.append(((b >> i) & 1) == 1 ? '1' : '0');
            }
        }
        return sb.substring(0, bitLength); // trim excess bits
    }

    private byte[] decode(String bitString, HuffmanNode root, int originalLength) {
        List<Byte> output = new ArrayList<>();
        HuffmanNode current = root;

        for (int i = 0, count = 0; i < bitString.length() && count < originalLength; i++) {
            current = (bitString.charAt(i) == '0') ? current.left : current.right;

            if (current.isLeaf()) {
                output.add(current.data);
                current = root;
                count++;
            }
        }

        byte[] result = new byte[output.size()];
        for (int i = 0; i < output.size(); i++) {
            result[i] = output.get(i);
        }
        return result;
    }
}
