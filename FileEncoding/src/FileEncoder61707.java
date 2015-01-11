import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/*
 * 
 * @author Plamen Markov
 *
 */

public class FileEncoder61707 implements FileEncoder {

    public boolean isPrime(int num) {
        if (num == 1)
            return true;

        if ((num & 1) == 0) {
            if (num == 2) {
                return true;
            } else {
                return false;
            }
        } else {

            for (int i = 3; (i * i) <= num; i += 2) {
                if ((num % i) == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        try (BufferedInputStream inputFile = new BufferedInputStream(new FileInputStream(sourceFile));
                BufferedOutputStream outputFile = new BufferedOutputStream(new FileOutputStream(destinationFile))) {
            int ch = 0;
            int counter = 0;
            while ((ch = inputFile.read()) != -1) {

                if (isPrime(counter)) {

                    outputFile.write(ch);
                } else {
                    outputFile.write(key.get(ch));
                }
                counter++;
            }

        } catch (FileNotFoundException fnfe) {
            System.err.println("Cannot open the file " + fnfe.getMessage());
        } catch (IOException ioe) {
            System.err.printf("Error when processing file; Exiting..");
        }

    }

    @Override
    public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) {

        try (BufferedInputStream inputFile = new BufferedInputStream(new FileInputStream(encodedFile));
                BufferedOutputStream outputFile = new BufferedOutputStream(new FileOutputStream(destinationFile))) {
            int ch = 0;
            int counter = 0;
            while ((ch = inputFile.read()) != -1) {

                if (isPrime(counter)) {

                    outputFile.write(ch);
                } else {
                    outputFile.write(key.indexOf((char) ch));
                }
                counter++;
            }

        } catch (FileNotFoundException fnfe) {
            System.err.println("Cannot open the file " + fnfe.getMessage());
        } catch (IOException ioe) {
            System.err.printf("Error when processing file; Exiting..");
        }
    }
}
