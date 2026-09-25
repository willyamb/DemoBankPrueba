package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

/**
 * Page Object del flujo de pagos de DemoBank.
 *
 * Centraliza la selección del servicio, cuenta de origen,
 * monto, nota y validaciones del formulario de pago.
 */
public class PaymentPage {

    private final AndroidDriver driver;

    @FindBy(xpath = "//android.widget.TextView[@text='Energía Eléctrica']")
    private WebElement serviceList;

    @FindBy(xpath = "//android.widget.TextView[@text='Monto a pagar']")
    private WebElement paymentAmountTitle;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'Cuenta Corriente')]")
    private WebElement checkingAccount;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'Cuenta Ahorros')]")
    private WebElement savingsAccount;

    /*
     * El monto es dinámico y puede venir precargado según el servicio.
     * Por eso se identifica mediante el hint y no por su valor.
     */
    @FindBy(xpath = "//android.widget.EditText[@hint='0.00']")
    private WebElement paymentAmount;

    @FindBy(xpath = "//android.widget.EditText[@hint='Ej. Pago de julio']")
    private WebElement paymentNote;

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
    public PaymentPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Verifica que la pantalla de selección de servicios esté visible.
     *
     * @return true si existe un servicio disponible
     */
    public boolean isServiceListDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, serviceList)
                .isDisplayed();
    }

    /**
     * Selecciona un servicio por su nombre.
     *
     * El nombre forma parte del content-desc del contenedor
     * clicable de cada servicio.
     *
     * @param serviceName nombre del servicio
     */
    public void selectService(String serviceName) {

        By serviceLocator = By.xpath(
                "//android.view.ViewGroup[contains(@content-desc,'"
                        + serviceName
                        + "')]"
        );

        WaitUtils
                .waitForClickable(driver, serviceLocator)
                .click();
    }

    /**
     * Verifica que el formulario de pago esté disponible.
     *
     * @return true si el encabezado "Monto a pagar" está visible
     */
    public boolean isPaymentFormDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, paymentAmountTitle)
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
     * Obtiene el monto precargado por la aplicación.
     *
     * Este valor debe ser leído antes de modificarlo para
     * validar posteriormente la precarga dinámica del servicio.
     *
     * @return monto actualmente mostrado
     */
    public String getPreloadedAmount() {
        return WaitUtils
                .waitForVisibility(driver, paymentAmount)
                .getText();
    }

    /**
     * Ingresa un monto de pago.
     *
     * @param amount monto que se desea pagar
     */
    public void enterAmount(String amount) {
        WebElement field = WaitUtils
                .waitForVisibility(driver, paymentAmount);

        field.clear();
        field.sendKeys(amount);
    }

    /**
     * Obtiene el monto actualmente ingresado.
     *
     * @return monto mostrado en el campo
     */
    public String getAmount() {
        return WaitUtils
                .waitForVisibility(driver, paymentAmount)
                .getText();
    }

    /**
     * Ingresa una nota para el pago.
     *
     * @param note texto de la nota
     */
    public void enterNote(String note) {
        WebElement field = WaitUtils
                .waitForVisibility(driver, paymentNote);

        field.clear();
        field.sendKeys(note);
    }

    /**
     * Obtiene la nota actualmente ingresada.
     *
     * @return nota mostrada en el campo
     */
    public String getNote() {
        return WaitUtils
                .waitForVisibility(driver, paymentNote)
                .getText();
    }

    /**
     * Continúa con el proceso de pago.
     */
    public void tapContinue() {
        WaitUtils
                .waitForClickable(driver, continueButton)
                .click();
    }

    /**
     * Verifica la validación de monto inválido.
     *
     * @return true si aparece el mensaje correspondiente
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
     * @return true si aparece el mensaje correspondiente
     */
    public boolean isInsufficientBalanceMessageDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, insufficientBalanceMessage)
                .isDisplayed();
    }
}