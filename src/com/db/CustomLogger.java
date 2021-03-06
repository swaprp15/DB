package com.db;
import java.io.IOException;
import java.util.Formatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class CustomLogger {
	
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;

	static public void setup() throws IOException 
	{
	    // Get the global logger to configure it
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	    logger.setLevel(Level.INFO);
	    fileTxt = new FileHandler("Log.txt");

	    // create txt Formatter
	    formatterTxt = new SimpleFormatter();
	    fileTxt.setFormatter(formatterTxt);
	    logger.addHandler(fileTxt);

	}

	
	
}
