package config.setup;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;
import util.constants.Config;
import util.enums.BrowserType;

import java.net.URL;

/**
 * Class that sets up the WebDriver configuration.
 */
@Slf4j
@Getter
public class BrowserConfig {

    private WebDriver driver;
    private ChromeOptions chromeOptions;
    private FirefoxOptions firefoxOptions;
    private EdgeOptions edgeOptions;
    private String env;

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        this.env = System.getenv("environment") != null ? System.getenv("environment") : "local";

        if (this.env.equalsIgnoreCase("docker")) {
            log.info("Docker environment detected, skipping Docker container setup...");
            return;
        }
        try {
            log.info("Starting Docker Application container...");
            ProcessBuilder processBuilder = new ProcessBuilder("./application.sh", "start");
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            log.error("Failed to start Docker container", e);
        }
        log.info("Setting up the suite...");
        log.info("Suite set up correctly!");
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        log.info("Tearing down the suite...");
        if (this.env.equalsIgnoreCase("docker")) {
            log.info("Docker environment detected, skipping Docker container teardown...");
            return;
        }
        log.info("Stopping Docker Application container...");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("./application.sh", "stop");
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            log.error("Failed to stop Docker container", e);
        }
        log.info("Suite torn down correctly!");
    }


    @BeforeMethod(alwaysRun = true)
    @Parameters({"browserName"})
    public void setUp(@Optional String browserName, ITestContext context) throws Exception {
        browserName = (browserName == null) ? System.getProperty("browser") : browserName;
        this.driver = (env != null && env.equalsIgnoreCase("docker")) ?
                initRemoteDriver(browserName, context) :
                initLocalDriver(browserName, context);

        this.driver.manage().window().maximize();
    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    private WebDriver initLocalDriver(String browserName, ITestContext context) {
        BrowserType browser = BrowserType.getBrowser(browserName);
        context.setAttribute("browserName", browser.name());
        WebDriver webDriver;

        webDriver = switch (browser) {
            case EDGE -> getEdgeDriver();
            case FIREFOX -> getFirefoxDriver();
            default -> getChromeDriver();
        };
        return webDriver;
    }

    private RemoteWebDriver initRemoteDriver(String browserName, ITestContext context) throws Exception {
        BrowserType browser = BrowserType.getBrowser(browserName);
        context.setAttribute("browserName", browserName);

        Capabilities options = switch (browser) {
            case EDGE -> getEdgeOptions();
            case FIREFOX -> getFirefoxOptions();
            default -> getChromeOptions();
        };
        return createRemoteDriver(options);
    }

    private RemoteWebDriver createRemoteDriver(Capabilities options) throws Exception {
        try {
            String hubUrl = Config.HUB_URL;
            return new RemoteWebDriver(new URL(hubUrl), options);
        } catch (Exception e) {
            log.error("Failed to create RemoteWebDriver", e);
            throw e;
        }
    }

    private WebDriver getChromeDriver() {
        log.info("Launching Chrome Driver...");
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(getChromeOptions());
    }

    private WebDriver getEdgeDriver() {
        log.info("Launching Edge Driver...");
        WebDriverManager.edgedriver().setup();
        return new EdgeDriver(getEdgeOptions());
    }

    private WebDriver getFirefoxDriver() {
        log.info("Launching Firefox Driver...");
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(getFirefoxOptions());
    }

    private ChromeOptions getChromeOptions() {
        log.info("Setting Chrome preferences...");
        chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(Config.CHROME_ARGUMENTS);
        if (isHeadless()) {
            this.chromeOptions.addArguments("--no-sandbox");
            this.chromeOptions.addArguments("--disable-dev-shm-usage");
            this.chromeOptions.addArguments("--headless");
        }
        chromeOptions.setExperimentalOption(Config.PREFS, Config.PASSWORD_PREFS);
        log.info("Chrome preferences set correctly!");
        return chromeOptions;
    }

    private FirefoxOptions getFirefoxOptions() {
        log.info("Setting Firefox preferences...");
        firefoxOptions = new FirefoxOptions();
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.INFO);
        if (isHeadless()) {
            firefoxOptions.addArguments("--headless");
        }
        log.info("Firefox preferences set correctly!");
        return firefoxOptions;
    }

    private EdgeOptions getEdgeOptions() {
        log.info("Setting Edge preferences...");
        edgeOptions = new EdgeOptions();
        if (isHeadless()) {
            edgeOptions.addArguments("headless");
            edgeOptions.addArguments("enable-automation");
        }
        log.info("Edge preferences set correctly!");
        return edgeOptions;
    }

    private boolean isHeadless() {
        if ("docker".equalsIgnoreCase(this.env)) {
            return true;
        }
        String headless = System.getenv("HEADLESS");
        return headless != null && headless.equalsIgnoreCase("true");
    }
}
