package homework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * 
 * @author Borislav Kamenov
 *
 */

public class FileEncoder61672 implements FileEncoder{

	@Override
	public void encode(String sourceFile, String destinationFile,
			LinkedList<Character> key) {
		
		File file = new File(sourceFile);
		FileInputStream fin = null;
		FileOutputStream out = null;
		int i = 0;
		try {
			 fin = new FileInputStream(file);
			 out = new FileOutputStream(destinationFile);
			 
			 while (fin.available() > 0) {
					
					int x = fin.read();
					char c = (char) x;
					if (isPrime(i)) {
						out.write((int)c);
					} else {
						out.write((int) key.get(x));
					}
					out.flush();
					++i;
				}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void decode(String encodedFile, String destinationFile,
			LinkedList<Character> key) {
		
		File file = new File(encodedFile);
		FileInputStream fin = null;
		FileOutputStream out = null;
		try {
			 fin = new FileInputStream(file);
			 out = new FileOutputStream(destinationFile);
			 
			 int i = 0;
			 while (fin.available() > 0) {
					
					int x = fin.read();
					
					if (isPrime(i)) {
						out.write(x);
					} else {
						out.write(indexOfElement((char)x, key));
					}
					out.flush();
					++i;
				}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	private boolean isPrime(int n){
		if(n == 0){
			return false;
		}
		
		 if(n==2 || n == 1){ 
		     return true;
		  }
		  for(int i=2;i <= (int)Math.sqrt(n)+1; i++){
		    if(n%i==0){ 
		      return false;
		    }
		 }
		 return true; 
	}
	private int indexOfElement(char elem, LinkedList<Character> list){
		
		int i = 0;
		for(char c: list){
			if(c == elem){
				return i;
			}
			++i;
		}
		return 0;
	}
}
