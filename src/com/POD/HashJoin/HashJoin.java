package com.POD.HashJoin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.POD.FileHelper.FileReaderClass;
import com.POD.FileHelper.FileWriterClass;
import com.POD.Main.Constants;

public class HashJoin {

	public static String relationName;
	public static int M;
	public static int blockSize;
	public static int relationType;
	public static int totalLinesRelation;
	public static int noOfTimesToLoad;
	public static FileReaderClass myReader;
	public static int position;
	public static String intermediateFileName;
	public static FileWriterClass[] fileWriter;
	
	static List<String> lines;
	
	public HashJoin(String relationNameArgs,int MArgs,int blockSizeArgs,HashMap<String,Integer> relationNumberArgs)
	{
		
		totalLinesRelation =0;
		noOfTimesToLoad = 0;
		relationName = relationNameArgs;
		M = MArgs;
		lines = new ArrayList<>();
		blockSize = blockSizeArgs;
		fileWriter = new FileWriterClass[M-1];		
		relationType = (int)relationNumberArgs.get(relationNameArgs);
		
	}
	
	public void calcuateNoOfLinesToRead()
	{
		FileReaderClass myFileReader = new FileReaderClass(relationName);
//		System.out.println("RelationName is "+relationName);
		myFileReader.openFile();
		
		try {
			String myLine = myFileReader.buff_reader.readLine();
			while(myLine!=null)
			{
				totalLinesRelation++;
				myLine = myFileReader.buff_reader.readLine();
			}
			myFileReader.closeFile();
		}
		
		catch (IOException e) {
			System.out.println("Exception caught in SortBigRelatoins: calculateNoOfLinesToRead");
		}				
//		System.out.println("Number of lines are "+totalLinesRelation);
	}
	
	
	public  void computeNoOfTimeToLoad()
	{
		double result = ( (double)1.0*totalLinesRelation/(double)(blockSize*1.0));
		noOfTimesToLoad =(int) Math.ceil(result);		
	}
	
	
	public void createHashedSublists()
	{
		calculatePosition();
		createHashedFiles();
		
		myReader = new FileReaderClass(relationName);
		myReader.openFile();
		
		for(int i=0;i<noOfTimesToLoad;i++)
		{
			readLines();
			hashAndWrite();
			lines.clear();
		}	
		
		myReader.closeFile();
		closeHashedFiles();
	}
	
	
	
	
	public static void hashAndWrite()
	{
		String line;
		String operand;
		int hashIndex;
		
		for(int i=0;i<lines.size();i++)
		{
			line = lines.get(i);
			String[] myArray = line.split(",");
			operand = myArray[position];
//			int hashCode = operand.hashCode();
			
			
			hashIndex = Math.abs(operand.hashCode())%(M-1);
			fileWriter[hashIndex].writeline(line);
		}
	}
	
	
	
	
	public static void readLines()
	{
		try
		{
			String line=myReader.buff_reader.readLine();
			int counter = 0;
			
			while(line!=null)
			{
				lines.add(counter,line);
				counter++;
				if(counter<blockSize)
					line = myReader.buff_reader.readLine();
				else
					break;
			}
		}
		catch(IOException e)
		{
			System.out.println("IOException in In Read N lines function class:FirstPhaseSort");
		}		
	}
	
	
	public static void createHashedFiles()
	{
		if(relationType==0)
			intermediateFileName=Constants.RHASHSUBLIST;
		else
			intermediateFileName=Constants.SHASHSUBLIST;
		
		for(int i=0;i<M-1;i++)
		{
			fileWriter[i] = new FileWriterClass(intermediateFileName+i);
			fileWriter[i].createFile();
		}
	}
	
	
	public static void closeHashedFiles()
	{
		for(int i=0;i<M-1;i++)
		{
			fileWriter[i].closeFile();
		}
	}
	
	
	public static void calculatePosition()
	{
		
		if(relationType==0)
		{
			FileReaderClass myFileReader = new FileReaderClass(relationName);
//			System.out.println("RelationName is "+relationName);
			myFileReader.openFile();
			String myLine; 
			try {
				myLine = myFileReader.buff_reader.readLine();
				myFileReader.closeFile();
				
				String[] myArray = myLine.split(",");
				position = myArray.length-1;
			}
							
			catch (IOException e) {
				System.out.println("Exception caught in SortBigRelatoins: calculateNoOfLinesToRead");
			}
		}
		else
		{
			position=0;
		}
	}
	
	
}
