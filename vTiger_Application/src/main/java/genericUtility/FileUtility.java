package genericUtility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FileUtility {


	public String getDataFromPropertyFile(String key) throws IOException {
		FileInputStream fis = new FileInputStream("./src/main/resources/commonData.properties");
		Properties p = new Properties();
		p.load(fis);
		
		return p.getProperty(key);
	}

}