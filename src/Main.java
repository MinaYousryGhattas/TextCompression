import Huffman.*;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.*;

public class Main
{
    public static void main(String[] args) throws IOException
    {
/*        //ABAABABBAABAABAAAABABBBBBBBB
        FileOutputStream output = new FileOutputStream("output.txt");
        FileInputStream input = new FileInputStream("input.txt");
        LZW lz = new LZW();
        lz.CheckCompress(input,output);*/
        JFrame frame = new JFrame("Huffman");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(new GUI());
        frame.pack();
        frame.setVisible(true);

    }
}