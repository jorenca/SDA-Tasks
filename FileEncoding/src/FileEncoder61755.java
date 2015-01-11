import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class FileEncoder61755 implements FileEncoder {

    @Override
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        code(sourceFile, destinationFile, toCharArray(key));
    }

    @Override
    public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) {
        code(encodedFile, destinationFile, reverseKey(key));
    }

    public void code(String sourceFile, String destinationFile, char[] key) {
        try {
            byte[] source = Files.readAllBytes(Paths.get(sourceFile));
            byte[] dest = new byte[source.length];
            for (int i = 0; i < source.length; i++) {
                byte b = source[i];
                if (isPrime(i)) {
                    dest[i] = b;
                } else {
                    dest[i] = (byte) key[b & 0xFF];
                }
            }
            Files.write(Paths.get(destinationFile), dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public char[] toCharArray(LinkedList<Character> key) {
        char[] keyArr = new char[key.size()];
        int i = 0;
        for (Character character : key) {
            keyArr[i] = character.charValue();
            i++;
        }
        return keyArr;
    }

    public char[] reverseKey(LinkedList<Character> key) {
        char[] reversedKey = new char[key.size()];
        char i = 0;
        for (Character character : key) {
            reversedKey[character.charValue()] = i;
            i++;
        }
        return reversedKey;
    }

    private static boolean isPrime(int number) {
        if (number % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= number; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
