package com.POD.SortedMerge;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AscendingSort implements Comparator<String> {

	public static int relation;
	public static int position;
	
	AscendingSort(int relationArgs,int positionArgs)
	{
		relation = relationArgs;
		position = positionArgs;
		
//		System.out.println("The position is "+position);
	}
	
	
	@Override
	public int compare(String string1, String string2) {
		// TODO Auto-generated method stub
		
		//need to receive a negative, positive or 0
		//if 1st is lesser than 2nd negative
		//if 1st is greater than 2nd positive
		//if first is equal to second then 0
		int result = compareAs(string1,string2);
			
		if(result==0)
		{
			//sort on the next column
		}
		else
		{
			return result;
		}
		

		return 0;
	}
	


	public static int compareAs(String string1,String string2)
	{


		String[] array1 = string1.split(",");
		String[] array2 = string2.split(",");
		
		String op1 = array1[position];
		String op2 = array2[position];
		
		return op1.compareTo(op2);
		
	}

}
