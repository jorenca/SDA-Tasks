import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * @author Simeon Markov
 */
public class FileEncoder61717 implements FileEncoder {

    @Override
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        validateArgs(sourceFile, destinationFile, key);
        try {
            createFile(destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        InputStream is = null;
        try {
            is = new FileInputStream(sourceFile);
            byte[] buf = new byte[1024];
            int totalBytesCount = 0;
            int bytesCount;
            while ((bytesCount = is.read(buf)) != -1) {
                for (int i = 0; i < bytesCount; i++) {
                    int byteIndex = i + totalBytesCount;
                    if (!isPrime(byteIndex)) {
                        char keyChar = key.get((char) buf[i] & 0xFF);
                        buf[i] = (byte) keyChar;
                    }
                }
                appendBytes(buf, bytesCount, destinationFile);
                totalBytesCount += bytesCount;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.out.println("WARNING: Failed to close file " + sourceFile);
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) {
        validateArgs(encodedFile, destinationFile, key);
        try {
            createFile(destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        InputStream is = null;
        try {
            is = new FileInputStream(encodedFile);
            byte[] buf = new byte[1024];
            int totalBytesCount = 0;
            int bytesCount;
            while ((bytesCount = is.read(buf)) != -1) {
                for (int i = 0; i < bytesCount; i++) {
                    int byteIndex = i + totalBytesCount;
                    if (!isPrime(byteIndex)) {
                        char keyChar = (char) buf[i];
                        int index = key.indexOf(keyChar);
                        buf[i] = (byte) index;
                    }
                }
                appendBytes(buf, bytesCount, destinationFile);
                totalBytesCount += bytesCount;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.out.println("WARNING: Failed to close file " + encodedFile);
                    e.printStackTrace();
                }
            }
        }

    }

    private void createFile(String path) throws IOException {
        File file = new File(path);
        if (file.isFile()) {
            file.delete();
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
                file.createNewFile();
            }
        }
    }

    private void appendBytes(byte[] bytes, int len, String file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file, true);
        try {
            fos.write(bytes, 0, len);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.out.println("WARNING: Failed to close file " + file);
                }
            }
        }
    }

    private boolean isPrime(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Invalid number " + number);
        }
        for (int j = 3; j <= Math.sqrt(number); j += 2) {
            if (number % j == 0)
                return false;
        }
        if ((number % 2 == 0 && number > 2) || number == 0)
            return false;
        return true;
    }

    private void validateArgs(String sourceFile, String destinationFile, LinkedList<Character> key) {
        checkNullOrEmpty(sourceFile, "sourceFile");
        checkNullOrEmpty(destinationFile, "destinationFile");
        checkNullOrEmpty(key, "key");
        checkFile(sourceFile);
    }

    private void checkNullOrEmpty(Object arg, String argName) {
        if (arg == null) {
            throw new IllegalArgumentException(String.format("Argument %s is null", argName));
        } else if (arg instanceof String && ((String) arg).isEmpty()) {
            throw new IllegalArgumentException(String.format("Argument %s is empty", argName));
        } else if (arg instanceof LinkedList<?> && ((LinkedList<?>) arg).size() < 256) {
            throw new IllegalArgumentException(String.format("Argument %s must be 256 characters long", argName));
        }
    }

    private void checkFile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            throw new IllegalArgumentException("Non-existent file " + file.getAbsolutePath());
        }
    }

}
