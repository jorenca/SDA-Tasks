import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

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
		FileEncoder61733 coder = new FileEncoder61733();


		LinkedList<Character> key = new LinkedList<>();

		FileInputStream keyFile = null;

		String[] keys = new String[]{"key1.txt", "key2.txt", "key3.txt", "key4.txt"};
		String[] inputs = new String[]{"in1.jpg", "in2.RAR", "in3.rar", "in4.pdf"};
		String[] outputs = new String[]{"out1.enc", "out2.enc", "out3.enc", "out4.enc"};
		String[] originalOutputs = new String[]{"original-out1.enc", "original-out2.enc", "original-out3.enc", "original-out4.enc"};


		for (int i = 0; i<4; i++) {
			keyFile = new FileInputStream(keys[i]);

			int c;
			while ((c = keyFile.read()) != -1) {
				key.add((char)c);
			}

			coder.encode(inputs[i], outputs[i], key);

			System.out.print("Test  " + (i+1) + " is ... " + readAndCompareFiles(outputs[i], originalOutputs[i]) + "\n");
			while (!key.isEmpty()) {
				key.removeFirst();
			}
		}
	}

	private static boolean readAndCompareFiles(String pathToCurrentFile, String pathToExpectedFIle){
		FileInputStream current = null;
		FileInputStream expected = null;
		try{
			current = new FileInputStream(pathToCurrentFile);
			expected = new FileInputStream(pathToExpectedFIle);

			int currentChar;
			int expectedChar;
			while ((currentChar = current.read()) != -1 && (expectedChar = expected.read()) != -1) {

				if(currentChar != expectedChar){
					return false;
				}
			}
		}catch(Exception ex){
			System.out.print(ex);
			return false;
		}
		return true;
	}
}