package main;

import java.util.LinkedList;
import java.util.HashSet;
import java.io.IOException;
import java.io.FileOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import contracts.FileEncoder61712;
//import helpers.PrimeGenerator;

public class FileEncoder61712 implements FileEncoder {
	private static final String DECODE_OP = "decode";
	private static final String ENCODE_OP = "encode";
	private static final int PRIME_NUMS_COUNT = 260;
	
	private HashSet<Integer> primeNumbers;
	private String sourceFilePath;
	private String destinationFilePath;
	private LinkedList<Character> keys;
	
	public FileEncoder() {
		this.primeNumbers = PrimeGenerator.generateNumbers(260);
	}
	
	public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {
		this.setOperationalData(sourceFile, destinationFile, key);
		this.manipulateFile(ENCODE_OP);
	}
	
	public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) {
		this.setOperationalData(encodedFile, destinationFile, key);
		this.manipulateFile(DECODE_OP);
	}
	
	private void manipulateFile(String operation) {
		Path path = FileSystems.getDefault().getPath("", this.sourceFilePath);
		
		try {
	        byte[] fileBytes = Files.readAllBytes(path);
	        
	        switch (operation) {
	        	case ENCODE_OP:
	        		this.encodeBytes(fileBytes);
	        		break;
	        	case DECODE_OP:
	        		this.decodeBytes(fileBytes);
	        		break;
        		default:
        			// throw invalid operation exception
        			break;
	        }
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void encodeBytes(byte[] data) {
		byte[] output = new byte[data.length];
		
		for (int i = 0; i < data.length; i++) {			
			if (this.primeNumbers.contains(i)) {
				output[i] = data[i];
			} else {
				char key = this.keys.get(data[i] & 0xFF);
				
				output[i] = (byte)key;
			}
		}
		
		this.saveByteArrayToFile(output);
	}
	
	private void decodeBytes(byte[] data) {
		byte[] output = new byte[data.length];
		
		for (int i = 0; i < data.length; i++) {
			if (this.primeNumbers.contains(i)) {
				output[i] = data[i];
			} else {
				char keyChar = (char)(data[i] & 0xFF);
				
				output[i] = (byte)this.keys.indexOf(keyChar);
			}
		}
		
		this.saveByteArrayToFile(output);
	}
	
	private void saveByteArrayToFile(byte[] data) {
		try {
			FileOutputStream outputStream = new FileOutputStream(this.destinationFilePath);
			outputStream.write(data);
			outputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void setOperationalData(String sourceFile, String destinationFile, LinkedList<Character> key) {
		this.sourceFilePath = sourceFile;
		this.destinationFilePath = destinationFile;
		this.keys = key;
	}
}

// helpers/PrimeGenerator.java
public class PrimeGenerator {
	private static final int ONE = 1;
	
	public static HashSet<Integer> generateNumbers(int count) {
		HashSet<Integer> numbers = new HashSet<Integer>();
		int counter = 0;
		
		// Adding the special case
		numbers.add(ONE);
		
		for (int i = 2; i <= count; i++) {
		    counter = 0;
		    
		    for (int n = 2; n < i; n++) {
		        if (i % n == 0) {
		            counter++;
		        }
		    }
		    
		    if (counter == 0) {
		        numbers.add(i);
		    }
		}
		
		return numbers;
	}
}
