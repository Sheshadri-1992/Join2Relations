package com.POD.SortedMerge;

import com.POD.FileHelper.*;
import com.POD.Main.Constants;

import java.io.IOException;
import java.util.*;

public class JoinPhase {

	public static int  noOfSortedListsR;
	public static int  noOfSortedListsS;
	public static int  M;
	public static FileReaderClass[] readerRelForR;
	public static FileReaderClass[] readerRelForS;
	public static FileWriterClass myWriter;
	
	public static boolean[] flagsForRelationR;
	public static boolean[] flagsForRelationS;
	public static String[] relRString;
	public static String[] relSString;
	public static List<String> totalLinesforR;
	public static List<String> totalLinesforS;
	public static List<String> linesOfR;
	public static List<String> linesOfS;
	public static List<String> outputList;
	public static String minString;
	public static boolean minStringFlag;
	public static int joinAttributePosition;
	
	public JoinPhase(int noOfSortedListsRArgs,int noOfSortedListsSArgs,int MArgs,String relRName,String relSName)
	{
		noOfSortedListsR = noOfSortedListsRArgs;
		noOfSortedListsS = noOfSortedListsSArgs;
		M=MArgs;
		
		readerRelForR = new FileReaderClass[noOfSortedListsRArgs];
		readerRelForS = new FileReaderClass[noOfSortedListsSArgs];
		myWriter = new FileWriterClass(relRName+"_"+relSName+"_join");
		
		relRString = new String[noOfSortedListsR];
		relSString = new String[noOfSortedListsS];
		
		flagsForRelationR = new boolean[noOfSortedListsR];
		flagsForRelationS = new boolean[noOfSortedListsS];
		minStringFlag = true;
		
		linesOfR = new ArrayList();
		linesOfS = new ArrayList();
		totalLinesforR = new ArrayList();
		totalLinesforS = new ArrayList();
//		outputList = new ArrayList();
	}
	
	public static void openFiles()
	{
		for(int i=0;i<noOfSortedListsR;i++)
		{
			readerRelForR[i] = new FileReaderClass(Constants.RSUBLIST+i);
			readerRelForR[i].openFile();
			flagsForRelationR[i] = false;
		}
		
		for(int i=0;i<noOfSortedListsS;i++)
		{
			readerRelForS[i] = new FileReaderClass(Constants.SSUBLIST+i);
			readerRelForS[i].openFile();
			flagsForRelationS[i] =false;
		}
		
		
	}
	
	public static void mainLogic()
	{
		
		myWriter.createFile();
		callOnceInitially();
		int counter = 0;
		
		do
		{
			
			boolean stopFlag = checkForExhaustionOfFiles();
			
			if(stopFlag == true)
				break;
			
			findMinStringModified();	
			
//			if(minString.compareTo("UtBYslEqzt")==0)
//			{
//				System.out.println("Got you!");
//				break;
//			}
			
			if(minStringFlag==false)
				break;
			
	
//			System.out.println("The min string is "+minString);
			
			prepareStructureForR();
			prepareStructureForS();
//			System.out.println("R about to join "+linesOfR);
//			System.out.println("S about to join "+linesOfS);
			
			
			counter += linesOfR.size()*linesOfS.size();
			
			if(linesOfR.size()>0 && linesOfS.size()>0)
				performJoin();
			

			
//			System.out.println("The no of lines in R are "+linesOfR.size());
//			System.out.println("The no of lines in S are "+linesOfS.size());
			
			linesOfR.clear();
			linesOfS.clear();
//			myWriter.closeFile();
//
//			System.exit(0);
			
		}while(true);
		
		myWriter.closeFile();
		
		System.out.println("Counter is "+counter);
	}
	
	public static boolean checkForExhaustionOfFiles()
	{
		boolean RFlag=flagsForRelationR[0],SFlag=flagsForRelationS[0];
		
		for(int i=0;i<noOfSortedListsR;i++)
		{
			RFlag = RFlag && flagsForRelationR[i];
		}
		
		for(int i=0;i<noOfSortedListsS;i++)
		{
			SFlag = SFlag && flagsForRelationS[i];
		}
			
		return (RFlag||SFlag);
	}
	
