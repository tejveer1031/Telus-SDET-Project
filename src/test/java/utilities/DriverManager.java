package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.time.Duration;
import org.apache.commons.io.FileUtils;

public class DriverManager {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<String> browserName = new ThreadLocal<>();
    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    private static final Duration EXPLICIT_WAIT = Duration.ofSeconds(15);

    public static void setBrowser(String browser) {
        browserName.set(browser.toLowerCase());
        initializeDriver();
    }

    private static void initializeDriver() {
        String browser = browserName.get();
        if (driver.get() == null) {
            switch(browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver.set(new ChromeDriver(getChromeOptions()));
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver.set(new FirefoxDriver(getFirefoxOptions()));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            configureDriver();
        }
    }


    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                 "--headless=new", // Ensure this is commented
                "--width=1920",
                "--height=1080",
                "--start-maximized",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--force-device-scale-factor=1",
                "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36",
                "--disable-browser-side-navigation",
                "--disable-extensions",
                "--disable-gpu-compositing",
                // "--user-data-dir=C:\\Temp\\ChromeProfile", // Test without profile
                "--disable-features=DialMediaRouteProvider",
                "--enable-automation",
                "--disable-component-update"
        );
        options.setBinary("/usr/bin/google-chrome-stable");  // Explicit binary path
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return options;
    }

    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments(
                "-headless",
                "--width=1920",
                "--height=1080",
                "--start-maximized",
                "--disable-gpu",
                "--no-sandbox",
                "--disable-dev-shm-usage"
        );
        options.setBinary("/usr/bin/firefox");
        options.addPreference("layers.acceleration.disabled", true);
        options.setAcceptInsecureCerts(true);
        return options;
    }

    private static void configureDriver() {
        driver.get().manage().timeouts()
                .implicitlyWait(IMPLICIT_WAIT)
                .pageLoadTimeout(IMPLICIT_WAIT);
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            throw new IllegalStateException("Driver not initialized. Call setBrowser() first.");
        }
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            try {
                driver.get().quit();
            } finally {
                driver.remove();
            }
        }
    }
}