import java.io.FileOutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Georgi Gaydarov
 *
 */
public abstract class TestGen 
{
	private static void dumpKey(String where, List<Character> key) throws Exception
	{
		FileOutputStream keyOut = new FileOutputStream(where);
		for(char c : key)
		{
			keyOut.write(c); 
		}
		keyOut.close();
	}
	public static void main(String[] a) throws Exception
	{
		FileEncoder61692 coder = new FileEncoder61692();
		LinkedList<Character> key = new LinkedList<Character>();
		for(int i=0; i<256; i++)
		{
			key.add((char)i);
		}
		
		long start = System.nanoTime();
		
		Collections.shuffle(key); 
		dumpKey("key1.txt", key);
		coder.encode("in1.jpg", "out1.enc", key);
		coder.decode("out1.enc", "decode1.jpg", key);
		
		long end = System.nanoTime();
		System.out.println(((end - start)/1000000) + "ms");
		
		start = System.nanoTime();
		
		Collections.shuffle(key); 
		dumpKey("key2.txt", key);
		coder.encode("in2.rar", "out2.enc", key);
		
		end = System.nanoTime();
		System.out.println(((end - start)/1000000) + "ms");
		
		start = System.nanoTime();
		
		Collections.shuffle(key); 
		dumpKey("key3.txt", key);
		coder.encode("in3.rar", "out3.enc", key);
		coder.decode("out3.enc", "decode3.rar", key);
		
		end = System.nanoTime();
		System.out.println(((end - start)/1000000) + "ms");
		
		start = System.nanoTime();
		
		Collections.shuffle(key); 
		dumpKey("key4.txt", key);
		coder.encode("in4.pdf", "out4.enc", key);
		coder.decode("out4.enc", "decode4.pdf", key);
		
		end = System.nanoTime();
		System.out.println(((end - start)/1000000) + "ms");
		
		start = System.nanoTime();
		
		Collections.shuffle(key); 
		dumpKey("key4.txt", key);
		coder.encode("kirilica.txt", "out5.enc", key);
		coder.decode("out5.enc", "decode5.txt", key);
		
		end = System.nanoTime();
		System.out.println(((end - start)/1000000) + "ms");
	}
}