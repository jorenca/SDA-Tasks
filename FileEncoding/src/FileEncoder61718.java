/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Alex Kushev
 */
public class FileEncoder61718 implements FileEncoder {

    public boolean isPrime(int number) {

        if (number == 2 || number == 3) {
            return true;
        }
        if (number % 2 == 0) {
            return false;
        }

        for (int i = 3; i < (int) Math.sqrt(number) + 1; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }

        return number > 0;
    }

    @Override
    public void encode(String inFile, String outFile, LinkedList<Character> key) {

        List<Character> keysContainer = new ArrayList<>(key);
        /*
         * we will use data structure ArrayList because the complexity of method get is 0(1);
         */

        try (FileInputStream in = new FileInputStream(inFile); FileOutputStream out = new FileOutputStream(outFile)) {

            int curChar = 0;
            int indexCounter = 0;

            while ((curChar = in.read()) != -1) {
                if (isPrime(indexCounter) || indexCounter == 1) {
                    out.write(curChar);
                } else {
                    out.write(keysContainer.get(curChar));
                }

                indexCounter++;
            }

        } catch (IOException io) {
            io.getStackTrace();
        }

    }

    @Override
    public void decode(String encodedFile, String outFile, LinkedList<Character> key) {

        List<Character> keyContainer = new ArrayList<>(key);

        try (FileInputStream in = new FileInputStream(encodedFile);
                FileOutputStream out = new FileOutputStream(outFile)) {

            int curChar = 0;
            int indexCounter = 0;

            while ((curChar = in.read()) != -1) {
                if (isPrime(indexCounter) || indexCounter == 1) {
                    out.write(curChar);
                } else {
                    out.write(keyContainer.indexOf((char) curChar));
                }

                indexCounter++;
            }

        } catch (IOException io) {
            io.getStackTrace();
        }

    }

}
