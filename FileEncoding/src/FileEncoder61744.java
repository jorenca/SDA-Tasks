package fileencryption;

import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author MarinaG
 */
public class FileEncoder61744 implements FileEncoder {
    
    @Override
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {
       
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        
        try {
            inputStream = new FileInputStream(sourceFile);
            outputStream = new FileOutputStream(destinationFile);
            
            int current;
            int count = 0;
            while ((current = inputStream.read()) != -1) {
                if (isPrime(count) || count == 1) {
                    outputStream.write(current);
                } else {
                    outputStream.write(key.get(current));
                }
                count++;
            }
            
            inputStream.close();
            outputStream.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }       
    }

    @Override
    public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) {
        
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        
        try {
            inputStream = new FileInputStream(encodedFile);
            outputStream = new FileOutputStream(destinationFile);
            
            int current;
            int count = 0;
            while ((current = inputStream.read()) != -1) { 
                if (isPrime(count) || count == 1) {
                    outputStream.write(current);
                } else {
                    outputStream.write(key.indexOf((char)current));
                }
                count++;
            }
            inputStream.close();
            outputStream.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private boolean isPrime(int number) {
        if (number == 0) {
            return false;
        }
        if (number == 1 || number == 2) {
            return true;
        }
        for (int i = 2; i <= (int) Math.sqrt(number) + 1; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
    
}
