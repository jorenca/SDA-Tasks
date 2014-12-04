import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * @author Jivko Todorov 0.0113 za 1000 testa
 * 
 */
public class FileEncoder61713 implements FileEncoder {

    private final static int MAX_NUMBER = 307200;

    private static boolean[] primes = new boolean[MAX_NUMBER];

    public FileEncoder61713() {
        getPrimeNums();

    }

    private void getPrimeNums() {
        Arrays.fill(primes, true);
        primes[0] = false;
        primes[1] = true;
        int rootMP = (int) Math.floor(Math.sqrt(MAX_NUMBER));
        int halfMax = MAX_NUMBER / 2;

        for (int i = 3; i <= rootMP;) {
            for (int j = ((i * 3) / 2); j <= halfMax; j += i) {
                primes[j] = false;
            }
            i += 2;
            int k = i / 2;
            while (primes[k] == false) {
                k++;
            }
            i = (k * 2) + 1;
        }

        for (int i = 0; i <= halfMax; i++) {
            if (primes[i]) {
                primes[i] = isPrimeNumber((i * 2) + 1);
            }
        }
    }

    private boolean isPrimeNumber(int n) {
        if (n == 0) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    private static boolean isPrime(int n) {
        return primes[n];
    }

    @Override
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        ArrayList<Character> arrayKey = new ArrayList<>(key);

        try {
            BufferedInputStream inputFileStream = new BufferedInputStream(new FileInputStream(sourceFile));
            BufferedOutputStream outputFileStream = new BufferedOutputStream(new FileOutputStream(destinationFile));

            int counter = 0;
            int currentByte;

            while ((currentByte = inputFileStream.read()) != -1) {

                if (this.isPrime(counter)) {
                    outputFileStream.write(currentByte);
                } else {
                    outputFileStream.write(arrayKey.get(currentByte));
                }

                counter++;

            }

            inputFileStream.close();
            outputFileStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) {
        HashMap<Character, Integer> hashKey = new HashMap<>();
        for (int i = 0; i < key.size(); i++) {
            hashKey.put(key.get(i), i);
        }
        try {
            BufferedInputStream inputFileStream = new BufferedInputStream(new FileInputStream(encodedFile));
            BufferedOutputStream outputFileStream = new BufferedOutputStream(new FileOutputStream(destinationFile));

            int counter = 0;
            int currentByte;

            while ((currentByte = inputFileStream.read()) != -1) {
                if (this.isPrime(counter)) {
                    outputFileStream.write(currentByte);
                } else {
                    outputFileStream.write(hashKey.get((char) currentByte));
                }
                counter++;
            }
            inputFileStream.close();
            outputFileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
