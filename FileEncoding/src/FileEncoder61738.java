import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * 
 * @author Todor Zhelev
 * 
 *         GitHub: http://goo.gl/qEmsEU
 * 
 */

public class FileEncoder61738 implements FileEncoder {
    public class FileEncoder61738 implements FileEncoder
{	
	public static void main(String[] args)
	{	
		LinkedList<Character> key = new LinkedList<>();
		
		for(int i=0; i<256; i++)
		{
			key.add((char)i);
		}
		
		Collections.shuffle(key);
		
		FileEncoder61738 enc = new FileEncoder61738();

		File originalFile = new File("files/in4.pdf");
		File encoded 	  = new File("files/encoded.txt");
		File decoded 	  = new File("files/decoded.pdf");
		
		   
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
	
	
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key)
    {
    	try
    	{
    		BufferedInputStream inputStream   = new BufferedInputStream(new FileInputStream(sourceFile));
    		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile));
	         
	        TreeMap<Integer, Character> map = new TreeMap<Integer, Character>();
	              	
	    	int k = 0;
	    	for( Character myChar : key )
	    	{
	    		map.put(k, myChar);
	    		k++;
	    	}
	        
	    	int r;
	    	int i = 0;
	    	
	    	while( (r = inputStream.read()) >= 0)
	    	{
	    		char symbol = (char) r;
	    		
	    		if( IsPrime(i) )
	    		{
					outputStream.write(symbol);
	    		}
	    		else
	    		{		
	    			char symbolMap = (char) map.get((int)symbol);
					outputStream.write(symbolMap);
	    		}
	    		
	    		i++;
	    	}   
			
			inputStream.close();
			outputStream.close();
			
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public void decode(String encodedFile, String destinationFile, LinkedList<Character> key)
    {
    	try
    	{	
    		BufferedInputStream inputStream   = new BufferedInputStream(new FileInputStream(encodedFile));
    		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile));
	
	    	TreeMap<Character, Character> map = new TreeMap<Character, Character>();
          	
	    	int k = 0;
	    	for( Character myChar : key )
	    	{
	    		map.put(myChar, (char) k);
	    		k++;
	    	}
	        
	    	int r;
	    	int i = 0;
	    	
	    	while( (r = inputStream.read()) >= 0)
	    	{
	    		char symbol = (char) r;
	    		
	    		if( IsPrime(i) )
	    		{
					outputStream.write(symbol);
	    		}
	    		else
	    		{		
	    			char symbolMap = (char) map.get(symbol);
					outputStream.write(symbolMap);
	    		}
	    		
	    		i++;
	    	}   
	    	
			inputStream.close();
			outputStream.close();
	    }
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
	}
}
