import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class BookIndexer61708 implements BookIndexer {

	private Map<String, List<Integer>> index;
	private Map<Integer, String> pages;

	public BookIndexer61708() {
		this.index = new HashMap<String, List<Integer>>();
		this.pages = new HashMap<Integer, String>();
	}

	@Override
	public void buildIndex(String bookFilePath, String[] keywords, String indexFilePath) {
		this.getBookContent(bookFilePath);
		this.findKeywords(keywords);
		this.buildIndexOutput(indexFilePath);
	}

	private void getBookContent(String bookFilePath) {
		final String PAGE_START = "=== Page ";
		final int HEADING_LENGTH_PAGE_NUMBER = 9;

		try {
			FileReader in = new FileReader(bookFilePath);
			BufferedReader br = new BufferedReader(in);

			String currentPage = null;
			StringBuilder sb = new StringBuilder();
			int startOfPageHeadingIndex;
			String currentLine = null;
			int endOfPageHeading;
			while ((currentLine = br.readLine()) != null) {
				if ((startOfPageHeadingIndex = currentLine.indexOf(PAGE_START)) != -1) {
					endOfPageHeading = currentLine.indexOf("===",
							startOfPageHeadingIndex
									+ HEADING_LENGTH_PAGE_NUMBER);
					if (currentPage != null) {
						this.pages.put(Integer.parseInt(currentPage),
								sb.toString());
					}

					sb.setLength(0);
					currentPage = currentLine.substring(
							startOfPageHeadingIndex + 9, endOfPageHeading - 1);
				} else {
					sb.append(currentLine);
				}
			}

			this.pages.put(Integer.parseInt(currentPage), sb.toString());
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void findKeywords(String[] keywords) {
		for (Entry<Integer, String> page : pages.entrySet()) {
			String text = page.getValue();
			for (int i = 0; i < keywords.length; i++) {
				String currentWord = keywords[i];
				String currentWordUpper = currentWord.substring(0, 1)
						.toUpperCase() + currentWord.substring(1);

				String regexInText = String.format(
						"[^a-zA-Z0-9-]+(%s|%s)[^a-zA-Z0-9-]+",
						currentWordUpper, currentWord);
				String regexStartOfText = String.format("%s[^a-zA-Z0-9-]+",
						currentWordUpper);

				boolean isFoundAtStart = Pattern.compile(regexStartOfText)
						.matcher(text).find(0);
				boolean isFoundInText = false;
				if (!isFoundAtStart) {
					isFoundInText = Pattern.compile(regexInText).matcher(text)
							.find();
				}

				if (isFoundInText || isFoundAtStart) {
					this.addKeywordMatch(currentWord, page.getKey());
				}
			}
		}
	}

	private void addKeywordMatch(String key, int pageNumber) {
		List<Integer> list;
		if (this.index.containsKey(key)) {
			list = this.index.get(key);
			list.add(pageNumber);
		} else {
			list = new ArrayList<Integer>();
			list.add(pageNumber);
			this.index.put(key, list);
		}
	}

	private void buildIndexOutput(String outputFile) {
		PrintWriter out = null;
		StringBuilder sb = new StringBuilder();
		try {
			
			File file = new File(outputFile);
			if(!file.exists()){
				file.createNewFile();
			}
			
			out = new PrintWriter(outputFile);
			List<String> keys = new ArrayList<String>(this.index.keySet());
			Collections.sort(keys);
			for (int i = 0; i < keys.size(); i++) {
				String currentKey = keys.get(i);
				sb.append(currentKey);
				String matchedPages = this.orderPageMatches(this.index
						.get(currentKey));
				sb.append(matchedPages + "\r\n");
			}

			out.println("INDEX");
			out.print(sb.toString().trim());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		finally{
			out.close();
		}
	}

	private String orderPageMatches(List<Integer> pagesContaingKeyword) {
		StringBuilder result = new StringBuilder();
		int startOfSequnce = 0;
		int sequenceLength = 1;
		int endOfSequence = 0;
		if (pagesContaingKeyword.size() == 1) {
			result.append(", " + pagesContaingKeyword.get(0));
		} else {
			for (int i = 0; i < pagesContaingKeyword.size(); i++) {
				int currentPage = pagesContaingKeyword.get(i);
				startOfSequnce = currentPage;
				for (int j = i + 1; j < pagesContaingKeyword.size(); j++) {
					int nextPage = pagesContaingKeyword.get(j);
					if (currentPage == nextPage - 1) {
						endOfSequence = nextPage;
						sequenceLength++;
						if (j == pagesContaingKeyword.size() - 1) {
							result.append(", " + startOfSequnce + "-"
									+ endOfSequence);
							i += sequenceLength - 1;
							break;
						}
						currentPage = nextPage;
					} else if (sequenceLength != 1) {
						result.append(", " + startOfSequnce + "-"
								+ endOfSequence);
						if (j == pagesContaingKeyword.size() - 1) {
							result.append(", " + pagesContaingKeyword.get(j));
						}
						i += sequenceLength - 1;
						sequenceLength = 1;
						break;
					} else if (i == (pagesContaingKeyword.size() - 2)
							&& sequenceLength == 1) {
						result.append(", " + startOfSequnce);
						result.append(", " + pagesContaingKeyword.get(i + 1));
						break;
					} else {
						result.append(", " + startOfSequnce);
						break;
					}
				}
			}
		}

		return result.toString();
	}
}
