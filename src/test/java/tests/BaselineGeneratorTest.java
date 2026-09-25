package tests;

import org.openqa.selenium.OutputType;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.TestDataProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Genera una imagen base de referencia de la pantalla Home.
 *
 * Este test se ejecuta únicamente para crear el baseline inicial.
 * La imagen generada se utilizará posteriormente para la comparación
 * visual mediante OpenCV.
 */
public class BaselineGeneratorTest extends BaseTest {

    @Test
    public void generateHomeBaseline() throws IOException {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);

        if (!homePage.isHomeDisplayed()) {
            throw new AssertionError(
                    "La pantalla Home no está visible."
            );
        }

        /*
         * Capturamos la pantalla completa del dispositivo.
         */
        File screenshot =
                driver.getScreenshotAs(OutputType.FILE);

        /*
         * Ruta donde el PDF exige almacenar los baselines.
         */
        Path baselineDirectory =
                Paths.get(
                        "src",
                        "test",
                        "resources",
                        "baselines"
                );

        Files.createDirectories(baselineDirectory);

        Path baselinePath =
                baselineDirectory.resolve(
                        "home_baseline.png"
                );

        Files.copy(
                screenshot.toPath(),
                baselinePath,
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );

        System.out.println(
                "Baseline generado correctamente: "
                        + baselinePath.toAbsolutePath()
        );
    }
}