import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class FileEncoder61673 implements FileEncoder {
    private ArrayList<Boolean> eraList = new ArrayList<Boolean>();

    FileEncoder61673() {
        for (int i = 0; i < 300000; i++) {
            eraList.add(true);
        }

        eraList.set(0, false);
        eraList.set(1, true);

        for (int i = 2; i < Math.sqrt(300000); i++) {

            if (eraList.get(i) == true) {
                for (int j = i; j * i < 300000; j++) {
                    eraList.set(i * j, false);
                }
            }
        }
    }

    @Override
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        try {
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destinationFile);

            int i = 0;
            int readed = 0;
            while ((readed = in.read()) != -1) {
                if (!isPrime(i)) {
                    out.write(key.get(readed));
                } else {
                    out.write(readed);
                }
                i++;
            }
            in.close();
            out.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private boolean isPrime(int number) {
        if (eraList.get(number)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void decode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        try {
            InputStream in = new FileInputStream(sourceFile);
            OutputStream out = new FileOutputStream(destinationFile);

            int i = 0;
            int readed = 0;
            while ((readed = in.read()) != -1) {
                if (!isPrime(i)) {
                    out.write((char) key.indexOf(readed));
                } else {
                    out.write(readed);
                }
                i++;
            }
            in.close();
            out.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
