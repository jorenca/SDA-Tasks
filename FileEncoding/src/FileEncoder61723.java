/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.LinkedList;

public class FileEncoder61723 implements FileEncoder{

    public static boolean isPrime(int number) {
          int sqrtNumber = (int) Math.sqrt(number);
          boolean[] isComposite = new boolean[number + 1];

          for (int i = 2; i <= sqrtNumber; i++) {
              if (!isComposite[i]) {
                  for (int j = i * i; j <= number; j += i) {
                      isComposite[j] = true;
                  }
              }
          }
          return !isComposite[number];
      }

    public static Hashtable<Integer, Character> toHashTable (LinkedList<Character> list){
      Hashtable<Integer, Character> table = new Hashtable<Integer, Character>();

      for (int i = 0; i < list.size(); i++) {
      table.put(i, list.get(i));
    }
      return table;
    }

    @Override

    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) {
        Path path = Paths.get(sourceFile);
        byte[] allBytes;
        FileOutputStream fileOutput;
        Hashtable<Integer, Character> hashtable = toHashTable(key);
        int number = 0;

        try {
            allBytes = Files.readAllBytes(path);
            fileOutput = new FileOutputStream(destinationFile);
            for (int i = 0; i < allBytes.length; i++) {
                if (isPrime(i) && i != 0) {
                    fileOutput.write(allBytes[i]);
                } else {
                    number = allBytes[i] & 0xff;
                    fileOutput.write(hashtable.get(number));
                }
            }
            fileOutput.flush();
            fileOutput.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override

    public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) {
        Path path = Paths.get(encodedFile);
        byte[] allBytes;
        FileOutputStream fileOutput;
        Hashtable<Integer, Character> hashtable = toHashTable(key);
        int index = 0;

        try {
            allBytes = Files.readAllBytes(path);
            fileOutput = new FileOutputStream(destinationFile);
            for (int i = 0; i < allBytes.length; i++) {
                if (isPrime(i) && i != 0) {
                    fileOutput.write(allBytes[i]);
                } else {
                    index = allBytes[i] & 0xff;
                    fileOutput.write(hashtable.get(index));
                }
            }
            fileOutput.flush();
            fileOutput.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
