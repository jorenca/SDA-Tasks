import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;

/**
 * 
 * @author B.Dinchev
 */
public class FileEncoder61682 implements FileEncoder {

    public boolean isPrime(int num) {
        if (num == 1 || num == 2 || num == 3) {
            return true;
        }
        if (num % 2 == 0 || num % 3 == 0) {
            return false;
        } else {
            for (int i = 5; i * i <= num; i += 6) {
                if (num % i == 0 || num % (i + 2) == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void encode(String inFile, String outFile, LinkedList<Character> key) {
        try {
            FileInputStream inStream = new FileInputStream(inFile);
            FileOutputStream outStream = new FileOutputStream(outFile);

            int inByte;
            int count = 0;
            while ((inByte = inStream.read()) != -1) {

                if (isPrime(count)) {
                    outStream.write(inByte);
                } else {
                    outStream.write(key.get(inByte));
                }
                count++;
            }

            inStream.close();
            outStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void decode(String encodedFile, String outFile, LinkedList<Character> key) {
        try {
            FileInputStream inStream = new FileInputStream(encodedFile);
            FileOutputStream outStream = new FileOutputStream(outFile);

            int inByte;
            int count = 0;
            while ((inByte = inStream.read()) != -1) {

                if (isPrime(count)) {
                    outStream.write(inByte);
                } else {
                    outStream.write(key.indexOf((char) inByte));
                }
                count++;
            }

            inStream.close();
            outStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
