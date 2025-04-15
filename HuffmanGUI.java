import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;
import java.io.*;

public class HuffmanGUI extends JFrame {
    private final HuffmanCompressor compressor = new HuffmanCompressor();

    public HuffmanGUI() {
        setTitle("Huffman Compressor");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton compressBtn = new JButton("Compress File");
        JButton decompressBtn = new JButton("Decompress File");

        compressBtn.addActionListener(e -> compressFile());
        decompressBtn.addActionListener(e -> decompressFile());

        setLayout(new GridLayout(2, 1, 10, 10));
        add(compressBtn);
        add(decompressBtn);
    }

    private void compressFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File inputFile = fc.getSelectedFile();
            File outputFile = new File(inputFile.getParent(), inputFile.getName() + ".huff");

            try {
                compressor.compress(inputFile, outputFile);
                JOptionPane.showMessageDialog(this, "File compressed:\n" + outputFile.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Compression failed: " + ex.getMessage());
            }
        }
    }

    private void decompressFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File inputFile = fc.getSelectedFile();
            String name = inputFile.getName().replace(".huff", "_decompressed");
            File outputFile = new File(inputFile.getParent(), name);

            try {
                compressor.decompress(inputFile, outputFile);
                JOptionPane.showMessageDialog(this, "File decompressed:\n" + outputFile.getAbsolutePath());
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Decompression failed: " + ex.getMessage());
            }
        }
    }
}
