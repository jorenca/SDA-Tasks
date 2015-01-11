import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class BookIndexer61707 implements IBookIndexer {

	@Override
	public void buildIndex(String bookFilePath, String[] keywords, String indexFilePath) {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(bookFilePath))) {

			String line;
			String currentPage = "";
			Map<String, String> wordFrequency = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

			while ((line = bufferedReader.readLine()) != null) {

				String currentLine = line.trim();

				if (currentLine.equals("")) {
					continue;
				}

				if (currentLine.startsWith("=== Page")) {
					currentPage = currentLine.replaceAll("\\D+", "");
//					currentPage = currentLine.split(" ")[2];
					continue;
				}

				String[] words = currentLine.replaceAll("[^a-zA-Z0-9\\- ]", " ").toLowerCase().split("\\s+");
				ArrayList<String> wordList = new ArrayList<>(Arrays.asList(words));

				for (String word : keywords) {

					if (!wordList.contains(word.toLowerCase())) {
						continue;
					}

					if (wordFrequency.containsKey(word)) {

						String elementPages = wordFrequency.get(word);
						int elementPagesLength = elementPages.length();
						int previousPage = Character.getNumericValue(elementPages.charAt(elementPagesLength - 1));
						int currentPageNumber = Integer.parseInt(currentPage);

						if (previousPage == currentPageNumber) {
							continue;
						} else if (previousPage + 1 == currentPageNumber) {

							if (elementPagesLength > 2 && elementPages.indexOf('-') == elementPagesLength - 2) {
								wordFrequency.put(word, elementPages.substring(0, elementPagesLength - 1) + currentPage);
							} else {
								wordFrequency.put(word, elementPages + "-" + currentPage);
							}

						} else {
							wordFrequency.put(word, elementPages + ", " + currentPage);
						}

					} else {
						wordFrequency.put(word, currentPage);
					}
				}
			}

			writeToFile(indexFilePath, wordFrequency);

		} catch (FileNotFoundException fnfe) {
			System.err.println("Cannot open the file " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.err.printf("Error when processing file; Exiting..");
		}
	}

	private void writeToFile(String indexFilePath, Map<String, String> wordFrequency) {

		try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(indexFilePath))) {

			String ENDL = System.getProperty("line.separator");
			byte[] ENDLB = ENDL.getBytes();

			bufferedOutputStream.write("INDEX".getBytes());
			bufferedOutputStream.write(ENDLB);

			for (Entry<String, String> entry : wordFrequency.entrySet()) {
				String indexedWord = entry.getKey() + ", " + entry.getValue();

				bufferedOutputStream.write(indexedWord.getBytes());
				bufferedOutputStream.write(ENDLB);
			}

		} catch (FileNotFoundException fnfe) {
			System.err.println("Cannot open the file " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.err.printf("Error when processing file; Exiting..");
		}

	}
}
