package utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;

public class DriverFactory {

    private static AndroidDriver driver;

    private static final String APPIUM_SERVER_URL =
            "http://127.0.0.1:4723";

    private static final String APK_PATH =
            Paths.get(
                    "src",
                    "test",
                    "resources",
                    "apk",
                    "DemoBank.apk"
            ).toAbsolutePath().toString();

    public static void startDriver() throws MalformedURLException {

        UiAutomator2Options options = new UiAutomator2Options();

        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setDeviceName("emulator-5554");
        options.setApp(APK_PATH);

        options.setNewCommandTimeout(Duration.ofSeconds(120));

        options.setCapability(
                "appium:uiautomator2ServerInstallTimeout",
                120000
        );

        options.setCapability(
                "appium:uiautomator2ServerLaunchTimeout",
                120000
        );

        driver = new AndroidDriver(
                new URL(APPIUM_SERVER_URL),
                options
        );
    }

    public static AndroidDriver getDriver() {
        return driver;
    }

    public static void quitDriver() {

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}