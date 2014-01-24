package com.db;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class PageStore 
{
	private static HashMap<String, LinkedHashMap<Long, Page>> pageCollection = null;
	
	static 
	{
		pageCollection = new HashMap<String, LinkedHashMap<Long,Page>>();
	}
	
	public static void addPage(String tableName, Page newPage) 
	{
		if(!pageCollection.containsKey(tableName))
			pageCollection.put(tableName, new LinkedHashMap<Long, Page>());
		
		//pageCollection.get(tableName).put((long)newPage.getGlobalPageNo(), newPage);
		pageCollection.get(tableName).put((long)newPage.getInternalPageNo(), newPage);
	}
	
	public static void showAll()
	{
		System.out.println("Tables: "+pageCollection.size());
		
		for(String table : pageCollection.keySet()) 
		{
			System.out.println("Table: "+table);
			LinkedHashMap<Long, Page> pageList = pageCollection.get(table);
			
			for(long pageNo : pageList.keySet())
			{
				System.out.println("Page Info: "+pageList.get(pageNo));
			}
		}
	}
	
	// Added by Swapnil
	
	public static LinkedHashMap<Long, Page> GetPageTable(String tableName)
	{
		return pageCollection.get(tableName);
	}
}
