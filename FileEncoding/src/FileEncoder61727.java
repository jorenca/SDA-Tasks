import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class FileEncoder61727 implements IFileEncoder61727 {

	private static boolean[] isPrimeArray;

	public FileEncoder61727() {
		init();
	}

	@Override
	public void encode(String sourceFile, String destinationFile,
			LinkedList<Character> key) {
		ArrayList<Character> arrayListKey = new ArrayList<Character>(key);
		File inputFile = new File(sourceFile);
		if (!inputFile.exists() || inputFile.isDirectory()) {
			System.out.println("No file found.");
			return;
		}

		try {
			FileInputStream inputFileStream = new FileInputStream(sourceFile);
			FileOutputStream outputFileStream = new FileOutputStream(
					destinationFile);		
			for (int i = 0, 
					currentCharacter = inputFileStream.read(); 
					currentCharacter != -1; ++i, 
					currentCharacter = inputFileStream.read()) {
				if (isPrime(i)) {
					outputFileStream.write(currentCharacter);
				} else {
					outputFileStream.write(arrayListKey.get(currentCharacter));
				}
			}
			inputFileStream.close();
			outputFileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void decode(String encodedFile, String destinationFile,
			LinkedList<Character> key) {
		// for faster search
		Character[] myKey = new Character[256];
		Iterator<Character> it = key.listIterator();
		for (int index = 0; it.hasNext(); index++) {
			char currentChar = it.next();
			myKey[index] = currentChar;
		}
		try {
			FileInputStream fileInputStr = new FileInputStream(encodedFile);
			FileOutputStream fileOutputStr = new FileOutputStream(
					destinationFile);
			
			for (int i = 0, currChar = fileInputStr.read(); 
					currChar != -1; 
					++i, currChar = fileInputStr.read()) {
			if (isPrime(i)) {
					fileOutputStr.write(currChar);
				} else {
					fileOutputStr.write(myKey[currChar]);
				}
			}
			fileInputStr.close();
			fileOutputStr.close();
		} catch (Exception ex) {
			System.err.println("Output stream error :(");
	}
	}

	private boolean isPrime(int num) {
		return isPrimeArray[num];
	}

	private void init() {
		int N = 307200;
		isPrimeArray = new boolean[N + 1];
		isPrimeArray[1] = true;
		for (int i = 2; i <= N; i++) {
			isPrimeArray[i] = true;
		}

	for (int i = 2; i * i <= N; i++) {

			if (isPrimeArray[i]) {
				for (int j = i; i * j <= N; j++) {
					isPrimeArray[i * j] = false;
				}
		}
		}
	}

}
