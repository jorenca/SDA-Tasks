import java.util.LinkedList;
import java.util.Arrays;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
/**
 * 
 * @author Borislav Mitkov
 *
 */
public class FileEncoder61668 implements FileEncoder {
	
	public boolean isPrime(int n) 
	{
		return primes[n];
	}
	
	@Override
	public  void encode(String inFile, String outFile,LinkedList<Character> key) 
	{
			
			File infile = new File(inFile);
			File outfile = new File(outFile);
			
			if (!infile.exists()) 
			{
				try 
				{
					outfile.createNewFile();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
			try(FileInputStream fip = new FileInputStream(infile);FileOutputStream fop = new FileOutputStream(outfile))
			{
				for(int i=0;i<infile.length();i++)
				{
					char b = (char)fip.read();
					if(primes[i]) fop.write(b);
					else fop.write(key.get(b));
				}
			fip.close();
			fop.close();
			} 	
			catch (IOException e)
			{
				e.printStackTrace();
			}
			System.out.println("EncodingDone");
	}

	@Override
	public void decode(String inFile, String outFile,LinkedList<Character> key) 
	{
		File efile = new File(inFile);
		File dfile = new File(outFile);
		
		if (!dfile.exists()) 
		{
			try 
			{
				dfile.createNewFile();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try(FileInputStream fip = new FileInputStream(efile);FileOutputStream fop = new FileOutputStream(dfile))
		{
			for(int i=0;i<efile.length();i++)
			{
				char b = (char)fip.read();
				if(primes[i]) fop.write(b);
				else fop.write(key.indexOf(b));
			}
			fip.close();
			fop.close();
		}
		catch (IOException e)
			{
			e.printStackTrace();
			}
		System.out.println("DecodingDone");
	}

	public FileEncoder61668 ()
	{
			
	} 
	
	static boolean  primes[] = new boolean[1000000]; 
	//Създаваме простите
	public static void fillSieve() 
	{
	    Arrays.fill(primes,true);        // Приемаме всички числа да са прости
	    primes[0]=false; 			    // 0 не е просто
	    primes[1]=true;					// приемаме 1 за просто
	    for (int i=2;i<primes.length;i++) 
	    {
	        //Ако числото е просто, 
	        //то преглеждаме всички негови кратни и им дава стойност false.
	        if(primes[i]) 
	        	{
	            for (int j=2;i*j<primes.length;j++) 
	            	{
	                	primes[i*j]=false;
	            	}
	        }
	    }
	}
	//Зарежда ключ от даден файл
	private static void loadKey(String keypath, LinkedList<Character> key)
	{
		File keyfile = new File(keypath);
		try(FileInputStream fip = new FileInputStream(keyfile))
		{
			for(int i=0; i<256; i++)
				{
					key.add((char)fip.read());
				}
			fip.close();
		}
		catch (IOException e)
			{
				e.printStackTrace();
			}
		System.out.println("Done loading key");
	}
	
	public static void main(String[] args) 
	{
		fillSieve();// За запълване на масива с прости числа;
		
		FileEncoder61668 code = new FileEncoder61668();
		
		LinkedList<Character> key = new LinkedList<>();
		
		loadKey("key1.txt",key);
		code.encode("in1.jpg","encodedfile1.enc",key);
		code.decode("encodedfile1.enc","picture.jpg",key);
		key.clear();
		
		loadKey("key2.txt",key);
		code.encode("in2.RAR","encodedfile2.enc",key);
		code.decode("encodedfile2.enc","nqkavRAR.RAR",key);
		key.clear();
		
		loadKey("key3.txt",key);
		code.encode("in3.rar","encodedfile3.enc",key);
		code.decode("encodedfile3.enc","drugrar.rar",key);
		key.clear();
		
		loadKey("key4.txt",key);
		code.encode("in4.pdf","encodedfile4.enc",key);
		code.decode("encodedfile4.enc","nqkavpdf.pdf",key);
		key.clear();
		
		System.out.println("Program finished");
		

	}

}
