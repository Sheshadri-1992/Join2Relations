package com.POD.HashJoin;
import java.util.*;

import com.POD.FileHelper.FileReaderClass;
import com.POD.FileHelper.FileWriterClass;
import com.POD.Main.Constants;

import java.io.*;

public class FinishHashProcess {

	public static int positionR;
	public static int positionS;
	public static int M;
	public static HashMap<Integer,List<String>> finalHashMap;
	public static FileReaderClass[] fileReaderForR;
	public static FileReaderClass[] fileReaderForS;
	public static String relRName;
	public static String relSName;
	public static FileWriterClass fileWriter;
	
	public FinishHashProcess(int MArgs,String RelRNameArgs,String RelSNameArgs)
	{
		System.out.println("Control came here");
		M = MArgs;
		finalHashMap = new HashMap<Integer,List<String>>();
		fileReaderForR = new FileReaderClass[M-1];
		fileReaderForS = new FileReaderClass[M-1];
		relRName=RelRNameArgs;
		relSName=RelSNameArgs;
	}
	
	
	
	public static void finishMyWork()
	{
		fileWriter = new FileWriterClass(relRName+"_"+relSName+"_join");
		fileWriter.createFile();
		
		setPositions();		
		openFiles();

//		System.out.println("The size of the hash strucrure is "+finalHashMap.size());
		
		
		for(int i=0;i<M-1;i++)
		{
			prepareHashStructure();
			hashFirstRelation(i);
//			System.out.println("The size of the hash strucrure is "+finalHashMap.size());
//			System.exit(0);

			hashSecondRelation(i);
			finalHashMap.clear();
		}
		
		closeFiles();
		fileWriter.closeFile();
	}
	
	public static void prepareHashStructure()
	{
		for(int i=0;i<Constants.HASHVALUE;i++)
		{			
			finalHashMap.put(i, new ArrayList<String>());
		}
	}
	
	public static void setPositions()
	{
		FileReaderClass myReader = new FileReaderClass(relRName);
		myReader.openFile();
		
		try {
			String line = myReader.buff_reader.readLine();
			String[] myArray = line.split(",");
			positionR = myArray.length -1;
			myReader.closeFile();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		positionS = 0;
		
		System.out.println("Position1 is "+positionR+ " Postion 2 is "+positionS);
	}
	
	
	public static void openFiles()
	{
		for(int i=0;i<M-1;i++)
		{
			fileReaderForR[i]= new FileReaderClass(Constants.RHASHSUBLIST+i);
			fileReaderForR[i].openFile();
			
			
			fileReaderForS[i]= new FileReaderClass(Constants.SHASHSUBLIST+i);
			fileReaderForS[i].openFile();
		}
	}
	
	public static void closeFiles()
	{
		for(int i=0;i<M-1;i++)
		{
			fileReaderForR[i].closeFile();
			fileReaderForS[i].closeFile();
		}
	}
	
	public static void hashFirstRelation(int i)
	{
//		System.out.println("Position of Relatioon R is "+positionR);
		String line;
		try {
			line = fileReaderForR[i].buff_reader.readLine();
			while(line!=null)
			{
				String[] myArray = line.split(",");
				String operand = myArray[positionR];
				int hashIndex = Math.abs(operand.hashCode())%Constants.HASHVALUE;
				finalHashMap.get(hashIndex).add(line);
				line = fileReaderForR[i].buff_reader.readLine();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("Fininshed");
	}
	
	public static void hashSecondRelation(int i)
	{
//		System.out.println("The position of RelationS is "+positionS);
		String line;
		try {
			line = fileReaderForS[i].buff_reader.readLine();
			while(line!=null)
			{
				String[] split2 = line.split(",");
				String Soperand = split2[positionS];
				int hashIndex = Math.abs(Soperand.hashCode())%Constants.HASHVALUE;
				List<String> theList = finalHashMap.get(hashIndex);//this belongs to R
				
				for(int j=0;j<theList.size();j++)
				{
					String myLine = theList.get(j);
					String[] split1= myLine.split(",");
					String Roperand = split1[positionR];
					
					if(Soperand.compareTo(Roperand)==0)
					{
						
						StringBuilder myStringBuilder = new StringBuilder();				
						
						int k=0;
						
						for(k=0;k<split1.length;k++)
						{
							myStringBuilder.append(split1[k]);
							myStringBuilder.append(",");
						}
						
						for(k=1;k<split2.length-1;k++)
						{
							myStringBuilder.append(split2[k]);
							myStringBuilder.append(",");
						}
						
						myStringBuilder.append(split2[k]);
						
						fileWriter.writeline(myStringBuilder.toString());
						myStringBuilder = null;					
						
					}
					
				}
				
				line = fileReaderForS[i].buff_reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
