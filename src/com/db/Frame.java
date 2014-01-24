
package com.db;

import java.util.List;


public class Frame 
{
	private long logicalPageId;
	private long size;
	
	private String tableName;
	
	private boolean isDirty;
	
	private List<String> data;
	
	public Frame(String tableName, long logicalPageId)
	{
		this.tableName = tableName;
		this.logicalPageId = logicalPageId;
	}

	public long getSize() 
	{
		return size;
	}

	public void setSize(long size) 
	{
		this.size = size;
	}

	public boolean isDirty() 
	{
		return isDirty;
	}

	public void setDirty(boolean isDirty) 
	{
		this.isDirty = isDirty;
	}

	public List<String> getData() 
	{
		return data;
	}

	public void setData(List<String> data) 
	{
		this.data = data;
	}

	public long getLogicalPageId() 
	{
		return logicalPageId;
	}

	public String getTableName() 
	{
		return tableName;
	}
	
	public String GetRecord(int offset)
	{
		return data.get(offset);
	}
}
