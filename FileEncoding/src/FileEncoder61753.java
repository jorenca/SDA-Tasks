package fileencrytion;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * @author AleksandraYanovska
 *
 */

public class FileEncoder61753 implements FileEncoder{
	
	@Override 
	public void encode (String sourceFile, String destinationFile, LinkedList<Character> key)
	{
	
	FileInputStream inputStream  = null;
	FileOutputStream outputStream = null;
	
	try{
		
		inputStream = new FileInputStream(sourceFile);
		outputStream = new FileOutputStream(destinationFile);
		
		int counter = 0;
		int current;
		while ((current = inputStream.read()) != -1 )
		{
			
			if( isPrime(counter) || counter==1 )
			{
				outputStream.write(current);
			}
			else 
			{
				outputStream.write(key.get(current));			
			}
			counter++;
		}
		
		inputStream.close();
		outputStream.close();
				
	}
	catch (IOException e)
	{
		e.printStackTrace();
	}
	

	
 }

@Override
public void decode(String encodeFile, String destinationFile, LinkedList<Character> key)
{
	FileInputStream inputStream = null;
	FileOutputStream outputStream = null;
	
	try
	{
		inputStream = new FileInputStream(encodeFile);
		outputStream = new FileOutputStream(destinationFile);
		
		int counter = 0;
		int current;
		
		while (( current = inputStream.read()) != -1 )
		{
			if (isPrime(counter)||counter == 1)
			{
				outputStream.write(key.indexOf((char)current));
				
			}
			else
			{
				outputStream.write(key.indexOf((char)current));
			}
			counter++;
			
		}
			inputStream.close();
			outputStream.close();
				
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}
	
		}
private boolean isPrime(int number)
{
	if (number == 0)
	{
		return false;
	}
	if(number == 1 || number == 2)
	{
		return true;
	}
	for (int i = 2; i <= (int) Math.sqrt(number) + 1; i++)
		{
		if(number % i == 0)
		{
	     return false;
		}

	
}
	return true;
}
}


