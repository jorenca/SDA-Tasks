import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/*
 * @author Bat Georgi
 */

public class BookIndexer61712 implements IBookIndexer {
	private static final String DELIMITERS_STR = " !#$%&()*+,./:;<>?@[]^_`{|}~\\\"\'";
	private static final String IDX_TITLE = "INDEX";
	private static final String PAGE_START = "===";
	private static final String COMMA_SPACE = ", ";
	private static final Integer PAGE_ST_IDX = 9;
	private static final Character SPACE = ' ';
	
	private HashSet<Character> delimiters;
	private TreeMap<String, TreeSet<Integer>> indices;
	private HashSet<String> keywords;
	private Integer currentPage;
	
	public BookIndexer61712() {
		this.indices = new TreeMap<String, TreeSet<Integer>>();
		this.keywords = new HashSet<String>();
		this.delimiters = new HashSet<Character>();
		
		this.generateDelimiters();
	}
	
	public void buildIndex(String bookFilePath, String[] keywords, String indexFilePath) {
		this.addKeywords(keywords);
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(bookFilePath));
			
		    try {
		        String line = reader.readLine();

		        while (line != null && line != "") {
		        	this.analyzeLine(line);
		            line = reader.readLine();
		        }
		    } finally {
		        reader.close();
		    }
		}
		catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		
		this.saveIndicesToFile(indexFilePath);
	}
	
	private void generateDelimiters() {
		for (int i = 0; i < this.DELIMITERS_STR.length(); i++) {
			this.delimiters.add(this.DELIMITERS_STR.charAt(i));
		}
	}
	
	private void addKeywords(String[] keywords) {
		for (String word : keywords) {
			this.keywords.add(word.toLowerCase());
		}
	}
	
	private void analyzeLine(String line) {
		StringBuilder word = new StringBuilder();
		
		for (int i = 0; i < line.length(); i++) {
			char currentChar = line.charAt(i);
			
			if (!this.delimiters.contains(currentChar)) {
				word.append(currentChar);
				
				if (i == line.length() - 1) {
					String strWord = word.toString().toLowerCase();
					this.addWord(strWord);
				}
			}
			else {
				String strWord = word.toString().toLowerCase();
				Boolean isWordAdded = this.addWord(strWord);
				
				if (!isWordAdded && strWord.contains(this.PAGE_START)) {
					StringBuilder page = new StringBuilder();
					int startIdx = strWord.indexOf(this.PAGE_START) + this.PAGE_ST_IDX;
					
					for (int j = startIdx; j < line.length(); j++) {
						char digit = line.charAt(j);
						
						if (digit == this.SPACE) {
							break;
						}
						
						page.append(digit);
					}

					this.currentPage = Integer.parseInt(page.toString());
					
					break;
				}
				
				word.setLength(0);
			}
		}
	}
	
	private Boolean addWord(String strWord) {
		if (this.keywords.contains(strWord)) {
			if (this.indices.containsKey(strWord)) {
				this.indices.get(strWord).add(this.currentPage);
			}
			else {
				TreeSet<Integer> pages = new TreeSet<Integer>();
				pages.add(this.currentPage);

				this.indices.put(strWord, pages);
			}
			
			return true;
		}
		
		return false;
	}
	
	private StringBuilder generateFileOutput() {
		StringBuilder fileOutput = new StringBuilder();
		fileOutput.append(this.IDX_TITLE).append(System.lineSeparator());
		
		for (Map.Entry<String, TreeSet<Integer>> item : this.indices.entrySet()) {
			fileOutput.append(item.getKey()).append(this.COMMA_SPACE);
			
			int from = -1;
			int prev = -1;	
			int pagesLength = item.getValue().size();
			int i = 0;

			for (int page : item.getValue()) {
				if (from == -1) {
					from = page;
				}
				else if (prev != page - 1) {
					if (prev - from >= 1) {
						fileOutput.append(from).append('-');
					}
					
					fileOutput.append(prev).append(this.COMMA_SPACE);
					
					from = page;
				}

				if (i == pagesLength - 1) {
					if (page - from >= 1) {
						fileOutput.append(from).append('-');
					}
					
					fileOutput.append(page);
					
					break;
				}

				prev = page;
				i++;
			}
			
			fileOutput.append(System.lineSeparator());
		}
		
		return fileOutput;
	}
	
	private void saveIndicesToFile(String path) {
		File file = new File(path);
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
		    writer.append(this.generateFileOutput());
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		BookIndexer61712 indexer = new BookIndexer61712();
		indexer.buildIndex("src\\book4.txt",  new String[] { "miracle" }, "src\\index8.txt");
		System.out.println("Done");
	}
}
