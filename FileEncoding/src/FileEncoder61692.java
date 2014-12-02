import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Kancho Kanev
 * 
 */
public class FileEncoder61692 implements FileEncoder {

    public boolean isPrime(int number) {

        if (number == 1 || number == 2 || number == 3) {
            return true;
        }
        if (number % 2 == 0) {
            return false;
        }
        int sqrt = (int) Math.sqrt(number) + 1;
        for (int i = 3; i < sqrt; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        List<Character> keyArrayList = new ArrayList<Character>(key);
        int codeOfChar;
        int counter = 0;

        try {
            BufferedInputStream inputFile = new BufferedInputStream(new FileInputStream(sourceFile));
            BufferedOutputStream outputFile = new BufferedOutputStream(new FileOutputStream(destinationFile));

            while ((codeOfChar = inputFile.read()) != -1) {

                if (!isPrime(counter)) {

                    outputFile.write(keyArrayList.get(codeOfChar));

                } else {

                    outputFile.write(codeOfChar);

                }
                counter++;
            }
            inputFile.close();
            outputFile.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void decode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        List<Character> keyArrayList = new ArrayList<Character>(key);
        int codeOfChar;
        int counter = 0;
        try {
            BufferedInputStream inputFile = new BufferedInputStream(new FileInputStream(sourceFile));
            BufferedOutputStream outputFile = new BufferedOutputStream(new FileOutputStream(destinationFile));

            while ((codeOfChar = inputFile.read()) != -1) {

                if (!isPrime(counter)) {

                    int symbol = keyArrayList.indexOf((char) codeOfChar);
                    outputFile.write(symbol);

                } else {

                    outputFile.write(codeOfChar);

                }
                counter++;
            }
            inputFile.close();
            outputFile.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
