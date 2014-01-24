
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try
		{
			CustomLogger.setup();
			ConfigReader configReader = new ConfigReader("config.txt");
			configReader.ReadConfigs();
			
			System.err.println(configReader.getNumberOfPages());
			System.err.println(configReader.getPageSize());
			System.err.println(configReader.getPathForData());
			System.err.println(configReader.getTableNames());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

}
