

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

/**
 *
 * @author B.Dinchev
 */
public class BookIndexer61682 implements IBookIndexer {

    @Override
    public void buildIndex(String bookFilePath, String[] keywords, String indexFilePath) {
        try (BufferedReader book = new BufferedReader(new FileReader(bookFilePath))) {
            String line;
            String newValue;
            Integer page = 1;
            TreeMap<String, String> map = new TreeMap<>();
            while ((line = book.readLine()) != null) {

                if (line.matches(".*===.*===.*")) {
                    page = Integer.parseInt(line.replaceAll("\\D+", ""));

                } else if (!line.equals("")) {
                    line = line.toLowerCase();
                    Integer previous = page - 1;
                    String previousPage = previous.toString();
                    for (String keyword : keywords) {

                        if (line.replaceAll("[^a-zA-Z0-9\\- ]", "").matches("[\\s*\\w*]*" + keyword + "[\\s*\\w*]*")) {

                            if (map.get(keyword) == null) {

                                map.put(keyword, page.toString());

                            } else {

                                if (map.get(keyword).contains(previousPage) && !map.get(keyword).contains(page.toString())) {

                                    if (map.get(keyword).matches(".*\\-" + previousPage)) {

                                        newValue = map.get(keyword).replace("-" + previousPage, "-" + page);
                                        map.put(keyword, newValue);

                                    } else {

                                        newValue = map.get(keyword).replace(previousPage, previousPage + "-" + page);
                                        map.put(keyword, newValue);

                                    }
                                } else if (!map.get(keyword).contains(page.toString())) {

                                    newValue = map.get(keyword) + String.format(", %d", page);
                                    map.put(keyword, newValue);

                                }
                            }

                        }

                    }

                }
            }

            book.close();
            write(indexFilePath, map);

        } catch (IOException e) {

        }

    }

    private void write(String path, TreeMap<String, String> map) {
        try (BufferedWriter index = new BufferedWriter(new FileWriter(path))) {
            index.write("INDEX");
            for (String key : map.keySet()) {
                index.newLine();
                index.write(key + ", " + map.get(key));
            }
            index.close();
        } catch (IOException e) {

        }
    }
}
