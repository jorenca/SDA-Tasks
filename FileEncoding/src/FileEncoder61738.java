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

<<<<<<< HEAD
public class FileEncoder61738 implements FileEncoder {
    public static void main(String[] args) {
        LinkedList<Character> key = new LinkedList<>();

        for (int i = 0; i < 256; i++) {
            key.add((char) i);
        }

        Collections.shuffle(key);

        FileEncoder61738 enc = new FileEncoder61738();

        File originalFile = new File("files/in4.pdf");
        File encoded = new File("files/encoded.txt");
        File decoded = new File("files/decoded.pdf");

        long start = System.currentTimeMillis();

        enc.encode(originalFile.getAbsolutePath(), encoded.getAbsolutePath(), key);

        StopTimer(start, "total time for encoding ");

        start = System.currentTimeMillis();

        enc.decode(encoded.getAbsolutePath(), decoded.getAbsolutePath(), key);

        StopTimer(start, "total time for decoding ");
    }

    public static void StopTimer(long start, String message) {
        long end = System.currentTimeMillis();
        long diff = end - start;

        System.out.println(message + diff);
    }

    public boolean IsPrime(int number) {
        if (number == 0) {
            return false;
        }

        if (number > 3) {
            if (number % 2 == 0 || number % 3 == 0) {
                return false;
            }
        }

        for (int i = 5, j = 0; i * i <= number;) {
            if (number % i == 0) {
                return false;
            }

            if (j % 2 == 0) {
                i += 2;
            } else {
                i += 4;
            }
            j++;
        }

        return true;
    }

    @Override
    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile));

            TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();

            // copy the elements into tree map, so we can access the key faster
            // keys are in the range [-128,127] and the values are the elements of the passed key
            // the range is such, because the bytes are in range [-128,127]
            // and i want directly to get the value of the map for this key
            // based on the value of the byte

            int k = -128;
            for (Character myChar : key) {
                map.put(k, (int) myChar);
                k++;
            }

            File source = new File(sourceFile);

            byte[] buffer = new byte[(int) source.length()];

            int numberOfBytes = inputStream.read(buffer);

            if (numberOfBytes != -1) {
                for (int i = 0; i < numberOfBytes; i++) {
                    if (IsPrime(i)) {
                        outputStream.write(buffer[i]);
                    } else {
                        int key1 = buffer[i];
                        int valueMap = map.get(key1);
                        char symbolMap = (char) (valueMap);
                        outputStream.write(symbolMap);
                    }
                }
            }

            inputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(encodedFile));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile));

            TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();

            // copy the elements of the key into tree map for faster access
            // the key in the map will be the elements in the key and the values of the map will be
            // in the range [-128,127] - the opposite than the encode
            int k = -128;
            for (Character myChar : key) {
                map.put((int) myChar, k);
                k++;
            }

            File encoded = new File(encodedFile);

            byte[] buffer = new byte[(int) encoded.length()];

            int numberOfBytes = inputStream.read(buffer);
            if (numberOfBytes != -1) {
                for (int i = 0; i < numberOfBytes; i++) {
                    if (IsPrime(i)) {
                        outputStream.write(buffer[i]);
                    } else {
                        int sum = 0;

                        // imagine that we want to encode 1. The code of 1 is 49.
                        // also let the value which corresponds to 49 in the key is 164.
                        // then in the encoded file we have to write 164.
                        // after we read the encoded file, though, 164 is transformed into -92
                        // which is with 256 less. So to recover the original value, which is 164
                        // i just add 256. Note that this happens only when the byte is encoded
                        // and when the byte is negative ( when we read the encoded file )

                        if (buffer[i] < 0) {
                            sum = 256;
                        }

                        int key1 = buffer[i] + sum;

                        int valueMap = map.get(key1);
                        char symbolMap = (char) (valueMap);

                        outputStream.write(symbolMap);
                    }
                }
            }

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
=======
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
>>>>>>> bc489de94e46b240b29c0ca826be1e766f2ca89a
    }
}
