package com.db;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
class LRUCache extends LinkedHashMap<Long, Frame>
{
	public static int MAX_ENTRIES;
	
	public LRUCache(int maxEntries)
	{
		super(MAX_ENTRIES, (float) 1.0, true);
		MAX_ENTRIES = maxEntries;
	}
	
	@Override
	protected boolean removeEldestEntry(Map.Entry<Long, Frame> eldest)
	{
		if(size() > MAX_ENTRIES)
		{
			// Should set isInMemory false.
			Frame frame = eldest.getValue();
			
			long logicalPageId = frame.getLogicalPageId();
			String tableName = frame.getTableName();
			
			try
			{
				PageStore.GetPageTable(tableName).get(logicalPageId).setInMemory(false);
			
				System.out.println("Removed. Now is in memory?? - " + (PageStore.GetPageTable(tableName).get(logicalPageId).isInMemory() ? "Yes" : "No"));
			}
			catch(Exception e)
			{
				System.out.println("Error");
			}
			System.out.println("MISS - Removed page : " + eldest.getKey());
			
			return true;
		}
		
		return false;
	}
	
}
public class Cache{
	
	private LRUCache cache;
	
	private final Logger logger = Logger.getLogger(ConfigReader.class.getName());
	
	public Cache(int maxEntries)
	{
		cache = new LRUCache(maxEntries);
		
		logger.setLevel(Level.INFO);
		
	}
	
	public String GetRecord(long globalPageId, int offset)
	{
		if(cache.containsKey(globalPageId))
		{
			// Print 'HIT' here or from where it was called.

			return cache.get(globalPageId).GetRecord(offset);
		}
		else
		{
			logger.severe("Global page " + globalPageId + " not found.");
			return null;
		}
	}
	
	public boolean BringPageInCache(long globalPageId, String tableName, long logicalPageId, long startOffset, long numberOfRecords)
	{
		try
		{
			//
			// Instead of directly storing list of string, have a wrapper which will also store size of the page.
			//
			
			Frame frame = new Frame(tableName, logicalPageId);
			
			List<String> list = new ArrayList<String>();
			
			// Read table file to construct page
			
			RandomAccessFile tableFileReader = new RandomAccessFile(tableName + Constants.TableFileExtension, "r");
			
			tableFileReader.seek(startOffset);
			
			long size = 0;
			
			while(numberOfRecords > 0)
			{
				String line = tableFileReader.readLine();
				list.add(line);
				size += line.length()*2;
				numberOfRecords--;
			}
			
			frame.setData(list);
			frame.setSize(size);
			
			cache.put(globalPageId, frame);
			
			logger.info("Inserted page " + globalPageId + " in cache");
		}
		catch(Exception e)
		{
			logger.warning(e.getMessage());
			return false;
		}
		
		return true;
	}
}
