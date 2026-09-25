package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

/**
 * Page Object de la pantalla de confirmación de una transferencia.
 */
public class TransferConfirmationPage {

    private final AndroidDriver driver;

    @FindBy(xpath = "//android.widget.TextView[@text='Confirma tu transferencia']")
    private WebElement confirmationTitle;

    @FindBy(xpath = "//android.widget.TextView[@text='María López']")
    private WebElement recipientName;

    @FindBy(xpath = "//android.widget.TextView[@text='Cuenta Corriente']")
    private WebElement sourceAccount;

    /*
     * El monto es dinámico, por lo que se identifica por el formato
     * monetario y no por un valor concreto.
     */
    @FindBy(xpath = "//android.widget.TextView[starts-with(@text,'$')]")
    private WebElement transferAmount;

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Confirmar transferencia']")
    private WebElement confirmTransferButton;

    /**
     * Inicializa el Page Object.
     *
     * @param driver instancia activa de AndroidDriver
     */
    public TransferConfirmationPage(AndroidDriver driver) {
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
     * Obtiene el nombre del destinatario mostrado.
     *
     * @return nombre del destinatario
     */
    public String getRecipientName() {
        return WaitUtils
                .waitForVisibility(driver, recipientName)
                .getText();
    }

    /**
     * Obtiene la cuenta utilizada como origen.
     *
     * @return nombre de la cuenta de origen
     */
    public String getSourceAccount() {
        return WaitUtils
                .waitForVisibility(driver, sourceAccount)
                .getText();
    }

    /**
     * Obtiene el monto mostrado en la confirmación.
     *
     * @return monto de la transferencia
     */
    public String getTransferAmount() {
        return WaitUtils
                .waitForVisibility(driver, transferAmount)
                .getText();
    }

    /**
     * Confirma la transferencia.
     */
    public void confirmTransfer() {
        WaitUtils
                .waitForClickable(driver, confirmTransferButton)
                .click();
    }
}