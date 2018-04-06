package Huffman;

import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GUI extends JPanel implements ActionListener
{
    private JButton CompressAndDecompress;
    private JLabel SourceFile;
    private JLabel SourceSize;
    private JLabel CompressedSize;
    private JLabel CompressionRate;
    private JLabel HeaderSize;
    private JButton buttonsourcepath;
    private JFileChooser SourcePath;
    private File f;

    private JTextField textinput;
    private JButton textsubmit;


    public GUI()
    {
        f = new File("input.txt");
        CompressAndDecompress = new JButton("Compress");
        SourceFile = new JLabel("Source path");
        buttonsourcepath = new JButton("Choose file");
        SourcePath = new JFileChooser();
        SourceSize = new JLabel("Source size = 0.0");
        CompressedSize = new JLabel("");
        CompressionRate = new JLabel("");
        textinput = new JTextField("Input ur phrase");
        textsubmit = new JButton("Save ur text");
        HeaderSize = new JLabel("");


        setPreferredSize(new Dimension(500, 180));
        setLayout(null);

        add(CompressAndDecompress);
        add(SourceFile);
        add(buttonsourcepath);
        add(SourceSize);
        add(CompressedSize);
        add(CompressionRate);
        add(textinput);
        add(textsubmit);
        add(HeaderSize);


        SourceFile.setBounds(10, 10, 100, 25);
        buttonsourcepath.setBounds(120, 10, 110, 25);
        CompressAndDecompress.setBounds(120, 85, 110, 25);
        SourceSize.setBounds(170, 115, 150, 25);
        CompressedSize.setBounds(170, 145, 150, 25);
        CompressionRate.setBounds(330, 145, 150, 25);
        HeaderSize.setBounds(10, 145, 150, 25);
        textinput.setBounds(10, 45, 100, 25);
        textsubmit.setBounds(120, 45, 110, 25);

        SourceSize.setVisible(false);
        CompressedSize.setVisible(false);
        CompressionRate.setVisible(false);
        HeaderSize.setVisible(false);


        buttonsourcepath.addActionListener(this);
        CompressAndDecompress.addActionListener(this);
        textsubmit.addActionListener(this);
    }


    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == buttonsourcepath)
        {
            int x = SourcePath.showOpenDialog(null);
            if (x == JFileChooser.APPROVE_OPTION)
            {
                f = SourcePath.getSelectedFile();
                SourceSize.setText("Source size = " + f.length());
                SourceSize.setVisible(true);
            }
        } else if (e.getSource() == CompressAndDecompress)
        {
            FileOutputStream output;
            FileInputStream input;
            try
            {

                output = new FileOutputStream("output.txt");
                input = new FileInputStream(f);
                Huffman h = new Huffman();
                h.Compress(input);
                h.Decompress(output);
                h.CheckCompression();
                File out = new File("compressed.txt");
                File Header = new File("Header.txt");
                SourceSize.setText("Source size = " + f.length());
                CompressedSize.setText("Compressed size = " + out.length());
                CompressionRate.setText("Compression rate = " + f.length() / (double) out.length());
                HeaderSize.setText("Header size = "+ Header.length());
                SourceSize.setVisible(true);
                HeaderSize.setVisible(true);
                CompressedSize.setVisible(true);
                CompressionRate.setVisible(true);

            } catch (IOException ioe)
            {
                final JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "There is Error in Opening File : " + f.getName(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if (e.getSource() == textsubmit)
        {
            String phrase = textinput.getText();
            if (phrase.length() == 0)
            {
                final JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "Phrase can't be empty", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try
            {
                FileOutputStream to_write_phrase = new FileOutputStream(f);
                for (int i = 0; i < phrase.length(); i++)
                    to_write_phrase.write(phrase.charAt(i));
                SourceSize.setText("Source size = " + f.length());
                SourceSize.setVisible(true);
            } catch (IOException ioe)
            {
                final JPanel panel = new JPanel();
                JOptionPane.showMessageDialog(panel, "Error in writing phrase", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (e.getSource() == textsubmit || e.getSource() == buttonsourcepath)
        {
            CompressionRate.setVisible(false);
            CompressedSize.setVisible(false);
            HeaderSize.setVisible(false);
        }

    }
}