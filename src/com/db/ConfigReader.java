package com.db;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class ConfigReader {

	private static String configFileName;
	private static String pathForData;
	
	private static int pageSize;
	private static int numberOfPages;
	
	private static List<String> tableNames;
	
	// Getters.
	public static String getConfigFileName() {
		return configFileName;
	}

	public static String getPathForData() {
		return pathForData;
	}

	public static int getPageSize() {
		return pageSize;
	}

	public static int getNumberOfPages() {
		return numberOfPages;
	}

	public static List<String> getTableNames() {
		return tableNames;
	}

	public static Logger getLogger() {
		return logger;	}
	
	private final static Logger logger = Logger.getLogger(ConfigReader.class
		      .getName());
	
	public ConfigReader(String fileName)
	{
		this.configFileName = fileName;
		this.tableNames = new ArrayList<String>();

		this.logger.setLevel(Level.INFO);
	}

	public void ReadConfigs()
	{
		try
		{
			InputStreamReader reader = new InputStreamReader(new FileInputStream(configFileName));
			
			StringBuilder br = new StringBuilder();
			
			char ch;
			int charInt;
			
			boolean lineComplete = false, lastLineWasBEGIN = false;;
			
			
			while((charInt = reader.read()) != -1)
			{
				ch = (char)charInt;
				
				if(ch != '\n')
					br.append(ch);
				else
				{
					lineComplete = true;
				}
				
				// Line is not complete
				if(!lineComplete)
					continue;
				
				lineComplete = false;
				
				String parts[] = br.toString().split("\\s");

				br.setLength(0);
				
				if(parts[0].equalsIgnoreCase("BEGIN"))
				{
					lastLineWasBEGIN = true;
					continue;
				}
				
				switch (parts[0]) 
				{
					case "PAGESIZE":
								
							try
							{
								pageSize = Integer.parseInt(parts[1]);
							}
							catch(Exception e)
							{
								logger.severe("Could not parse page size.");
							}
						
							break;
	
					case "NUM_PAGES":
							
							try
							{
								numberOfPages = Integer.parseInt(parts[1]);
							}
							catch(Exception e)
							{
								logger.severe("Could not parse number of pages");
							}
							break;
							
					case "PATH_FOR_DATA":
							
							if(parts[1] != null && !parts[1].isEmpty())
								pathForData = parts[1];
						
							break;
							
					default:
							if(lastLineWasBEGIN)
							{
								tableNames.add(parts[0]);
								lastLineWasBEGIN = false;
							}
							break;
				}
				
			}
		
		}
		catch(Exception e)
		{
			logger.warning("Exception : " + e.getMessage());			
		}
	}
	
}
