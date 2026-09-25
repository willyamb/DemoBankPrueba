package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

/**
 * Page Object de la pantalla de transferencia exitosa.
 *
 * Centraliza la consulta del resultado de la transferencia
 * y las acciones disponibles al finalizar el proceso.
 */
public class TransferSuccessPage {

    private final AndroidDriver driver;

    @FindBy(xpath = "//android.widget.TextView[@text='¡Transferencia exitosa!']")
    private WebElement successMessage;

    @FindBy(xpath = "//android.widget.TextView[contains(@text,'Enviaste')]")
    private WebElement transferSummary;

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Volver al inicio']")
    private WebElement backToHomeButton;

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Hacer otra transferencia']")
    private WebElement newTransferButton;

    /**
     * Inicializa el Page Object y sus elementos.
     *
     * @param driver instancia activa de AndroidDriver
     */
    public TransferSuccessPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Verifica que la pantalla de transferencia exitosa
     * esté visible.
     *
     * @return true si se muestra el mensaje de éxito
     */
    public boolean isSuccessDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, successMessage)
                .isDisplayed();
    }

    /**
     * Obtiene el mensaje de resumen de la transferencia.
     *
     * @return texto mostrado por la aplicación
     */
    public String getTransferSummary() {
        return WaitUtils
                .waitForVisibility(driver, transferSummary)
                .getText();
    }

    /**
     * Regresa a la pantalla principal.
     */
    public void tapBackToHome() {
        WaitUtils
                .waitForClickable(driver, backToHomeButton)
                .click();
    }

    /**
     * Inicia una nueva transferencia.
     */
    public void tapNewTransfer() {
        WaitUtils
                .waitForClickable(driver, newTransferButton)
                .click();
    }


}