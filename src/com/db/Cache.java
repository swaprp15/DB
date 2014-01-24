package com.db;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
class LRUCache extends LinkedHashMap<Long, Frame>
{
	public static int MAX_ENTRIES;
	public long lastRemovedFrameNumber;
	
	public LRUCache(int maxEntries)
	{
		super(MAX_ENTRIES, (float) 1.0, true);
		MAX_ENTRIES = maxEntries;
		
		lastRemovedFrameNumber = 0;
	}
	
	@Override
	protected boolean removeEldestEntry(Map.Entry<Long, Frame> eldest)
	{
		if(size() > MAX_ENTRIES)
		{
			lastRemovedFrameNumber = eldest.getKey();
			
			// Add this frame to free list
			Cache.freeFrames.add(lastRemovedFrameNumber);
			
			// Should set isInMemory false.
			Frame frame = eldest.getValue();
			
			long logicalPageId = frame.getLogicalPageId();
			String tableName = frame.getTableName();
			
			try
			{
				PageStore.GetPageTable(tableName).get(logicalPageId).setInMemory(false);
			}
			catch(Exception e)
			{
				// Use logger
			}
			
			// Check what should be the message.
			System.out.println("MISS - Removed page : " + lastRemovedFrameNumber);
			
			return true;
		}
		
		return false;
	}
	
}

public class Cache{
	
	private LRUCache cache;
	public static Set<Long> freeFrames;
	
	private final Logger logger = Logger.getLogger(ConfigReader.class.getName());
	
	public Cache(int maxEntries)
	{
		cache = new LRUCache(maxEntries);
		
		logger.setLevel(Level.INFO);
		
		freeFrames = new HashSet<Long>();
		
		for(long i = 0; i < ConfigReader.getNumberOfPages(); i++)
			freeFrames.add(i);
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
			
			long frameNumber;
			
			if(freeFrames.iterator().hasNext())
			{
				frameNumber = freeFrames.iterator().next();
				cache.put(frameNumber, frame);
			}
			else
			{
				// Add a dummy entry.
				
				cache.put((long)LRUCache.MAX_ENTRIES, null);
				cache.remove((long)LRUCache.MAX_ENTRIES);
				cache.put(cache.lastRemovedFrameNumber, frame);
				frameNumber = cache.lastRemovedFrameNumber;
			}
			
			freeFrames.remove(frameNumber);
			
			// Set frame number for the page.
			PageStore.GetPageTable(tableName).get(logicalPageId).setFrameNo(frameNumber);
			
			// Remove this and add logger..
			System.out.println("Inserted page " + logicalPageId + " of table " + tableName + " in cache frame " + frameNumber);
			
			//logger.info("Inserted page " + logicalPageId + " of table " + tableName + " in cache frame " + frameNumber);
		}
		catch(Exception e)
		{
			logger.warning(e.getMessage());
			return false;
		}
		
		return true;
	}
}
