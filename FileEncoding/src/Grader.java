import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

        File folder = new File("./src/");
        File[] listOfFiles = folder.listFiles();
        Pattern homeworkPattern = Pattern.compile("FileEncoder(\\d){5}.java");

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String fileName = file.getName();
                Matcher matcher = homeworkPattern.matcher(fileName);
                if (matcher.find()) {
                    String className = fileName.replaceFirst(".java", "");
                    System.out.println("Adding class " + className);
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

    public List<Double> gradeAllAgainst(String inFile, LinkedList<Character> key) throws Exception {
        String authorOutFile = "authorOut.enc";
        String gradedOutFile = "out.enc";

        long authorStart = System.nanoTime();
        authorEncoder.encode(inFile, authorOutFile, key);
        long authorEnd = System.nanoTime();
        long authorTime = authorEnd - authorStart;
        System.out.println("Author result: " + authorTime + "ns");

        List<Double> results = new LinkedList<Double>();
        for (FileEncoder work : instancesToBeGraded) {
            long workStart = System.nanoTime();
            work.encode(inFile, gradedOutFile, key);
            long workEnd = System.nanoTime();
            long workTime = workEnd - workStart;

            boolean correct = readAndCompareFiles(gradedOutFile, authorOutFile);
            System.out.print(work.getClass().getName() + " result: ");
            if (correct) {
                results.add(((double) authorTime) / workTime);
                System.out.println(((double) authorTime) / workTime + " (time " + workTime + "ns)");
            } else {
                results.add(0.0);
                System.out.println("Incorrect.");
            }
        }

        return results;
    }

    public static void main(String[] args) throws Exception {
        // WORK IN PROGRESS
        Grader grader = new Grader();

        LinkedList<Character> key = generateRandomKey();
        List<Double> results = grader.gradeAllAgainst("in2.RAR", key);

        for (int i = 0; i < results.size(); i++) {
            // System.out.println(classesToBeGraded.get(i).getName() + ": score " + results.get(i));
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
                    return false;
                }
            }
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
