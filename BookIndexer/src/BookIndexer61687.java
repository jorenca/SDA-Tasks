import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookIndexer61687 implements BookIndexer {
	public final static Pattern pat = Pattern.compile("^=== page (\\d+) ===$");
	
	public void buildIndex(String bookFilePath, String[] keywords, String indexFilePath) {	
		try {
			int page;
			TreeMap<String, ArrayList<Integer>> map = new TreeMap<String, ArrayList<Integer>>();		
			Stack<Integer> stackOfPages = new Stack<Integer>();
			ArrayList<Integer> targetList = new ArrayList<Integer>();
			List<String> lines = Files.readAllLines(Paths.get(bookFilePath), Charset.defaultCharset());
			            for (String line : lines) {
			                line = line.toLowerCase();
			                page = getPage(line);
			        		
			        		if (page != 0) {
			        			stackOfPages.add(page);
			        		}
			        		
			        		if (page == 0 && !(line.isEmpty())) {
				                for(int i = 0; i < keywords.length; i++) {
				                	targetList = new ArrayList<Integer>();
					                	if(containsWord(line,keywords[i])) {
					                		if (map.get(keywords[i]) != null) {
					                			targetList = map.get(keywords[i]);
					                		}
					                		
					                		int curentPage = stackOfPages.peek();
					                		
					                		if(!(targetList.contains(curentPage))) {
					                			targetList.add(curentPage);	
					                		}
						                    map.put(keywords[i], targetList);
						                    targetList = new ArrayList<Integer>();
					                    }
				                }
				                
			        		}
			            }
			            
			            printPages(map, keywords, indexFilePath);
			            
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
							e.printStackTrace();
					}
	}
	
	private Integer getPage(String line) {
		int result = 0;
		if(!(line.isEmpty())) {
		    Matcher m = pat.matcher(line);
		    String page = null;
		        
		    if(m.find()) { 
		    	page = m.group(1);
		    }
		    
		    if (page != null) {
		    	result = Integer.parseInt(page);
		    }
		}
	     return result;
	}
		
	private void printPages(TreeMap<String, ArrayList<Integer>> map, String[] keywords,String indexFilePath) {
			String result = "INDEX\r\n";
			String pages = "";
			int counter;
			Writer writer = null;
			
			if(!(map.isEmpty())) {
				Arrays.sort(keywords);
				for(int i = 0; i < keywords.length; i++) {
					counter = 0;
					keywords[i].toLowerCase();
					ArrayList<Integer> listOfPages = map.get(keywords[i]);
						if(listOfPages != null) {
							for(int j = 0; j < listOfPages.size(); j++) {
									
								if(j == listOfPages.size() -1) { 
									if(counter != 0) {
										pages = pages + ", " + listOfPages.get(j-counter) + "-" + listOfPages.get(j);
									} else {
										pages = pages + ", " + listOfPages.get(j);
									}
										continue;
								}
									
								if (listOfPages.get(j+1) == listOfPages.get(j) + 1) {
									counter++;
								} else {
									if(counter != 0) {
										pages = pages + ", " + listOfPages.get(j-counter) + "-" + listOfPages.get(j);
									} else {
										pages = pages + ", " + listOfPages.get(j);
									}
								}
							}
							result = result + keywords[i] + pages + "\r\n";
							pages = "";
						}
						
				}	
			} 
			try {
			    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indexFilePath), "utf-8"));
			    writer.write(result);
			} catch (IOException ex) {
				} finally {
				   try {
					   writer.close();
				   } catch (Exception ex) {}
				}
	}

	private boolean containsWord(String line, String keyword) {
		line = " " + line;
		Pattern p = Pattern.compile("[^A-Za-z0-9-]"+keyword+"[^A-Za-z0-9-]");
		Matcher matcher = p.matcher(line);
		if(matcher.find()) {
			return true;
		}
		return false;
	}

}
