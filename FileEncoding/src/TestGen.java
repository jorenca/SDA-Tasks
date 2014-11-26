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
		AuthorEncoder coder = new AuthorEncoder();
		LinkedList<Character> key = new LinkedList<>();
		for(int i=0; i<256; i++)
		{
			key.add((char)i);
		}
		
		Collections.shuffle(key); 
		dumpKey("key1.txt", key);
		coder.encode("in1.jpg", "out1.enc", key);
		
		Collections.shuffle(key); 
		dumpKey("key2.txt", key);
		coder.encode("in2.rar", "out2.enc", key);
		
		Collections.shuffle(key); 
		dumpKey("key3.txt", key);
		coder.encode("in3.rar", "out3.enc", key);
		
		Collections.shuffle(key); 
		dumpKey("key4.txt", key);
		coder.encode("in4.pdf", "out4.enc", key); 
	}
}
