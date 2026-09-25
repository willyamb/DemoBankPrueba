package tests;

import org.openqa.selenium.OutputType;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.ImageMatchUtils;
import utils.TestDataProvider;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Prueba de regresión visual mediante OpenCV.
 *
 * Compara un screenshot actual de la pantalla Home contra
 * la imagen base almacenada en src/test/resources/baselines/.
 */
public class ImageMatchTest extends BaseTest {

    /**
     * Verifica que la pantalla Home mantenga una similitud visual
     * igual o superior al 95 % respecto al baseline.
     */
    @Test
    public void shouldMatchHomeAgainstBaseline() throws Exception {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(
                homePage.isHomeDisplayed(),
                "La pantalla Home no está visible."
        );

        /*
         * Ruta del baseline oficial.
         */
        Path baselinePath = Paths.get(
                "src",
                "test",
                "resources",
                "baselines",
                "home_baseline.png"
        );

        Assert.assertTrue(
                Files.exists(baselinePath),
                "No existe el baseline esperado: "
                        + baselinePath.toAbsolutePath()
        );

        /*
         * Creamos un directorio temporal dentro de target
         * para no contaminar src/test/resources.
         */
        Path screenshotDirectory = Paths.get(
                "target",
                "opencv"
        );

        Files.createDirectories(screenshotDirectory);

        Path currentScreenshotPath =
                screenshotDirectory.resolve(
                        "home_current.png"
                );

        /*
         * Capturamos la pantalla actual.
         */
        File screenshot =
                driver.getScreenshotAs(OutputType.FILE);

        Files.copy(
                screenshot.toPath(),
                currentScreenshotPath,
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
        );

        /*
         * Comparamos la captura actual contra el baseline.
         */
        double matchScore =
                ImageMatchUtils.compareImages(
                        currentScreenshotPath.toString(),
                        baselinePath.toString()
                );

        System.out.println(
                "OpenCV Match Score: "
                        + String.format("%.2f", matchScore)
                        + "%"
        );

        /*
         * Requisito mínimo del ejercicio: 95 %.
         */
        Assert.assertTrue(
                matchScore >= 95.0,
                "La similitud visual de Home está por debajo del "
                        + "umbral requerido. Match Score: "
                        + String.format("%.2f", matchScore)
                        + "%"
        );
    }
}