	public static void prepareStructureForR()
	{
		String next;
		
		for(int i =0;i<noOfSortedListsR;i++)
		{
			if(flagsForRelationR[i]==true)
				continue;
			
			if(relRString[i].compareTo("")==0)
				continue;
			
			next = relRString[i];
			String[] split1 = next.split(",");			
			String op1 = split1[joinAttributePosition];		
			
//			System.out.println("Inside preparestructure for R "+relRString[i]);
			
			if(op1.compareTo(minString)==0)
			{
				linesOfR.add(next);
//				System.out.println("R list added "+next);
				try {
					String line = readerRelForR[i].buff_reader.readLine();
					
					
					while(line!=null)
					{
						String[] myArray = line.split(",");						
						String operand = myArray[joinAttributePosition];
						
						relRString[i]=line;
						
						if(operand.compareTo(minString)==0)
						{
							linesOfR.add(line);
							line = readerRelForR[i].buff_reader.readLine();							
						}
						else
						{
							break;
						}
						
					}
					
					if(line==null)
					{
						relRString[i]="";
						readerRelForR[i].closeFile();
						flagsForRelationR[i]=true;
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		

		
//		System.out.println("R is "+linesOfR+"size of R is "+linesOfR.size());
	}
	
	public static void prepareStructureForS()
	{
		String next;
		for(int i =0;i<noOfSortedListsS;i++)
		{
			if(flagsForRelationS[i]==true)
				continue;
			
			if(relSString[i].compareTo("")==0)
				continue;
			
			next = relSString[i];
			String[] split1 = next.split(",");			
			String op1 = split1[0];		
			
			if(op1.compareTo(minString)==0)
			{
				linesOfS.add(next);
				try {
					String line = readerRelForS[i].buff_reader.readLine();
					while(line!=null)
					{
						String[] myArray = line.split(",");						
						String operand = myArray[0];
						relSString[i]=line;
						
						if(operand.compareTo(minString)==0)
						{
							linesOfS.add(line);
							line = readerRelForS[i].buff_reader.readLine();
						}
						else
						{
							break;
						}
						
					}
					if(line==null)
					{
						relSString[i]="";
						readerRelForS[i].closeFile();
						flagsForRelationS[i]=true;
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void performJoin()
	{	
		
		
		for(int i=0;i<linesOfR.size();i++)
		{
			String relRString = linesOfR.get(i);
			String[] split1 = relRString.split(",");
			
			for(int j=0;j<linesOfS.size();j++)
			{
				String relSString = linesOfS.get(j);
				String[] split2 = relSString.split(",");
//				outputList.add(split1[0]+split1[1]+split2[1]);
//				if(outputList.size()==M*Constants.TUPLESPERBLOCK)
//				{
//					myWriter.w
//				}
//				System.out.println(split1[0]+","+split1[1]+","+split2[1]);
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
				
				myWriter.writeline(myStringBuilder.toString());
				myStringBuilder = null;
			}
		
		}
		
		
	}
	
	public static void callOnceInitially()
	{
		String line;

		
		for(int i=0;i<noOfSortedListsR;i++)
		{
			try
			{
				line = readerRelForR[i].buff_reader.readLine();
			
//				System.out.println("Line is "+line);
				if(line==null)
				{
					flagsForRelationR[i]=true;
					readerRelForR[i].closeFile();
					relRString[i]="";
					continue;
				}
				relRString[i] = line;
			}
			catch(IOException e)
			{
				System.out.println("In the class JoinPhase: method callOnceInitially");
			}
		}
		
		
		/**This is important stuff**/
		String[] myArray = relRString[0].split(",");
		joinAttributePosition = myArray.length -1;
		System.out.println("Join attribute position is "+joinAttributePosition);
		
		for(int i=0;i<noOfSortedListsS;i++)
		{
			try {
				line = readerRelForS[i].buff_reader.readLine();
				
//				System.out.println(relSString[i]);
				
				if(line==null)
				{
					flagsForRelationS[i]=true;
					readerRelForS[i].closeFile();
					relSString[i]="";
					continue;
				}
				relSString[i]=line;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("In the class JoinPhase: method callOnceInitially");
			}
		}
		
		
	}
	
	public static void findMinStringModified()
	{
		totalLinesforR.clear();
		totalLinesforS.clear();
		
		
		for(int i=0;i<noOfSortedListsR;i++)
		{
			 if(relRString[i].compareTo("")!=0)
				totalLinesforR.add(relRString[i]);
			
		}
//		System.out.println("R: "+totalLinesforR);
		
		for(int i=0;i<noOfSortedListsS;i++)
		{
			 if(relSString[i].compareTo("")!=0)
					totalLinesforS.add(relSString[i]);
		}
		
//		System.out.println("S: "+totalLinesforS);
		
		if((totalLinesforR.size()==0) || (totalLinesforS.size()==0))
		{
			minStringFlag = false;
			totalLinesforR.clear();
			totalLinesforS.clear();
			return;
		}
		
		String[] myArray =totalLinesforR.get(0).split(","); 
		minString = myArray[joinAttributePosition];
		String next;
		
		for(int i =1;i<totalLinesforR.size();i++)
		{
			next = totalLinesforR.get(i);
			String[] split1 = next.split(",");
		
			
			String op1 = split1[joinAttributePosition]; 
			String op2 = minString;
			
						
			if(op1.compareTo("UtBYslEqzt")==0)
				System.out.println("In Relation R");
			
			//if op1 < op2 it returns negative
			if(op2.compareTo(op1)>0)
				minString = op1;
			
			
			
		}
		
		
		
		for(int i = 0; i<totalLinesforS.size();i++)
		{
			next = totalLinesforS.get(i);
			String[] split1 = next.split(",");
			
			
			String op1 = split1[0]; 
			String op2 = minString;
			
			//if op1 < op2 it returns negative
			if(op2.compareTo(op1)>0)
				minString = op1;
		}
		
		
			
		totalLinesforR.clear();
		totalLinesforS.clear();
		
	}

	

}
	
	
	

