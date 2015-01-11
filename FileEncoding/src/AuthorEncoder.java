import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Georgi Gaydarov
 * 
 */
public class AuthorEncoder implements FileEncoder {

    private static final int SIEVE_SIZE = 308000;

    private static final boolean isPrimeInverted[] = new boolean[SIEVE_SIZE];

    static {
        isPrimeInverted[0] = true;
        // isPrimeInverted[1] = true;
        for (int i = 2; i * i < SIEVE_SIZE; i++) {
            if (!isPrimeInverted[i]) {
                for (int j = i; i * j < SIEVE_SIZE; j++) {
                    isPrimeInverted[i * j] = true;
                }
            }
        }
    }

    private static char[] listToArray(List<Character> keyList) {
        char[] arr = new char[keyList.size()];
        int i = 0;
        for (Character b : keyList) {
            arr[i++] = b;
        }
        return arr;
    }

    private static boolean isPrime(int x) {
        return !isPrimeInverted[x];
    }

    @Override
    public void encode(String inFile, String outFile, LinkedList<Character> key) {
        char[] k = listToArray(key);

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(inFile));
            out = new BufferedOutputStream(new FileOutputStream(outFile));

            int inBuff;
            int position = 0;
            while ((inBuff = in.read()) >= 0) {
                char input = (char) inBuff;
                char result = isPrime(position++) ? input : k[input];
                out.write(result);
            }

            in.close();
            out.flush();
            out.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void decode(String encodedFile, String outFile, LinkedList<Character> key) {
        char[] keyReversed = new char[key.size()];
        char i = 0;
        for (char b : key) {
            keyReversed[b] = i++;
        }

        LinkedList<Character> reversed = new LinkedList<Character>();
        for (char b : keyReversed) {
            reversed.add(b);
        }

        encode(encodedFile, outFile, reversed);
    }

}
