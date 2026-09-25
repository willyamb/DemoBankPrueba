package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

/**
 * Page Object del flujo de transferencias de DemoBank.
 *
 * Centraliza la selección del destinatario, cuenta de origen,
 * monto, nota y validaciones del formulario.
 */
public class TransferPage {

    private final AndroidDriver driver;

    @FindBy(xpath = "//android.widget.TextView[@text='María López']")
    private WebElement recipientList;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'Cuenta Corriente')]")
    private WebElement checkingAccount;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'Cuenta Ahorros')]")
    private WebElement savingsAccount;

    @FindBy(xpath = "//android.widget.EditText[@hint='0.00']")
    private WebElement transferAmount;

    @FindBy(xpath = "//android.widget.EditText[@hint='Ej. Pago de renta']")
    private WebElement transferNote;

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Continuar']")
    private WebElement continueButton;

    @FindBy(xpath = "//android.widget.TextView[@text='Ingresa un monto válido.']")
    private WebElement invalidAmountMessage;

    @FindBy(xpath = "//android.widget.TextView[@text='Saldo insuficiente en la cuenta seleccionada.']")
    private WebElement insufficientBalanceMessage;

    /**
     * Inicializa el Page Object y sus elementos.
     *
     * @param driver instancia activa de AndroidDriver
     */
    public TransferPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Verifica que la pantalla de selección de destinatario
     * esté disponible.
     *
     * @return true si existe al menos un destinatario visible
     */
    public boolean isRecipientListDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, recipientList)
                .isDisplayed();
    }

    /**
     * Selecciona un destinatario por su nombre.
     *
     * El elemento puede tardar en aparecer mientras la lista
     * de destinatarios termina de cargarse.
     *
     * @param recipientName nombre del destinatario
     */
    public void selectRecipient(String recipientName) {

        By recipientLocator = By.xpath(
                "//android.view.ViewGroup[contains(@content-desc,'"
                        + recipientName
                        + "')]"
        );

        WaitUtils
                .waitForClickable(driver, recipientLocator)
                .click();
    }

    /**
     * Verifica que el formulario de transferencia esté disponible.
     *
     * @return true si el campo de monto está visible
     */
    public boolean isTransferFormDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, transferAmount)
                .isDisplayed();
    }

    /**
     * Selecciona Cuenta Corriente como cuenta de origen.
     */
    public void selectCheckingAccount() {
        WaitUtils
                .waitForClickable(driver, checkingAccount)
                .click();
    }

    /**
     * Selecciona Cuenta Ahorros como cuenta de origen.
     */
    public void selectSavingsAccount() {
        WaitUtils
                .waitForClickable(driver, savingsAccount)
                .click();
    }

    /**
     * Ingresa el monto de la transferencia.
     *
     * @param amount monto que se desea transferir
     */
    public void enterAmount(String amount) {
        WebElement field = WaitUtils
                .waitForVisibility(driver, transferAmount);

        field.clear();
        field.sendKeys(amount);
    }

    /**
     * Ingresa una nota para la transferencia.
     *
     * @param note texto de la nota
     */
    public void enterNote(String note) {
        WebElement field = WaitUtils
                .waitForVisibility(driver, transferNote);

        field.clear();
        field.sendKeys(note);
    }

    /**
     * Obtiene el valor actual del campo de monto.
     *
     * @return monto actualmente mostrado
     */
    public String getAmount() {
        return WaitUtils
                .waitForVisibility(driver, transferAmount)
                .getText();
    }

    /**
     * Obtiene el contenido actual del campo de nota.
     *
     * @return nota actualmente mostrada
     */
    public String getNote() {
        return WaitUtils
                .waitForVisibility(driver, transferNote)
                .getText();
    }

    /**
     * Continúa con el proceso de transferencia.
     */
    public void tapContinue() {
        WaitUtils
                .waitForClickable(driver, continueButton)
                .click();
    }

    /**
     * Verifica la validación de monto inválido.
     *
     * @return true si se muestra el mensaje correspondiente
     */
    public boolean isInvalidAmountMessageDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, invalidAmountMessage)
                .isDisplayed();
    }

    /**
     * Verifica la validación cuando el monto supera
     * el saldo disponible de la cuenta seleccionada.
     *
     * @return true si se muestra el mensaje correspondiente
     */
    public boolean isInsufficientBalanceMessageDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, insufficientBalanceMessage)
                .isDisplayed();
    }
}