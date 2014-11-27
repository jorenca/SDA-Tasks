package fileEncryption;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class FileEncoder61702 implements FileEncoder {

	private static boolean[] isPrimeArray;

	public FileEncoder61702() {
		initialize();
	}

	@Override
	public void encode(String sourceFile, String destinationFile,
			LinkedList<Character> key) {
		ArrayList<Character> myKey = new ArrayList<Character>(key);
		File inputFile = new File(sourceFile);
		if (!inputFile.exists() || inputFile.isDirectory()) {
			System.out.println("File does not exist or is directory");
			return;
		}

		try {
			FileInputStream inStream = new FileInputStream(sourceFile);
			FileOutputStream outStream = new FileOutputStream(destinationFile);
			BufferedInputStream inFileStream = new BufferedInputStream(inStream);
			BufferedOutputStream outFileStream = new BufferedOutputStream(
					outStream);
			int currentCharacter = inFileStream.read();
			for (int i = 0; currentCharacter != -1; ++i) {

				if (isPrime(i)) {
					outFileStream.write(currentCharacter);
				} else {
					outFileStream.write(myKey.get(currentCharacter));
				}
				currentCharacter = inFileStream.read();
			}
			inFileStream.close();
			outFileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void decode(String encodedFile, String destinationFile,
			LinkedList<Character> key) {
		Character[] arrayKey = new Character[256];
		Iterator<Character> iterator = key.listIterator();
		for (int index = 0; iterator.hasNext(); index++) {
			char currentChar = iterator.next();
			arrayKey[index] = currentChar;
		}

		try {
			FileInputStream inStream = new FileInputStream(encodedFile);
			FileOutputStream outStream = new FileOutputStream(destinationFile);
			BufferedInputStream inFileStream = new BufferedInputStream(inStream);
			BufferedOutputStream outFileStream = new BufferedOutputStream(
					outStream);
			int currentCharacter = inFileStream.read();
			for (int i = 0; currentCharacter != -1; ++i) {

				if (isPrime(i)) {
					outFileStream.write(currentCharacter);
				} else {
					outFileStream.write(arrayKey[currentCharacter]);
				}
				currentCharacter = inFileStream.read();
			}
			inFileStream.close();
			outFileStream.close();
		} catch (Exception e) {
			System.err.println("Exception while writing.");
			e.printStackTrace();
		}
	}

	private boolean isPrime(int num) {
		return isPrimeArray[num];
	}

	private void initialize() {
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
		// using Sieve of Eratosthenes
	}
}
