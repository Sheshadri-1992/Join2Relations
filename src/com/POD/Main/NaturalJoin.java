package com.POD.Main;

import java.io.IOException;
import java.util.*;

import com.POD.FileHelper.FileReaderClass;
import com.POD.HashJoin.FinishHashProcess;
import com.POD.HashJoin.HashJoin;
import com.POD.SortedMerge.JoinPhase;
import com.POD.SortedMerge.SortBigRelations;
import java.util.*;

public class NaturalJoin {

	public static String RelationR; 
	public static String RelationS;
	public static int M;
	public static String choice;
	public static int blockSize;
	public static int noOfLinesRelR;
	public static int noOfLinesRelS;
	public static HashMap<String,Integer> relationNumber;
	public static long startTime;
	public static long endTime;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		relationNumber = new HashMap<>();
		RelationR = args[0];
		RelationS = args[1];
		choice = args[2];
		M = Integer.parseInt(args[3]);
		blockSize = Constants.TUPLESPERBLOCK*M;
		
		relationNumber.put(RelationR, 0);
		relationNumber.put(RelationS, 1);
		
//		List<String> myList = new ArrayList();
//		
//		for(int i =0;i<10;i++)
//		{
//			myList.add(i+"");
//		}
//		
//		
//		System.out.println(myList.get(0));
//		
//		System.exit(0);
		
		if(choice.compareTo(Constants.SORTEDMERGE)==0)
		{
			startTime = System.currentTimeMillis();
			
			System.out.println("You chose Sorted merge join");
			/** things to do
			 * 1. Sort the first relation
			 * 2. Sort the second relation
			 * 3. Do Merge
			 */			
			
			int noOfSortedListsR=0;
			int noOfSortedListsS=0;
			
			noOfLinesRelR = countLinesInRelation(RelationR);
			noOfLinesRelS = countLinesInRelation(RelationS);
			
			boolean flag=checkIfSortedMergeIsPossible();
			
			if(!flag)
			{
				System.out.println("Sorted Merge join is not possible");
				System.exit(0);
			}
			
			SortBigRelations sortObj = new SortBigRelations(RelationR, blockSize,relationNumber);
			sortObj.calcuateNoOfLinesToRead();
			sortObj.computeNoOfSortedSublists();
			sortObj.createSortedSublists();
			noOfSortedListsR = sortObj.noOfSortedSubLists;
			System.out.println("Relation R sorted lists are  "+noOfSortedListsR);
			
			sortObj = new SortBigRelations(RelationS,blockSize,relationNumber);
			sortObj.calcuateNoOfLinesToRead();
			sortObj.computeNoOfSortedSublists();
			sortObj.createSortedSublists();
			noOfSortedListsS = sortObj.noOfSortedSubLists;
			System.out.println("Relation S sorted lists are  "+noOfSortedListsS);
			
			JoinPhase joinObj = new JoinPhase(noOfSortedListsR, noOfSortedListsS, M,RelationR,RelationS);
			joinObj.openFiles();
			joinObj.mainLogic();
			
			
			endTime = System.currentTimeMillis();
			
			System.out.println("Sorted Join Time is "+(endTime-startTime)/(1.0*1000));
		}
		else
		{
			startTime = System.currentTimeMillis();
			System.out.println("You chose Hashed Join ");
			HashJoin hashObj = new HashJoin(RelationR, M, blockSize,relationNumber);
			hashObj.calcuateNoOfLinesToRead();
			hashObj.computeNoOfTimeToLoad();
			hashObj.createHashedSublists();
			
			hashObj = new HashJoin(RelationS, M, blockSize,relationNumber);
			hashObj.calcuateNoOfLinesToRead();
			hashObj.computeNoOfTimeToLoad();
			hashObj.createHashedSublists();
			
			FinishHashProcess finHashObj = new FinishHashProcess(M, RelationR, RelationS);
			finHashObj.finishMyWork();
			endTime = System.currentTimeMillis();
			System.out.println("Hashed Join Time is "+(endTime-startTime)/(1.0*1000));
			
		}
		
	}
	
	public static int countLinesInRelation(String relationName)
	{
		int totalLinesRelation=0;
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
		
		return totalLinesRelation;
	}

	public static boolean checkIfSortedMergeIsPossible()
	{
		double result = Math.ceil((noOfLinesRelR*1.0/blockSize*1.0)) + Math.ceil((noOfLinesRelS*1.0/blockSize*1.0));
		if((int)result>(M*M))
			return false;
		else
			return true;
		
	}

}
