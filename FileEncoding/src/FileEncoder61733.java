import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author Martin Hristov
 * 
 */
public class FileEncoder61733 implements FileEncoder {

    @Override
    public void encode(String encodedFile, String outFile, LinkedList<Character> key) {
        FileInputStream input = null;
        FileOutputStream output = null;

        try {
            input = new FileInputStream(encodedFile);
            output = new FileOutputStream(outFile);

            int currentChar = 0;
            long index = 0;
            while ((currentChar = input.read()) != -1) {
                if (isPrime(index) || index == 1) {
                    output.write(currentChar);
                } else {
                    output.write(key.get(currentChar));
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void decode(String encodedFile, String outFile, LinkedList<Character> key) {
        FileInputStream input = null;
        FileOutputStream output = null;

        try {
            input = new FileInputStream(encodedFile);
            output = new FileOutputStream(outFile);

            int currentChar;
            long index = 0;
            while ((currentChar = input.read()) != -1) {

                if (isPrime(index) || index == 1) {
                    output.write(currentChar);
                } else {
                    output.write(key.indexOf((char) currentChar));
                }

                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isPrime(long num) {
        if (num == 2)
            return true;
        int to = (int) Math.sqrt(num) + 1;
        boolean prim = true;
        for (int i = 2; i <= to; i++) {
            if (num % i == 0) {
                prim = false;
                break;
            }
        }
        return num > 1 && prim;
    }

}