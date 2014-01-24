import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class ConfigReader {

	private String configFileName;
	private String pathForData;
	
	private int pageSize;
	private int numberOfPages;
	
	private List<String> tableNames;
	
	// Getters.
	public String getConfigFileName() {
		return configFileName;
	}

	public String getPathForData() {
		return pathForData;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public List<String> getTableNames() {
		return tableNames;
	}

	public static Logger getLogger() {
		return LOGGER;
	}
	
	private final static Logger LOGGER = Logger.getLogger(ConfigReader.class
		      .getName());
	
	public ConfigReader(String fileName)
	{
		this.configFileName = fileName;
		this.tableNames = new ArrayList<String>();
		
		LOGGER.setLevel(Level.SEVERE);
	    LOGGER.severe("Info Log");
	    LOGGER.warning("Info Log");
	    LOGGER.info("Info Log");
	    LOGGER.finest("Really not important");

	    // set the LogLevel to Info, severe, warning and info will be written
	    // finest is still not written
	    LOGGER.setLevel(Level.INFO);
	    LOGGER.severe("Info Log");
	    LOGGER.warning("Info Log");
	    LOGGER.info("Info Log");
	    LOGGER.finest("Really not important");
	}

	public void ReadConfigs()
	{
		try
		{
			InputStreamReader reader = new InputStreamReader(new FileInputStream(configFileName));
			
			StringBuilder br = new StringBuilder();
			
			char ch;
			
			boolean lineComplete = false, lastLineWasBEGIN = false;;
			
			
			while((ch = (char)reader.read()) != -1)
			{
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
								// Use logger.
							}
						
							break;
	
					case "NUM_PAGES":
							
							try
							{
								numberOfPages = Integer.parseInt(parts[1]);
							}
							catch(Exception e)
							{
								
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
			LOGGER.warning("Exception : " + e.getMessage());
			
		}
	}
	
}
