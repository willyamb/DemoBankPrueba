package utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Utilidad para gestionar esperas explícitas en las pruebas.
 *
 * Centraliza las esperas utilizadas por los Page Objects
 * para evitar esperas fijas y mejorar la estabilidad.
 */
public final class WaitUtils {

    /**
     * Tiempo máximo de espera para los elementos.
     *
     * Se aumenta a 30 segundos porque la aplicación puede tardar
     * en terminar de cargar durante el arranque del emulador.
     */
    private static final int DEFAULT_TIMEOUT = 30;

    private WaitUtils() {
        // Evita la instanciación de esta clase de utilidad.
    }

    /**
     * Crea una espera explícita centralizada.
     *
     * @param driver instancia activa de AndroidDriver
     * @return WebDriverWait configurado
     */
    private static WebDriverWait createWait(AndroidDriver driver) {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(DEFAULT_TIMEOUT)
        );

        wait.ignoreAll(
                java.util.Arrays.asList(
                        NoSuchElementException.class,
                        StaleElementReferenceException.class
                )
        );

        return wait;
    }

    /**
     * Espera hasta que un elemento sea visible.
     *
     * @param driver instancia activa de AndroidDriver
     * @param element elemento previamente localizado
     * @return elemento visible
     */
    public static WebElement waitForVisibility(
            AndroidDriver driver,
            WebElement element
    ) {

        WebDriverWait wait = createWait(driver);

        return wait.until(
                ExpectedConditions.visibilityOf(element)
        );
    }

    /**
     * Espera hasta que un elemento identificado mediante By
     * sea visible.
     *
     * @param driver instancia activa de AndroidDriver
     * @param locator localizador del elemento
     * @return elemento visible
     */
    public static WebElement waitForVisibility(
            AndroidDriver driver,
            By locator
    ) {

        WebDriverWait wait = createWait(driver);

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    /**
     * Espera hasta que un elemento sea clicable.
     *
     * @param driver instancia activa de AndroidDriver
     * @param element elemento previamente localizado
     * @return elemento clicable
     */
    public static WebElement waitForClickable(
            AndroidDriver driver,
            WebElement element
    ) {

        WebDriverWait wait = createWait(driver);

        return wait.until(
                ExpectedConditions.elementToBeClickable(element)
        );
    }

    /**
     * Espera hasta que un elemento identificado mediante By
     * sea clicable.
     *
     * @param driver instancia activa de AndroidDriver
     * @param locator localizador del elemento
     * @return elemento clicable
     */
    public static WebElement waitForClickable(
            AndroidDriver driver,
            By locator
    ) {

        WebDriverWait wait = createWait(driver);

        return wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }
}