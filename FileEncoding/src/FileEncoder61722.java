import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;


public class FileEncoder61722 implements FileEncoder{
	 
	    public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) throws IOException
	    {
	    	int ascii=0;
	    	InputStream inPut=new FileInputStream(sourceFile);
	    	OutputStream outPut=new FileOutputStream(destinationFile);
	    	int j=0;
	    	while((ascii=inPut.read())!=-1){
				if(isPrime(j)==true)
				{
					outPut.write(ascii);
				}else
				{
					outPut.write(key.get(ascii));
				}
				j++;
			}
	    	inPut.close();
	    	outPut.close();
	    }

		public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) throws IOException
	    {
			int index=0;
			InputStream in=new FileInputStream(encodedFile);
	    	OutputStream out=new FileOutputStream(destinationFile);
	    	int i=0;
	    	while((index=in.read())!=1){
				if(isPrime(i)==true)
				{
					out.write(index);
				}else
				{
					out.write((char)key.indexOf(index));
				}
				i++;
			}
	    	in.close();
	    	out.close();
	    	
	    }
		
		 public boolean isPrime(int num) {
			 	if(num==0)
			 	{
			 		return false;
			 	}
			 	if(num==1 || num==2)
			 	{
			 		return true;
			 	}
		        if (num % 2 == 0)
		            return false;
		        for (int i = 3; i * i <= num; i += 2)
		            if (num % i == 0) return false;
		        return true;
		  }  
	}

