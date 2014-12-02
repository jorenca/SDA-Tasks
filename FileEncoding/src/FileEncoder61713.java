import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * 
 * @author Jivko Todorov
 * 
 */
public class FileEncoder61713 implements FileEncoder {
    boolean[] primes = new boolean[307200];

    public FileEncoder61713() {
        fillSieve();
    }

    public void fillSieve() {
        Arrays.fill(primes, true);
        primes[0] = false;
        primes[1] = true;
        for (int i = 2; i < primes.length; i++) {
            if (primes[i]) {
                for (int j = 2; i * j < primes.length; j++) {
                    primes[i * j] = false;
                }
            }
        }
    }

    public boolean isPrime(int n) {
        return primes[n];
    }

    @Override
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {

        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(sourceFile));
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destinationFile));

            int counter = 0;
            int currentByte;

            while ((currentByte = input.read()) != -1) {
                if (isPrime(counter)) {

                    output.write(currentByte);
                } else {
                    output.write(key.get(currentByte));
                }

                counter++;
            }
            input.close();
            output.close();

        } catch (Exception e) {
            System.out.println("An error occured during encoding the file !");
        }

    }

    @Override
    public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) {

        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(encodedFile));
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destinationFile));

            int counter = 0;
            int currentByte;

            while ((currentByte = input.read()) != -1) {
                if (isPrime(counter)) {
                    output.write(currentByte);
                } else {
                    output.write(key.indexOf((char) currentByte));
                }
                counter++;
            }
            input.close();
            output.close();
        } catch (Exception e) {
            System.out.println("An error occured during decoding the file !");
        }
    }

}
