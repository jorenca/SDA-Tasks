import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Kancho Kanev
 */

public class BookIndexer61692 implements IBookIndexer{

	private Map<String, LinkedList<Integer>> words = new HashMap<String, LinkedList<Integer>>();
	
	private void fillWordMap(String word, int page){
		if(words.get(word.toLowerCase()) != null){
			LinkedList<Integer> currentList = words.get(word.toLowerCase());
			currentList.add(page);
			words.put(word.toLowerCase(), currentList);
		} else {
			LinkedList<Integer> newList = new LinkedList<Integer>();
			newList.add(page);
			words.put(word.toLowerCase(), newList);
		}
	}

	public void buildIndex(String bookFilePath, String[] keywords, String indexFilePath){
		
		ArrayList<String> sortedKeywords = new ArrayList<String>(Arrays.asList(keywords));
		Collections.sort(sortedKeywords, String.CASE_INSENSITIVE_ORDER);
		
		Path path = Paths.get(bookFilePath);
		Scanner scanner = null;
		try {
			scanner = new Scanner(path);
			scanner.useDelimiter("[^=A-Za-z0-9-]");
			
			int currentPage = 0;
			String currentWord;
			String nextWord;
			
			while(scanner.hasNext()){
				currentWord = scanner.next();
				if(currentWord.equals("===")){
					nextWord = scanner.next();
					if(nextWord.equals("Page")){
						currentPage = Integer.parseInt(scanner.next());
						currentWord = scanner.next();
					} else {
						fillWordMap(currentWord, currentPage);
						fillWordMap(nextWord, currentPage);	
					}
				} else {
					fillWordMap(currentWord, currentPage);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		
		FileWriter fileWriter = null;
		try{
			fileWriter = new FileWriter(indexFilePath);
			fileWriter.write("INDEX" + System.getProperty("line.separator"));
			
			for(int i = 0; i < sortedKeywords.size(); i++){
				if(words.get(sortedKeywords.get(i).toLowerCase()) != null){
					String pagesString = "";
					ArrayList<Integer> listOfPages = new ArrayList<Integer>(words.get(sortedKeywords.get(i).toLowerCase()));
					
//					Is it necessary sort the list, because the pages in it are added in order?
//					Collections.sort(listOfPages);
					
					int startPage = 0;
					int endPage = 0;
					
					for(int j = 0; j < listOfPages.size(); j++){
						if (listOfPages.get(j) != endPage) {
							if(startPage == 0){
								startPage = listOfPages.get(j);
								endPage = listOfPages.get(j);
							} else {
								if(listOfPages.get(j) == endPage + 1){
									endPage = listOfPages.get(j);
								} else {
									if(startPage == endPage){
										pagesString += String.valueOf(startPage) + ", ";
										startPage = listOfPages.get(j);
										endPage = listOfPages.get(j);
									} else {
										pagesString += String.valueOf(startPage) + "-" + String.valueOf(endPage) + ", ";
										startPage = listOfPages.get(j);
										endPage = listOfPages.get(j);
									}
								}
							}
						}
					}
					
					if(startPage == endPage){
						pagesString += String.valueOf(startPage) + System.getProperty("line.separator");
					} else {
						pagesString += String.valueOf(startPage) + "-" + String.valueOf(endPage) + System.getProperty("line.separator");
					}
					
					fileWriter.write(sortedKeywords.get(i) + ", " + pagesString);
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
