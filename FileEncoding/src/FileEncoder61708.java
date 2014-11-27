import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class FileEncoder61708 implements FileEncoder{

	private boolean[] sieve;
	
	@Override
	public void encode(String inFile, String outFile, LinkedList<Character> key) {
		FileInputStream input = null;
		FileOutputStream output = null;
		
		try{
			input = new FileInputStream(inFile);
			output = new FileOutputStream(outFile);
			
			long lengthInBytes = input.getChannel().size();
			this.getPrimeNumbersInRange(0, lengthInBytes);
			int current = 0;
			int index = 0;
			while((current = input.read()) != -1){
				if(sieve[index] == true){
					output.write(current);
				}
				else{
					output.write(key.get(current));
				}
				
				index++;
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		finally{
			try{
				input.close();
				output.close();
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void decode(String encodedFile, String outFile,LinkedList<Character> key) {
		FileInputStream input = null;
		FileOutputStream output = null;
		try{
			input = new FileInputStream(encodedFile);
			output = new FileOutputStream(outFile);
			
			long lengthInBytes = input.getChannel().size();
			this.getPrimeNumbersInRange(0, lengthInBytes);
			int current = 0;
			int index = 0;
			while((current = input.read()) != -1){
				if(sieve[index] == true){
					output.write(current);
				}
				else{
					output.write(key.indexOf((char)current));
				}
				
				index++;
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		finally{
			try{
				input.close();
				output.close();
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
	}
	
	private void getPrimeNumbersInRange(int start, long end){
		sieve = new boolean[(int)end];
		Arrays.fill(sieve, Boolean.TRUE);
		
		sieve[0] = false;
		for (int i = 2; i < Math.sqrt(end); i++) {
			if(sieve[i]){
				for (int j = i*i; j < sieve.length; j += i) {
				sieve[j] = false;	
				}
			}
		}
	}
}
