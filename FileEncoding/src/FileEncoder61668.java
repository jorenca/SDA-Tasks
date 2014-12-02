import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * 
 * @author Borislav Mitkov
 * 
 */
public class FileEncoder61668 implements FileEncoder {

    @Override
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        fillPrimes(); // Задава простите числа, ако преди това не са
                      // зададени.
        File infile = new File(sourceFile);
        File outfile = new File(destinationFile);

        if (!infile.exists()) // Създава файла, ако не съществува.
        {
            try {
                outfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileInputStream fip = new FileInputStream(infile); FileOutputStream fop = new FileOutputStream(outfile)) {
            for (int i = 0; i < infile.length(); i++) // Кодира
            {
                char b = (char) fip.read();
                if (primes[i])
                    fop.write(b);
                else
                    fop.write(key.get(b));
            }
            fip.close();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("EncodingDone");
    }

    @Override
    public void decode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        fillPrimes(); // Задава простите числа, ако преди това са зададени.
        File efile = new File(sourceFile);
        File dfile = new File(destinationFile);

        if (!dfile.exists()) // създава файла ако не съществува
        {
            try {
                dfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileInputStream fip = new FileInputStream(efile); FileOutputStream fop = new FileOutputStream(dfile)) {
            for (int i = 0; i < efile.length(); i++) // Дедира
            {
                char b = (char) fip.read();
                if (primes[i])
                    fop.write(b);
                else
                    fop.write(key.indexOf(b));
            }
            fip.close();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("DecodingDone");
    }

    static boolean primes[] = new boolean[1000000]; // Масив, който приема стойност true
                                                    // ако индекса е просто число, и false в
                                                    // противен случай

    // Създаваме простите
    public static void fillPrimes() {
        if (primes[1] == false) // Проверява дали масива е подреден, ако не е
                                // изпълнява функцията,
        { // ако е (т.е. 1-цата вече има стойност че е просто), не
          // изпълнява
            Arrays.fill(primes, true); // Приемаме всички числа да са прости
            primes[0] = false; // 0 не е просто

            for (int i = 2; i < primes.length; i++) {
                // Ако числото е просто,
                // то преглеждаме всички негови кратни и им дава стойност
                // false.
                if (primes[i]) {
                    for (int j = 2; i * j < primes.length; j++) {
                        primes[i * j] = false;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
    }
}
