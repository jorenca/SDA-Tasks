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
 */

public class FileEncoder61738 implements FileEncoder
{	
	public static void main(String[] args)
	{	
		LinkedList<Character> key = new LinkedList<>();
		
		for( int i = 255; i >= 0; i--)
		{
			key.add((char)i);
		}	
		
		Collections.shuffle(key);
		FileEncoder61738 enc = new FileEncoder61738();

		File originalFile = new File("files/in1.jpg");
		File encoded 	  = new File("files/encoded.txt");
		File decoded 	  = new File("files/decoded.jpg");
		
		   
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
	    	 FileInputStream inputStream 	=  new FileInputStream(sourceFile);
	         FileOutputStream outputStream 	=  new FileOutputStream(destinationFile);
	         
	        TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
	    	
	    	//copy the elements into tree map, so we can access the key faster
	        //keys are in the range [-128,127] and the values are the elements of the passed key
	        //the range is such, because the bytes are in range [-128,127]
	        //and i want directly to get the value of the map for this key
	        //based on the value of the byte 
	        
	    	int k = -128;
	    	for( Character myChar : key )
	    	{
	    		map.put(k, (int)myChar);
	    		k++;
	    	}
	    	
	    	File source = new File(sourceFile);
	    	
	    	byte[] buffer = new byte[(int)source.length()];
		
			int numberOfBytes = inputStream.read(buffer);
			
			if( numberOfBytes != -1)
			{	    
				for( int i = 0; i < numberOfBytes; i++)
				{
		    		if( IsPrime(i) )
		    		{
						outputStream.write(buffer[i]);
		    		}
		    		else
		    		{		
		    			int key1 = buffer[i];
		    			int valueMap = map.get(key1);
		    			char symbolMap = (char) (valueMap);
						outputStream.write(symbolMap);
		    		}
				}
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
	        FileInputStream inputStream 	=  new FileInputStream(encodedFile);
	        FileOutputStream outputStream 	=  new FileOutputStream(destinationFile);
	        
	    	TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
	    	
	    	//copy the elements into tree map, so we can access it faster
	    	//also use the original keys as key and the index of the key as value
	    	int k = -128;
	    	for( Character myChar : key )
	    	{
	    		map.put((int) myChar, k);
	    		k++;
	    	}
	    	
	    	File encoded = new File(encodedFile);
	    	
	    	byte[] buffer = new byte[(int)encoded.length()];
	
			int numberOfBytes = inputStream.read(buffer);
			if( numberOfBytes != -1)
			{	    
				for( int i = 0; i < numberOfBytes; i++)
				{		
		    		if( IsPrime(i) )
		    		{
						outputStream.write(buffer[i]);
		    		}
		    		else
		    		{		
		    			int sum = 0;
		    			
		    			//i noticed that for the bytes with negative values
		    			//256 must be added, so that the original value can be obtained
		    			if( buffer[i] < 0 )
		    			{
		    				sum = 256;
		    			}
		    			
		    			int key1 = buffer[i]+sum;
		    			int valueMap = map.get(key1);
		    			char symbolMap = (char) (valueMap );
		    			
						outputStream.write(symbolMap);
		    		}
				}
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
