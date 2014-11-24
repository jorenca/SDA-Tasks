import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MyFileEncoder implements FileEncoder61738
{	
	public static void main(String[] args) throws IOException 
	{	
		LinkedList<Character> key = new LinkedList<>();
		
		//the key is 256 255 254 253 ... 1 0
		for( int i = 256; i >= 0; i--)
		{
			key.add((char)i);
		}	
		
		MyFileEncoder enc = new MyFileEncoder();

		File originalFile = new File("files/code.txt");
		File encoded 	  = new File("files/encoded.txt");
		File decoded 	  = new File("files/decoded.txt");
		
		   
		long start = System.currentTimeMillis();
		
			enc.encode(originalFile.getAbsolutePath(), encoded.getAbsolutePath(), key);
		
		StopTimer(start,"total time for encoding ");
		
		start = System.currentTimeMillis();
			
			enc.decode(encoded.getAbsolutePath(), decoded.getAbsolutePath(), key);
		
		StopTimer(start,"total time for decoding ");
	}
	
	public static void StopTimer(long start, String message)
	{
		long end = System.currentTimeMillis();
	    long diff = end - start;
		
		System.out.println(message + diff);
	}
	
	public boolean IsPrime(int number)
	{
		if( number == 0 )
		{
			return false;
		}
		
		if( number > 3 )
		{
			if( number % 2 == 0 || number % 3 == 0 )
			{
				return false;
			}	
		}
		
		for( int i = 5, j = 0; i*i <= number; )
		{	 
			if( number % i == 0 )
			{
				return false;
			}
			
			if(j % 2 == 0) 
			{
				i+=2;
			}
			else
			{
				i+=4;
			}
			j++;
		}
			
		
		return true;
	}
	
	
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) throws IOException
    {
    	ArrayList<Character> ArrKey = new ArrayList<>();
    	
    	//copy the key into array so we can access the elements faster
    	for( Character myChar : key )
    	{
    		ArrKey.add(myChar);
    	}
    	
    	BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "UTF8"));
		BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destinationFile), "UTF8"));   
			
		int i = 0;
		int r = inputStream.read();
		while(  r != -1 )
		{	    
			if( r > 256 )
			{
				r = inputStream.read();
				
				continue;
			}
			
    		Character symbol = (char)r;
			
    		if( IsPrime(i) || i == 1 )
    		{
				outputStream.write(symbol);
    		}
    		else
    		{		
    			Character keySymbol = ArrKey.get(r);
				outputStream.write(keySymbol);
    		}
    		
    		i++;
    		
    		r = inputStream.read();
		}
		
		inputStream.close();
		outputStream.close();
    }
    
    public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) throws IOException
    {
    	HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    	
    	//copy the elements into hash map, so we can access it faster
    	//also use the original keys as key and the index of the key as value
    	int j = 0;
    	for( Character myChar : key )
    	{
    		map.put((int) myChar, j);
    		j++;
    	}
    	
    	
    	
    	BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(encodedFile), "UTF8"));
		BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destinationFile), "UTF8"));

		int i = 0;
		int r = inputStream.read();
		while(  r != -1 )
		{	    		
    		boolean bIsPrime = IsPrime(i);

    		Character symbol = (char)r;
			
    		if( bIsPrime || i == 1 )
    		{
				outputStream.write(symbol);
    		}
    		else
    		{			
    			Character originalSymbol = (char) map.get(r).byteValue();

				outputStream.write(originalSymbol);
    		}
    		i++;
    		
    		r = inputStream.read();
		}
		
		inputStream.close();
    	outputStream.close();
    }

}
