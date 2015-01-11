import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author Todor Zhelev
 */

public class BookIndexer61738 implements IBookIndexer 
{
    public static HashMap<String,String> wordMap;

    public static void main(String[] args) 
    {
        File book  = new File("files/book.txt");
        File index = new File("files/index.txt");
        
        String[] keywords = new String[] { "lorem", "quisque", "aenean" };
        
        BookIndexer61738 bookIndexer = new BookIndexer61738();
        
        long start = System.currentTimeMillis();
        
            bookIndexer.buildIndex(book.getAbsolutePath(),keywords,index.getAbsolutePath());
            
        StopTimer(start,"total time for building index ");
    }
    
    public static void GenerateWordMap(String bookFilePath)
    {
    	wordMap = new HashMap<String,String>();
    	
        Scanner scanner;
		try 
		{
			scanner = new Scanner(new FileReader(bookFilePath));
			
	        String delimiterRegex = "[^a-zA-Z0-9-=]";
	        
	        Integer currentPage = 0;
	        
	        while( scanner.hasNext() )
	        {
	            String str = scanner.nextLine();
	           
	            if( str.contains("===") )
	            {
	                String[] arr = str.split(delimiterRegex);
	
	                for( int i = 0; i < arr.length; i++)
	                {
	                    if( arr[i].compareTo("Page") == 0)
	                    {
	                        currentPage = Integer.parseInt(arr[i+1]);
	                        break;
	                    }
	                }
	            }
	            else
	            {
	                String[] arr = str.split(delimiterRegex);
	                
	                for (String tempStr : arr) 
	                {
	                    String lowerCase = tempStr.toLowerCase();
	                    
	                    if( wordMap.get(lowerCase) != null )
	                    {
	                        String value = wordMap.get(lowerCase);
	                        String strCurrentPage = currentPage.toString();
	                        if( !value.contains(strCurrentPage) )
	                        {
	                            value += ","+ currentPage.toString();
	                            wordMap.put(lowerCase, value);
	                        }
	                    }
	                    else
	                    {
	                        wordMap.put(lowerCase, currentPage.toString());
	                    }
	                }  
	            }
	        }
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
    }
    
    public static void StopTimer(long start, String message)
    {
        long end = System.currentTimeMillis();
        long diff = end - start;

        System.out.println(message + diff);
    }
    
    public String FormatPages(String pages)
    {
        String[] arr = pages.split(",");
        ArrayList<Integer> pagesArray = new ArrayList<Integer>();
        
        Integer prevPage = Integer.parseInt(arr[0]);
        pagesArray.add(prevPage);
        String finalString = "";
        String initComma = "";
        
        for( int i = 1; i < arr.length; i++ )
        {
            Integer currentPage = Integer.parseInt(arr[i]);
             
            if(currentPage == prevPage + 1)
            {
                pagesArray.add(currentPage);
            }
            else
            {
                //if prev page is 1 and current is 3 pagesArray will be 1 and we just print 1
                //as it is not sequence
                if( pagesArray.size() == 1)
                {
                    initComma = finalString.isEmpty() ? "" : ", ";
                    finalString += initComma + pagesArray.get(0);
                    pagesArray.clear();
                    pagesArray.add(currentPage);
                }
                else
                {
                    //if we have reached end of sequence, i.e. we have 1,2,3,7 and
                    //prev page is 3 and current is 7, we print the sequence in the needed format
                    initComma = finalString.isEmpty() ? "" : ", ";
                    finalString += initComma + pagesArray.get(0).toString() + "-" + pagesArray.get(pagesArray.size()-1).toString();
                    pagesArray.clear();
                    pagesArray.add(currentPage);
                }
            }
            
            prevPage = currentPage;
        }
        
        //if pagesArray is not empty output the pages
        if (!pagesArray.isEmpty()) 
        {
            initComma = finalString.isEmpty() ? "" : ", ";
            
            if (pagesArray.size() == 1) 
            {
                finalString += initComma + pagesArray.get(0);
            } 
            else 
            {
                finalString += initComma + pagesArray.get(0).toString() + "-" + pagesArray.get(pagesArray.size() - 1).toString();
            }
        }
        
        return finalString;
    }
    
    public void buildIndex(String bookFilePath, String[] keywords, String indexFilePath)
    {
        GenerateWordMap(bookFilePath);
        
        HashSet<String> keywordSet = new HashSet<String>();
        for( String temp : keywords)
        {
            keywordSet.add(temp.toLowerCase());
        }
       
        BufferedWriter writer;
		try 
		{
			writer = new BufferedWriter(new FileWriter(indexFilePath));	
            writer.write("INDEX");
            writer.newLine();
            
            for (String keyword : keywordSet) 
            {
                String pages;
                if( (pages = wordMap.get(keyword)) != null )
                {
                   writer.write(keyword + ", " + FormatPages(pages));
                   writer.newLine();     
                }
            }
            writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
    }
}

