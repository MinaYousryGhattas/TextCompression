package Huffman;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.sun.xml.internal.ws.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class Huffman
{
    public String input;
    public Vector<Node> chars;
    public Vector<String> code;
    public Vector<String> tag;
    public Vector<Character> chartocode;
    public Node parent;
    int length;
    String compressed;
    String output;

    public Huffman()
    {
        input="";
        compressed="";
        output="";
        tag = new Vector<String>();
        chars = new Vector<Node>();
        code = new Vector<String>();
        chartocode = new Vector<Character>();
        parent = new Node();
    }

    private void SortNodes(Vector<Node> e)
    {
        Collections.sort(e);
    }

    public void buidtree()
    {
        Vector<Node> tempchars = chars;
        SortNodes(tempchars);
        while (tempchars.size() > 2)
        {
            Node n = new Node();
            n.counter = tempchars.get(0).counter + tempchars.get(1).counter;
            n.left = tempchars.get(0);
            n.right = tempchars.get(1);
            Vector<Node> x = new Vector<Node>();
            x.addElement(n);
            for (int i = 2; i < tempchars.size(); i++)
                x.addElement(tempchars.get(i));
            tempchars = x;
            SortNodes(tempchars);
        }

        int c = 0;
        if (tempchars.size() > 0)
        {
            parent.left = tempchars.get(0);
            c = parent.left.counter;
        }
        if (tempchars.size() > 1)
        {
            parent.right = tempchars.get(1);
            c+= parent.right.counter;
        }
        parent.counter = c;
    }

    public void maketable(Node r, String s)
    {
        if (r == null)
            return;
        maketable(r.left, s + "0");
        maketable(r.right, s + "1");

        if (!s.equals("") && r.chars != 0 && !chartocode.contains(r.chars))
        {
            chartocode.addElement(r.chars);
            System.out.println("chars : " + r.chars + " " + s);
            code.addElement(s);
        }
    }

    public int ToInt(String s)
    {
        int x = 0;
        int xx = 1;
        for (int i = s.length() - 1; i >= 0; i--, xx *= 2)
            if (s.charAt(i) == '1')
                x += xx;
        return x;
    }

    public byte[] TableToByteArray()
    {
        byte[] b = new byte[4 * tag.size()];
        int offset = 0;
        int counter = 0;
        while (counter < tag.size())
        {
            int codesize = tag.get(counter).length();
            int value = ToInt(tag.get(counter));
            BitPacking.bitPackingWriter(b, offset, value, codesize);
            offset += codesize;
            counter++;
        }
        return Arrays.copyOfRange(b, 0, (int) Math.ceil((double) offset / 8));
    }

    public void maketags()
    {
        for (int i = 0; i < input.length(); i++)
        {
            int index = chartocode.indexOf(input.charAt(i));
            tag.addElement(code.get(index));
        }
    }

    public int countChar(String s, char c)
    {
        int counter = 0;
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == c)
                counter++;
        return counter;
    }

    public void makenode()
    {
        Vector<Character> exist = new Vector<Character>();
        for (int i = 0; i < input.length(); i++)
        {
            if (!exist.contains(input.charAt(i)))
            {
                Node n = new Node();
                n.chars = input.charAt(i);
                n.counter = countChar(input, n.chars);
                chars.addElement(n);
                exist.addElement(n.chars);
            }
        }
    }

    public void tagstofiles(FileOutputStream f) throws IOException
    {
        f.write(BitPacking.IntToByte(input.length()));
        f.write(TableToByteArray());
    }

    public void filetostring(FileInputStream f) throws IOException
    {
        byte[] len = new byte[4];
        f.read(len);
        length = BitPacking.ByteToInt(len);
        Vector<Byte> v = new Vector<Byte>();
        int i = 0;
        while ((i = f.read()) != -1)
        {
            v.addElement((byte) i);
        }
        compressed = "";
        for (int iv = 0; iv < v.size(); iv++)
        {
            byte b = v.elementAt(iv);
            compressed += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        }
    }


    public void compressedtooutput()
    {
        String s = "";
        int counter = 0;
        for (int i = 0;i<compressed.length();i++)
        {
            s+=compressed.charAt(i);
            if (code.contains(s))
            {
                counter++;
                int index = code.indexOf(s);
                output+=chartocode.get(index);
                s = "";
            }
            if (counter == length)
                break;
        }
    }


    public void writeHeader(FileOutputStream f) throws IOException
    {
        for (int i = 0;i<chars.size();i++)
        {
            f.write(chars.elementAt(i).chars);
            f.write(BitPacking.IntToByte(chars.elementAt(i).counter));
        }
    }
    public void ReadHeader(FileInputStream f) throws IOException
    {
        int x = f.read();
        Vector<Node> tp = new Vector<Node>();
        byte[] b = new byte[4];
        f.read(b);
        while (x != -1)
        {
            Node n = new Node();
            n.chars = (char)x;
            n.counter = BitPacking.ByteToInt(b);
            tp.addElement(n);
            x = f.read();
            b = new byte[4];
            f.read(b);
        }
        chars = tp;
    }

    public void Compress(FileInputStream data) throws IOException
    {
        ReadData(data);
        Compress();
    }

    public void Compress() throws IOException
    {
        FileOutputStream data = new FileOutputStream("compressed.txt");
        FileOutputStream h = new FileOutputStream("Header.txt");
        makenode();
        writeHeader(h);
        buidtree();
        maketable(parent,"");
        maketags();
        tagstofiles(data);
    }


    public void Decompress(FileOutputStream data) throws IOException
    {
        Decompress();
        WriteData(data);
    }
    public void Decompress() throws IOException
    {
        FileInputStream data = new FileInputStream("compressed.txt");
        FileInputStream h = new FileInputStream("Header.txt");
        ReadHeader(h);
        buidtree();
        maketable(parent,"");
        filetostring(data);
        compressedtooutput();
    }


    public void ReadData(FileInputStream fin) throws IOException
    {
        int x;
        input = "";
        while((x = fin.read()) != -1)
            input+=(char)x;
    }
    public void WriteData(FileOutputStream fout) throws IOException
    {
        for (int i = 0;i<output.length();i++)
            fout.write(output.charAt(i));
    }

    public void CheckCompression()
    {
        if (input.equals(output))
            System.out.println("compression is done");
        else
            System.out.println("compression is failed");
    }


}
