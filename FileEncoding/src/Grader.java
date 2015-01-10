import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author georgi.gaydarov
 * 
 */
public class Grader {
    private FileEncoder authorEncoder;

    private static List<Class<? extends FileEncoder>> classesToBeGraded;

    private List<FileEncoder> instancesToBeGraded;

    private static List<Class<? extends FileEncoder>> getWorks() throws ClassNotFoundException {
        List<Class<? extends FileEncoder>> result = new LinkedList<Class<? extends FileEncoder>>();
        
        result.add(FileEncoder61717.class);
        if(true) return result;

        File folder = new File("./src/");
        File[] listOfFiles = folder.listFiles();
        Pattern homeworkPattern = Pattern.compile("FileEncoder(\\d){5}.java");

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String fileName = file.getName();
                Matcher matcher = homeworkPattern.matcher(fileName);
                if (matcher.find()) {
                    String className = fileName.replaceFirst(".java", "");
                    System.out.println("Adding class " + className + " for grading.");
                    result.add((Class<? extends FileEncoder>) Class.forName(className));
                }
            }
        }

        return result;
    }

    private static List<FileEncoder> getEncoderInstances(List<Class<? extends FileEncoder>> classes)
        throws InstantiationException,
            IllegalAccessException {
        List<FileEncoder> result = new LinkedList<FileEncoder>();
        for (Class<? extends FileEncoder> clazz : classes) {
            result.add(clazz.newInstance());
        }
        return result;
    }

    public Grader() throws Exception {
        classesToBeGraded = getWorks();
        instancesToBeGraded = getEncoderInstances(classesToBeGraded);
        authorEncoder = new AuthorEncoder();
    }

    public List<Double> gradeAllAgainst(String inFile, LinkedList<Character> key, boolean encodingTask)
        throws Exception {
        String authorOutFile = "authorOut.enc";
        String gradedOutFile = "out.enc";

        File aoutf = new File(gradedOutFile);
    	aoutf.delete();
    	long authorTime=0;
    	try {
        	long authorStart = System.nanoTime();
        	if (encodingTask) {
            	authorEncoder.encode(inFile, authorOutFile, key);
        	} else {
            	authorEncoder.decode(inFile, authorOutFile, key);
        	} 
        	authorTime = System.nanoTime() - authorStart;
    	} catch (Throwable t) {}
        System.out.println("Author result: " + Math.round(authorTime / 1000000.0) + "ms");

        
        List<Double> results = new LinkedList<Double>();
        for (FileEncoder work : instancesToBeGraded) {
        	File outf = new File(gradedOutFile);
        	outf.delete();
            
        	long workTime = 0;
            try {
                long workStart = System.nanoTime();
                if (encodingTask) {
                    work.encode(inFile, gradedOutFile, key);
                } else {
                    work.decode(inFile, gradedOutFile, key);
                } 
                workTime = System.nanoTime() - workStart;
            } catch (Throwable t) {
                workTime = -1;
            }

            
            System.out.print(work.getClass().getName() + " result: ");
            if (workTime > 0) {
                boolean correct = readAndCompareFiles(gradedOutFile, authorOutFile);
                if (correct) {
                    DecimalFormat df = new DecimalFormat("#.#####");
                    df.setRoundingMode(RoundingMode.HALF_UP);
                    double workTimeResultFraction = ((double) authorTime) / workTime;
                    results.add(workTimeResultFraction);
                    System.out.println(df.format(workTimeResultFraction) + " (time " + Math.round(workTime / 1000000.0)
                            + "ms)");
                } else {
                    results.add(0.0);
                    System.out.println("Incorrect. (time " + Math.round(workTime / 1000000.0)
                            + "ms)");
                }
            } else {
                results.add(0.0);
                System.out.println("Threw an exception.");
            }

        }

        return results;
    }

    public static void main(String[] args) throws Exception {
        Grader grader = new Grader();

        List<List<Double>> allResults = new ArrayList<List<Double>>(10);

        LinkedList<Character> key = generateRandomKey();
        System.out.println("\n\n=== Encoding in1.jpg ===");
        List<Double> result1 = grader.gradeAllAgainst("in1.jpg", key, true);
        File enc = new File("authorOut.enc");
        enc.renameTo(new File("encoded.enc"));
        System.out.println("\n\n=== Decoding in1.jpg ===");
        List<Double> result2 = grader.gradeAllAgainst("encoded.enc", key, false);

        key = generateRandomKey();
        System.out.println("\n\n=== Encoding in5.jpg ===");
        List<Double> result3 = grader.gradeAllAgainst("in5.jpg", key, true);

        allResults.add(result1);
        allResults.add(result2);
        allResults.add(result3);

        double finalResults[] = new double[classesToBeGraded.size()];

        for (List<Double> allResultsFromTest : allResults) {
            for (int i = 0; i < allResultsFromTest.size(); i++) {
                finalResults[i] += allResultsFromTest.get(i);
            }
        }

        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.HALF_UP);
        System.out.println("\n\n\n=== FINAL RESULTS ===");
        for (int i = 0; i < finalResults.length; i++) {
            finalResults[i] /= allResults.size();

            System.out.println(classesToBeGraded.get(i).getName() + ": " + df.format(finalResults[i]));
        }

    }

    private static LinkedList<Character> generateRandomKey() {
        LinkedList<Character> key = new LinkedList<Character>();
        for (int i = 0; i < 256; i++) {
            key.add((char) i);
        }

        Collections.shuffle(key);
        return key;
    }

    /**
     * Fetched from PatchedTestGen.java (some changes applied).
     * 
     * @param pathToCurrentFile
     * @param pathToExpectedFile
     * @return
     */
    private static boolean readAndCompareFiles(String pathToCurrentFile, String pathToExpectedFile) throws Exception {
        InputStream current = null;
        InputStream expected = null;
        try {
            current = new BufferedInputStream(new FileInputStream(pathToCurrentFile));
            expected = new BufferedInputStream(new FileInputStream(pathToExpectedFile)); 
            int currentChar;
            int expectedChar;
            while ((currentChar = current.read()) != -1 && (expectedChar = expected.read()) != -1) {

                if (currentChar != expectedChar) {
                	System.out.println("Mismatch: "+currentChar+ " "+expectedChar);
                    return false;
                }
            }
            if(current.read() != expected.read()){ System.out.println("size mismatch"); return false;}
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (current != null) {
                current.close();
            }
            if (expected != null) {
                expected.close();
            }
        }
        return true;
    }
}
