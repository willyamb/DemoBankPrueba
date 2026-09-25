package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

/**
 * Page Object de la pantalla de confirmación de un pago.
 *
 * Centraliza la consulta de los datos principales del pago
 * y la acción de confirmación.
 */
public class PaymentConfirmationPage {

    private final AndroidDriver driver;

    @FindBy(xpath = "//android.widget.TextView[@text='Confirma tu pago']")
    private WebElement confirmationTitle;

    /*
     * Se contemplan los cinco servicios disponibles en la aplicación.
     */
    private final By serviceNameLocator = By.xpath(
            "//android.widget.TextView[" +
                    "@text='Energía Eléctrica' or " +
                    "@text='Agua Potable' or " +
                    "@text='Internet y Cable' or " +
                    "@text='Telefonía Móvil' or " +
                    "@text='Tarjeta de Crédito'" +
                    "]"
    );

    /*
     * La cuenta de origen puede ser Corriente o Ahorros.
     */
    private final By sourceAccountLocator = By.xpath(
            "//android.widget.TextView[" +
                    "@text='Cuenta Corriente' or " +
                    "@text='Cuenta Ahorros'" +
                    "]"
    );

    /*
     * El monto es dinámico, por lo que no se utiliza un valor fijo.
     */
    private final By paymentAmountLocator = By.xpath(
            "//android.widget.TextView[starts-with(@text,'$')]"
    );

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Confirmar pago']")
    private WebElement confirmPaymentButton;

    /**
     * Inicializa el Page Object y sus elementos.
     *
     * @param driver instancia activa de AndroidDriver
     */
    public PaymentConfirmationPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Verifica que la pantalla de confirmación esté visible.
     *
     * @return true si aparece el título de confirmación
     */
    public boolean isConfirmationDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, confirmationTitle)
                .isDisplayed();
    }

    /**
     * Obtiene el nombre del servicio mostrado en la confirmación.
     *
     * @return nombre del servicio
     */
    public String getServiceName() {
        return WaitUtils
                .waitForVisibility(driver, serviceNameLocator)
                .getText();
    }

    /**
     * Obtiene la cuenta seleccionada como origen del pago.
     *
     * @return nombre de la cuenta de origen
     */
    public String getSourceAccount() {
        return WaitUtils
                .waitForVisibility(driver, sourceAccountLocator)
                .getText();
    }

    /**
     * Obtiene el monto mostrado en la confirmación.
     *
     * @return monto del pago
     */
    public String getPaymentAmount() {
        return WaitUtils
                .waitForVisibility(driver, paymentAmountLocator)
                .getText();
    }

    /**
     * Confirma el pago.
     */
    public void confirmPayment() {
        WaitUtils
                .waitForClickable(driver, confirmPaymentButton)
                .click();
    }
}