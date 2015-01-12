package bookindexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class BookIndexer61744 implements IBookIndexer {

    @Override
    public void buildIndex(String bookFilePath, String[] keywords, String indexFilePath) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(bookFilePath));

            Map<String, String> keywordsMap = new HashMap<>();
            String currentLine = "";
            String currentPage = "";

            while ((currentLine = br.readLine()) != null) {
                if (currentLine.matches("=== Page [0-9]* ===")) {
                    currentPage = currentLine.replaceAll("\\D+", "");
                } else if (!currentLine.equals("")) {
                    String[] words = currentLine.replaceAll("[^a-zA-z0-9\\-]", " ").toLowerCase().split("\\s+");
                    List<String> wordsList = new ArrayList<>(Arrays.asList(words));
                    int curPage = Integer.parseInt(currentPage);
                    int next = curPage + 1;
                    String nextPage = Integer.toString(next);

                    for (String keyword : keywords) {
                        if (wordsList.contains(keyword)) {
                            if (keywordsMap.get(keyword) != null) {
                                if (keywordsMap.get(keyword).contains(currentPage) && !keywordsMap.get(keyword).contains(nextPage)) {
                                    keywordsMap.put(keyword, String.format("%d-%d", curPage, next));
                                } else if (!keywordsMap.get(keyword).contains(nextPage)) {
                                    keywordsMap.put(keyword, String.format("%s, %d", keywordsMap.get(keyword), next));
                                }                                   
                            } else {
                                keywordsMap.put(keyword, currentPage);
                            } 
                        }
                    }
                }
            }

            br.close();
            writeToIndexFile(indexFilePath, keywordsMap);

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void writeToIndexFile(String indexFilePath, Map<String, String> keywordsMap) {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(indexFilePath));
            bw.write("INDEX");
            bw.newLine();

            for (String keyword : keywordsMap.keySet()) {
                bw.write(keyword + ", " + keywordsMap.get(keyword));
                bw.newLine();
            }

            bw.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
