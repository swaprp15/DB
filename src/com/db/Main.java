package com.db;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try
		{
			DBSystem db = new DBSystem("config.txt");
			db.populatePageInfo();
			System.out.println(db.getRecord("student", 1));
			System.out.println(db.getRecord("student", 2));
			System.out.println(db.getRecord("student", 3));
			System.out.println(db.getRecord("student", 4));
			System.out.println(db.getRecord("student", 5));
			System.out.println(db.getRecord("student", 6));
			System.out.println(db.getRecord("student", 7));
			System.out.println(db.getRecord("student", 8));
			System.out.println(db.getRecord("student", 1));
			System.out.println(db.getRecord("student", 9));
			System.out.println(db.getRecord("student", 10));
			System.out.println(db.getRecord("student", 11));
			System.out.println(db.getRecord("student", 12));
			System.out.println(db.getRecord("student", 13));
			System.out.println(db.getRecord("student", 14));
			System.out.println(db.getRecord("student", 15));
			System.out.println(db.getRecord("student", 16));
			System.out.println(db.getRecord("student", 17));
			System.out.println(db.getRecord("student", 18));
			/*
			CustomLogger.setup();
			ConfigReader configReader = new ConfigReader("config.txt");
			configReader.ReadConfigs();
			
			System.err.println(configReader.getNumberOfPages());
			System.err.println(configReader.getPageSize());
			System.err.println(configReader.getPathForData());
			System.err.println(configReader.getTableNames());
			
			Cache lru = new Cache(2);
			
			if(lru.GetRecord(1, 0) == null)
			{
				lru.BringPageInCache(1, "student", 0, 1);
			}
			
			System.out.println(lru.GetRecord(1, 0));
			
			
			if(lru.GetRecord(2, 0) == null)
			{
				lru.BringPageInCache(2, "student", 0, 1);
			}
			
			System.out.println(lru.GetRecord(2, 0));
			
			if(lru.GetRecord(1, 0) == null)
			{
				lru.BringPageInCache(1, "student", 0, 1);
			}
			
			System.out.println(lru.GetRecord(1, 0));
			
			if(lru.GetRecord(3, 0) == null)
			{
				lru.BringPageInCache(3, "Professor", 0, 1);
			}
			
			System.out.println(lru.GetRecord(3, 0));
			*/
			
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}

}
