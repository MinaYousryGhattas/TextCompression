package Huffman;


public class BitPacking
{

    public static int ByteToInt(byte[] bb)
    {
        int[] b = new int[4];
        b[0]=bb[0];
        b[1]=bb[1];
        b[2]=bb[2];
        b[3]=bb[3];
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;


    }
    public static byte[] IntToByte(int i)
    {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i);

        return result;
    }

    static int log(int x)
    {
        double y = (Math.log(x) / Math.log(2));
        return (int) y + 1;
    }

    public static byte getlmsb(int b, int pos)
    {
        return (byte) ((b >> pos) & 1);
    }

    public static boolean bitPackingWriter(byte[] b, int offset, int value, int length)
    {

        int len = log(value);
        int counter = 0;
        offset += length - len;

        while (len > 0)
        {
            byte bb = getlmsb(value, len - 1);
            int pos = offset + counter;
            bb = (byte) (bb << 7 - pos % 8);
            b[pos / 8] |= bb;
            counter++;
            len--;
        }
        return false;
    }


    public static int bitPackingReader(byte[] b, int offset, int length)
    {
        length--;
        int counter = 0;
        int[] intbyte = new int[4];

        int pos = 32;
        while (length >= 0)
        {
            int position = offset + length;
            int off = position % 8;
            byte bb = getlmsb(b[position / 8], 7 - off);
            intbyte[3 - (counter / 8)] |= bb << counter % 8;
            length--;
            counter++;
        }

        return intbyte[0] << 24 | intbyte[1] << 16 | intbyte[2] << 8 | intbyte[3];
    }
}
