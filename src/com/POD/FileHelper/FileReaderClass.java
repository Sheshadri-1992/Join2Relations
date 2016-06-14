package com.POD.FileHelper;



import java.io.*;

public class FileReaderClass {

	public String fileName;
	public FileReader myFileReader;
	public BufferedReader buff_reader;
	public String line;
	public boolean active=true;
	
	public FileReaderClass(String fileName_arg)
	{
		fileName=fileName_arg;
	}
	
	public void openFile()
	{
		try
		{
			myFileReader= new FileReader(fileName);
			buff_reader = new BufferedReader(myFileReader);			
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File Not found in class: FileReader");
		}
//		catch(IOException e)
//		{
//			System.out.println("IO exception Unable to read in class: FileReader");
//		}
		
	}
	
	public String readFile()
	{
		
		try
		{
			line = buff_reader.readLine();	
			if(line==null)
			{
				active=false;
			}
				
		}
		catch(IOException e)
		{
			System.out.println("Exception in readFile class: FileReader");
		}
		
		return line;
	}
	
	public void closeFile()
	{
		try
		{
			buff_reader.close();
			myFileReader.close();
		}
		catch(Exception e)
		{
			System.out.println("File close function in class: FileReader");
		}
	}
}
