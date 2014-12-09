import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;


public class FileEncoder61687 implements FileEncoder{
	
	public void encode(String sourceFile, String destinationFile,
			LinkedList<Character> key) {
		int b;
		int mask = 0;
		
		try{
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(sourceFile));
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destinationFile));
			
			while((b = input.read()) != -1) {
				if (isPrime(mask)  || mask == 1) {
					output.write(b);
				} else {
					output.write(key.get(b)); 
				}
				mask++;
			}
			
			input.close();
	    	output.close();
	    	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	public void decode(String encodedFile, String destinationFile,
			LinkedList<Character> key) {
		int b;
		int mask = 0;
		
		try {
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(encodedFile));
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destinationFile));
            
			
			while((b = input.read()) != -1) {
				
				if (isPrime(mask) || mask == 1) {
					output.write(b);
				} else {
					output.write((key.indexOf((char) b)));
				}
			mask ++;
			}
			
			input.close();
	    	output.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	
	private static boolean isPrime(int number) {
		if (number == 0)
            return false;
        if (number == 1 || number == 2)
            return true;
        boolean isPrime = true;
        int limit = (int) Math.sqrt(number) + 1;

        for (int i = 2; i <= limit; i++) {
            if (number % i == 0) {
                isPrime = false;
                break;
            }
        }
        return isPrime;
	}
}
