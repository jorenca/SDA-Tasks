import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;


public class FileEncoder61662 implements FileEncoder {



     public static boolean isPrime(int number) {
      if(number<=1) {
          return false;
          }
      if(number<=2) {
          return true;
          }
      if(number%2==0){
          return false;
          }
      for(int i=3; i<Math.sqrt(number)+1;i+=2){
         if(number%i==0){
          return false;
          }
     }
        return true;
     }



	     public  void encode(String sourceFile, String destinationFile,
	LinkedList<Character> key) {
		int b;
		int count=0;
		
		try{
			BufferedInputStream input = new BufferedInputStream(new FileInputStream(sourceFile));
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destinationFile));
			
		    while((b=input.read())!=-1) {
			if(!isPrime(count)){
			   output.write(key.get(b));
			}
			else {
			
			  output.write(b);
			}
			count++;
			
			}
			input.close();
			output.close();
		
		} catch (FileNotFoundException e) {
		e.printStackTrace();
		} catch (IOException e1) {
		e1.printStackTrace();
		}
		}
		
	 public  void decode(String encodedFile, String destinationFile,
	LinkedList<Character> key) {
		int b;
		int count=0;
		
			try {
				BufferedInputStream input = new BufferedInputStream(new FileInputStream(encodedFile));
				BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destinationFile));
				
			while((b = input.read()) != -1) {
				if(!isPrime(count)) {
				 output.write((key.indexOf((char)b)));
				}
				else {
				 output.write(b);
				}
				count++;
				}
				 input.close();
				 output.close();
				
				}
			catch (FileNotFoundException e) {
			e.printStackTrace();
			} catch (IOException e1) {
			e1.printStackTrace();
			}
			
			
			}
		}