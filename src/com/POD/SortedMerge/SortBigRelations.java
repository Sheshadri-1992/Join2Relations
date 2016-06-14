package com.POD.SortedMerge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.POD.FileHelper.*;
import com.POD.Main.Constants;
import java.util.*;


public class SortBigRelations {

	
	static String relationName;
	static int totalLinesRelation;
	public static int noOfSortedSubLists;
	static int blockSize;
	static List<String> lines;
	static int relationType;
	static String intermediateFileName;	
	public static FileReaderClass myReader;
	public static int totalSortedLists;
	public static int position;
	
	public SortBigRelations(String relationNameArgs,int blockSizeArgs,HashMap<String,Integer> relationNumberArgs) {
		// TODO Auto-generated constructor stub	
		totalLinesRelation =0;
		noOfSortedSubLists = 0;
		relationName = relationNameArgs;
		blockSize = blockSizeArgs;
		lines = new ArrayList<>();
		
		relationType = (int)relationNumberArgs.get(relationNameArgs);
		
		
		
	}
	
	public void calcuateNoOfLinesToRead()
	{
		
		calculatePosition();
		
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
	
	public  void computeNoOfSortedSublists()
	{
		double result = ( (double)1.0*totalLinesRelation/(double)(blockSize*1.0));
		noOfSortedSubLists =(int) Math.ceil(result);		
	}
	
	public void createSortedSublists()
	{
		if(relationType==0)
			intermediateFileName=Constants.RSUBLIST;
		else
			intermediateFileName=Constants.SSUBLIST;
		
		
		myReader = new FileReaderClass(relationName);
		myReader.openFile();
		
		for(int i=0;i<noOfSortedSubLists;i++)
		{
			readLines();
			sortAndWrite(i);
			lines.clear();
		}	
		
		myReader.closeFile();
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
	
	
	public static void sortAndWrite(int j)
	{
		
		
		//ascending order sort
		Collections.sort(lines,new AscendingSort(relationType,position));		
		
		FileWriterClass obj = new FileWriterClass(intermediateFileName+j);
		obj.createFile();		
		
		for(int i=0;i<lines.size();i++)
		{
			obj.writeline(lines.get(i));
		}
		obj.closeFile();
		totalSortedLists++;
	}
	
	
	
}
