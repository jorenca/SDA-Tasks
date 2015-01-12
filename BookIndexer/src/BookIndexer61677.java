import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Rositsa Zlateva
 */

public class BookIndexer61677 implements IBookIndexer {

	public void buildIndex(String bookFilePath, String[] keywords,
			String indexFilePath) {
		HashMap<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
		Path fileToRead = Paths.get(bookFilePath);

		if (Files.isReadable(fileToRead)) {

			try {
				BufferedReader reader = Files.newBufferedReader(fileToRead,
						Charset.defaultCharset());

				String line;
				String searchNumOfPage = "=== Page ";
				String numberOfPage = "";
				while ((line = reader.readLine()) != null) {
					boolean found;
					found = line.contains(searchNumOfPage);
					if (found) {
						String[] array = line.split(" ");
						numberOfPage = array[2];
						continue;
					}
					String[] wordArray = line.toLowerCase().split(
							"[^a-z0-9\\-]\\s*");

					ArrayList<String> list = new ArrayList<>(
							Arrays.asList(wordArray));
					for (String word : keywords) {
						if (list.contains(word)) {
							ArrayList<Integer> Pages = new ArrayList<>();
							if (map.get(word) != null) {
								Pages.addAll(map.get(word));
								Pages.add(Integer.parseInt(numberOfPage));
							} else {
								Pages.add(Integer.parseInt(numberOfPage));
							}
							map.put(word, Pages);
						}
					}

				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List<Entry<String, ArrayList<Integer>>> listOfEntries = new LinkedList<Map.Entry<String, ArrayList<Integer>>>(
				map.entrySet());

		Collections.sort(listOfEntries,
				new Comparator<Map.Entry<String, ArrayList<Integer>>>() {
					public int compare(
							Map.Entry<String, ArrayList<Integer>> o1,
							Map.Entry<String, ArrayList<Integer>> o2) {
						return (o1.getKey().toLowerCase()).compareTo(o2
								.getKey().toLowerCase());
					}

				});

		for (Entry<String, ArrayList<Integer>> entry : listOfEntries) {
			String resultPages = new String();
			Integer pages[] = new Integer[entry.getValue().size()];
			pages = entry.getValue().toArray(pages);
			for (int i = 0; i < pages.length; i++) {
				boolean isSequence = false;
				if (i <= pages.length - 3) {
					if (pages.length < 3) {
						isSequence = false;
					} else {
						isSequence = pages[i].intValue() + 2 == pages[i + 2]
								.intValue();
					}
				}
				if (isSequence) {
					int firstMemberOfSequence = pages[i];
					int lastMemberOfSequence = 0;
					while (pages[i + 1] == pages[i] + 1) {
						lastMemberOfSequence = pages[i + 1];
						i++;
						if (i == pages.length - 1)
							break;

					}
					resultPages = resultPages + ", " + firstMemberOfSequence
							+ "-" + lastMemberOfSequence;
				} else {
					if (resultPages.isEmpty()) {
						resultPages = pages[i].toString();
					} else {
						if (resultPages.toCharArray()[resultPages.length() - 1] == ',')
							resultPages = resultPages + pages[i];
						else
							resultPages = resultPages + ", " + pages[i];
					}
				}

			}
			if (resultPages.toCharArray()[0] == ',') {
				resultPages = resultPages.substring(1, resultPages.length());

			}

			BufferedWriter writer = null;
			try {
				File file = new File(indexFilePath);
				file.createNewFile();
				writer = new BufferedWriter(new FileWriter(file));
				writer.write("INDEX\r\n");
				for (String key : map.keySet()) {
					writer.write(key + ", " + resultPages);
					writer.newLine();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					writer.close();
				} catch (Exception ex) {
				}
			}

		}
	}
}

