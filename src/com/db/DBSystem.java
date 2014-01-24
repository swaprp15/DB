package com.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DBSystem 
{
	// I guess need to start from 0
	// Keep a list of free frames
	// Change logical number to 0-1023
	
	private long globalPageNo = 1;
	
	private ConfigReader configReader;
	Cache cache;
	
	public DBSystem(String configFile) 
	{
		try
		{
			CustomLogger.setup();
			configReader = new ConfigReader(configFile);
			configReader.ReadConfigs();
			cache = new Cache(configReader.getNumberOfPages());
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void populatePageInfo()
	{
		File file = null;
		InputStreamReader tableFile = null;
		int pageNumber = 1, startRecordNo = 1, endRecordNo = 1;
		long startPointer = 0, lastLineFeedIndex = 0, currentByte = 0;
		int bufferSize = 0;
		char readCharacter = (char)-1;
		Page page = null;
		
		/**		Read Table data & Store in virtual page table with 
		 * 		start record marker (pointer) and starting and ending record no
		 * */
		
		int pageSize = ConfigReader.getPageSize();
		
		for (String table : ConfigReader.getTableNames()) 
		{
			try 
			{
				file = new File(ConfigReader.getPathForData(), table + Constants.TableFileExtension);
				
				pageNumber = 0;
				
				if(!file.exists())
					throw new FileNotFoundException(table + Constants.TableFileExtension + " file not found in " + configReader.getPathForData());
				
				//pageNumber = 1;
				startPointer = 0;
				startRecordNo = 1;
				endRecordNo = 1;
				bufferSize = 0;
				currentByte = -1;
				
				tableFile = new InputStreamReader(new FileInputStream(file));
				
				lastLineFeedIndex = 0;
				while((readCharacter = (char)tableFile.read()) != (char)-1) 
				{
					bufferSize++;
					currentByte++;
					if(bufferSize <= pageSize) 
					{
						if(readCharacter == '\n') {
							endRecordNo++;
							lastLineFeedIndex = currentByte;
						}
					}
					else 
					{
						page = new Page();
						page.setGlobalPageNo(globalPageNo++);
						page.setInternalPageNo(pageNumber++);
						page.setStartPointer(startPointer);
						page.setStartRecordId(startRecordNo);
						bufferSize = 0;
						
						if(lastLineFeedIndex != currentByte - 1) {
							endRecordNo--;
							startPointer = lastLineFeedIndex + 1;
						}
						else
							startPointer = currentByte;
						
						page.setEndRecordId(endRecordNo);
						
						//pageNumber++;
						startRecordNo = endRecordNo + 1;
						PageStore.addPage(table, page);
					}
				}
				
				if(bufferSize != 0) 
				{
					page = new Page();
					page.setGlobalPageNo(globalPageNo++);
					page.setInternalPageNo(pageNumber++);
					page.setStartPointer(startPointer);
					page.setStartRecordId(startRecordNo);
					page.setEndRecordId(endRecordNo);
					PageStore.addPage(table, page);
				}
			}
			catch(FileNotFoundException fnfe) 
			{
				System.out.println("Error: "+fnfe.getMessage());
				continue;
			}
			catch (IOException e) 
			{
				System.out.println("IO Error: "+e.getMessage());
				continue;
			}
		}
		
		PageStore.showAll();
	}

	public String getRecord(String tableName, int recordId) 
	{
		LinkedHashMap<Long, Page> pageTable =  PageStore.GetPageTable(tableName);
	
		if(pageTable == null)
		{
			System.out.println("Page table for the table " + tableName + " not found.");
			return null;
		}
		
		// Find the appropriate page which contains this record.
		
		Page requiredLogicalPage = null;
		
		//
		// ******** Use binary search ************
		//
		
		for(Long pageId : pageTable.keySet())
		{
			Page page = pageTable.get(pageId);
			
			if((page.getStartRecordId() <= recordId) && (page.getEndRecordId() >= recordId))
			{
				requiredLogicalPage = page;
				break;
			}
			
		}
		
		if(requiredLogicalPage == null)
			return null;
		
		long globalPageId = requiredLogicalPage.getGlobalPageNo();
		
		if(!requiredLogicalPage.isInMemory())
		{
			// Bring page into cache
			if(cache.BringPageInCache(globalPageId, tableName, requiredLogicalPage.getInternalPageNo(), requiredLogicalPage.getStartPointer(), requiredLogicalPage.getEndRecordId() - requiredLogicalPage.getStartRecordId() + 1) == true)
				requiredLogicalPage.setInMemory(true);
			
			// Will be set false when this page will be removed.
		}
		
		// Get record.
		
		return cache.GetRecord(requiredLogicalPage.getFrameNo(), recordId - requiredLogicalPage.getStartRecordId());
	}

	public void insertRecord(String tableName, String record)
	{
		LinkedHashMap<Long, Page> table = PageStore.GetPageTable(tableName);
		
		table.size();
		//table.entrySet().
	}
}