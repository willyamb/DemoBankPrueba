package tests;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utilidad para capturar pantallas cuando una prueba falla.
 */
public class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    public static String takeScreenshot(
            AndroidDriver driver,
            String testName
    ) {

        File source =
                ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.FILE);

        Path directory =
                Paths.get("test-output", "screenshots");

        Path destination =
                directory.resolve(testName + ".png");

        try {

            Files.createDirectories(directory);

            Files.copy(
                    source.toPath(),
                    destination,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

            return destination.toAbsolutePath().toString();

        } catch (IOException e) {

            return null;
        }
    }
}