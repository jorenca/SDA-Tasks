import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;


public class FileEncoder61687 implements FileEncoder{
	
	public void encode(String sourceFile, String destinationFile,
			LinkedList<Character> key) {
		int b;
		int mask = 0;
		
		try {
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
    	for (int i = 1; i < Math.sqrt(number); i += 2) {
        	if (number % i == 0) {
            	return false;
        	}
    	}
    	return true;
	}
}
