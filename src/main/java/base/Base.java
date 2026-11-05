package base;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Base {
	
	public static WebDriver driver;

	public static void initializeBrowser() {
	    driver = new ChromeDriver();
	    driver.manage().window().maximize();
	}

}
