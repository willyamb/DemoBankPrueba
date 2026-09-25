package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

/**
 * Page Object de la pantalla de pago exitoso.
 *
 * Centraliza la consulta del resultado del pago y las acciones
 * disponibles después de completar la operación.
 */
public class PaymentSuccessPage {

    private final AndroidDriver driver;

    @FindBy(xpath = "//android.widget.TextView[@text='¡Pago exitoso!']")
    private WebElement successMessage;

    /*
     * El resumen contiene el monto y el nombre del servicio.
     * Ambos valores son dinámicos, por lo que se utiliza una
     * coincidencia parcial.
     */
    @FindBy(xpath = "//android.widget.TextView[contains(@text,'Pagaste')]")
    private WebElement paymentSummary;

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Volver al inicio']")
    private WebElement backToHomeButton;

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Hacer otro pago']")
    private WebElement newPaymentButton;

    /**
     * Inicializa el Page Object y sus elementos.
     *
     * @param driver instancia activa de AndroidDriver
     */
    public PaymentSuccessPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Verifica que la pantalla de pago exitoso esté visible.
     *
     * @return true si se muestra el mensaje de éxito
     */
    public boolean isSuccessDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, successMessage)
                .isDisplayed();
    }

    /**
     * Obtiene el resumen mostrado después de completar el pago.
     *
     * @return texto del resumen del pago
     */
    public String getPaymentSummary() {
        return WaitUtils
                .waitForVisibility(driver, paymentSummary)
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
     * Inicia un nuevo pago.
     */
    public void tapNewPayment() {
        WaitUtils
                .waitForClickable(driver, newPaymentButton)
                .click();
    }
}