import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;


public class FileEncoder61657 {
	ArrayList<Boolean> primes = new ArrayList<Boolean>();
	FileEncoder61657 ()
	{
		for (int i = 0; i < 300000; i++) 
		{
			primes.add(true);
		}
		primes.set(0, false);
		primes.set(1,true);
		for (int i=2; i<Math.sqrt(300000);i++)
		{
			if (primes.get(i)==true)
			{
				for (int j = i; j * i < 300000; j++) 
				{
					primes.set(j*i, false);
			    }
			}
		}
	}
	public void encode(String sourceFile, String destinationFile, LinkedList<Character> key) throws IOException {
		InputStream begin=new FileInputStream (sourceFile);
		OutputStream finito=new FileOutputStream (destinationFile);
			int a,i=0;
			while((a=begin.read())!=-1)
			{
			if (!IsPrime(i)) 
			{
				finito.write(key.get(a));
			}
			else 
			{
				finito.write(a);
			}}
			begin.close();
			finito.close();
		}
	  
	public void decode(String encodedFile, String destinationFile, LinkedList<Character> key) throws IOException
	{
		InputStream begin=new FileInputStream (encodedFile);
		OutputStream finito=new FileOutputStream (destinationFile);
		int a,i=0;
		while((a=begin.read())!=-1)
		{
			if (!IsPrime(i))	
			{
				finito.write(key.indexOf(a));
			}
			else finito.write(a);
		}
		begin.close();
		finito.close();
	}
	
	public boolean IsPrime(int x)
	{
		if (primes.get(x)) return true;
		else return false;
	}
}
