package fileindexerr;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Alex Kushev
 */
public class BookIndexer61718 implements IBookIndexer {

    @Override
    public void buildIndex(String bookFilePath, String[] keywords, String indexFilePath) {

        Set<String> keyWords = new TreeSet<>(Arrays.asList(keywords));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(indexFilePath));
                BufferedReader reader = new BufferedReader(new FileReader(bookFilePath))) {

            writer.write("INDEX");

            String curLine = null;
            String curPage = null;

            String[] pages = new String[keywords.length];
            int t = 0;
            for (String s : keyWords) {

                pages[t] = s + ", ";
                t++;
            }

            while ((curLine = reader.readLine()) != null) {
                int i = 0;
                if (curLine.matches("=== Page [0-9]* ===")) {
                    curPage = curLine.replaceAll("\\D", "");
                } else {
                    if (!curLine.equals("")) {

                        String[] words = curLine.replaceAll("[^a-zA-Z0-9\\-]", " ").toLowerCase().split("\\s+");
                        List<String> word = new ArrayList<>(Arrays.asList(words));

                        for (String w : keyWords) {

                            if (word.contains(w)) {

                                pages[i] += curPage + ",";

                            }
                            i++;

                        }

                    }
                }
            }

            for (String pg : pages) {

                String[] pagesToArray = pg.split(",");

                try {

                    int[] pagesArray = new int[pagesToArray.length + 2];
                    pagesToArray[1] = pagesToArray[1].trim();
                    if (pagesToArray[1].equals("")) {

                    } else {
                        StringBuilder toPrint = new StringBuilder();
                        toPrint.append(pagesToArray[0]).append(", ");
                        int z = 0;
                        int index = 1;
                        for (int i = 0; i < pagesToArray.length - 1; i++) {
                            pagesArray[i] = Integer.parseInt(pagesToArray[index]);
                            index++;
                            z = i;
                        }

                        pagesArray[z + 1] = 0;
                        pagesArray[z + 2] = 0;

                        for (int i = 0; i < pagesArray.length - 1; i++) {
                            if (pagesArray[i] + 1 == pagesArray[i + 1]) {
                                toPrint.append(pagesArray[i]).append("-");
                                int k = i;
                                while (pagesArray[k] + 1 == pagesArray[k + 1] && k < pagesArray.length - 2) {
                                    k++;

                                }
                                if (pagesArray[k + 1] != 0) {
                                    toPrint.append(pagesArray[k]).append(", ");
                                } else {
                                    toPrint.append(pagesArray[k]);
                                }

                                i = k;
                            } else {
                                if (pagesArray[i] != 0) {
                                    if (pagesArray[i + 1] != 0) {
                                        toPrint.append(pagesArray[i]).append(", ");
                                    } else {
                                        toPrint.append(pagesArray[i]);
                                    }

                                }
                            }
                        }

                        writer.newLine();

                        writer.write(String.valueOf(toPrint));

                    }

                } catch (NumberFormatException ex) {
                    ex.getStackTrace();

                }

            }

            writer.flush();

        } catch (IOException ex) {

            ex.getStackTrace();
        }

    }
}
