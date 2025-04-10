package utilities;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverManager {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<String> browserName = new ThreadLocal<>();


    public static void setBrowser(String browser) {
        browserName.set(browser);
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            String browser = browserName.get().toLowerCase();
            switch(browser) {
                case "chrome":
//                    ChromeOptions chromeOptions = new ChromeOptions();
//                    chromeOptions.addArguments("--start-maximized");
//                    driver.set(new ChromeDriver(chromeOptions));
                    driver.set(new ChromeDriver());
                    break;
                case "firefox":
//                    FirefoxOptions firefoxOptions = new FirefoxOptions();
//                    driver.set(new FirefoxDriver(firefoxOptions));
                    driver.set(new FirefoxDriver());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
