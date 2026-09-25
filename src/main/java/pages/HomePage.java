package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.OCRUtils;
import utils.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import java.time.Duration;
import java.util.Collections;

/**
 * Page Object de la pantalla principal de DemoBank.
 *
 * Centraliza los elementos y acciones disponibles en Home:
 * selección de cuentas, consulta de información, accesos rápidos,
 * navegación inferior y cierre de sesión.
 */
public class HomePage {

    private final AndroidDriver driver;

    @FindBy(xpath = "//android.widget.TextView[@text='Saldo total']")
    private WebElement totalBalanceTitle;

    /*
     * Importe correspondiente al saldo consolidado.
     *
     * La lectura de este valor se realizará mediante OCR para cumplir
     * con el requisito de validación visual definido para este componente.
     */
    @FindBy(xpath = "//android.widget.TextView[@text='Saldo total']/following-sibling::android.widget.TextView[1]")
    private WebElement totalBalanceAmount;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'Cuenta Corriente')]")
    private WebElement checkingAccount;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'Cuenta Ahorros')]")
    private WebElement savingsAccount;

    /*
     * El elemento contiene el número enmascarado y el saldo
     * de la cuenta actualmente seleccionada.
     *
     * Ejemplos:
     * **** 4821 · $1474869.20
     * **** 7735 · $955450.00
     *
     * No se utiliza el saldo en el locator porque cambia
     * después de realizar operaciones.
     */
    @FindBy(xpath = "//android.widget.TextView[starts-with(@text,'****')]")
    private WebElement selectedAccountDetails;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'Transferir')]")
    private WebElement transferButton;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'Movimientos')]")
    private WebElement movementsButton;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'Pagar')]")
    private WebElement payButton;

    @FindBy(xpath = "//android.view.ViewGroup[contains(@content-desc,'Más')]")
    private WebElement moreButton;

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Ver todos']")
    private WebElement viewAllButton;

    /*
     * La aplicación expone actualmente la acción de salida
     * mediante el icono presente en content-desc.
     */
    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='']")
    private WebElement logoutButton;

    @FindBy(xpath = "//android.view.View[contains(@content-desc,'Inicio')]")
    private WebElement bottomHomeButton;

    /*
     * Se diferencia del acceso rápido superior de Movimientos
     * porque el elemento de navegación inferior es android.view.View.
     */
    @FindBy(xpath = "//android.view.View[contains(@content-desc,'Movimientos')]")
    private WebElement bottomMovementsButton;

    /**
     * Inicializa el Page Object.
     *
     * @param driver instancia activa de AndroidDriver
     */
    public HomePage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Verifica que la pantalla Home esté disponible.
     *
     * @return true cuando el título "Saldo total" es visible
     */
    public boolean isHomeDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, totalBalanceTitle)
                .isDisplayed();
    }

    /**
     * Obtiene el saldo consolidado mediante OCR.
     *
     * Se captura visualmente el importe mostrado en pantalla
     * y se procesa mediante Tesseract.
     *
     * @return texto reconocido por OCR
     */
    public String getTotalBalanceByOCR() {
        WebElement balance =
                WaitUtils.waitForVisibility(driver, totalBalanceAmount);

        return OCRUtils.extractTextFromElement(balance);
    }

    /**
     * Selecciona Cuenta Corriente.
     */
    public void selectCheckingAccount() {
        WaitUtils
                .waitForClickable(driver, checkingAccount)
                .click();
    }

    /**
     * Selecciona Cuenta Ahorros.
     */
    public void selectSavingsAccount() {
        WaitUtils
                .waitForClickable(driver, savingsAccount)
                .click();
    }

    /**
     * Obtiene el número enmascarado y el saldo
     * de la cuenta actualmente seleccionada.
     *
     * @return información de la cuenta seleccionada
     */
    public String getSelectedAccountDetails() {
        return WaitUtils
                .waitForVisibility(driver, selectedAccountDetails)
                .getText();
    }

    /**
     * Obtiene mediante OCR la información visual de la cuenta
     * actualmente seleccionada.
     *
     * @return texto reconocido por Tesseract
     */
    public String getSelectedAccountDetailsByOCR() {

        WebElement accountDetails =
                WaitUtils.waitForVisibility(
                        driver,
                        selectedAccountDetails
                );

        return OCRUtils.extractTextFromElement(accountDetails);
    }

    /**
     * Abre el flujo de Transferir.
     */
    public void tapTransfer() {
        WaitUtils
                .waitForClickable(driver, transferButton)
                .click();
    }

    /**
     * Abre Movimientos desde el acceso rápido de Home.
     */
    public void tapMovements() {
        WaitUtils
                .waitForClickable(driver, movementsButton)
                .click();
    }

    /**
     * Abre el flujo de Pagos.
     */
    public void tapPay() {
        WaitUtils
                .waitForClickable(driver, payButton)
                .click();
    }

    /**
     * Pulsa la opción "Más".
     *
     * Actualmente la aplicación no realiza navegación
     * desde esta opción.
     */
    public void tapMore() {
        WaitUtils
                .waitForClickable(driver, moreButton)
                .click();
    }

    /**
     * Abre la lista completa mediante "Ver todos".
     */
    public void tapViewAll() {
        WaitUtils
                .waitForClickable(driver, viewAllButton)
                .click();
    }

    /**
     * Selecciona la pestaña inferior "Inicio".
     */
    public void tapBottomHome() {
        WaitUtils
                .waitForClickable(driver, bottomHomeButton)
                .click();
    }

    /**
     * Selecciona la pestaña inferior "Movimientos".
     */
    public void tapBottomMovements() {
        WaitUtils
                .waitForClickable(driver, bottomMovementsButton)
                .click();
    }

    /**
     * Cierra la sesión desde Home.
     */
    public void logout() {

        WebElement element =
                WaitUtils.waitForClickable(driver, logoutButton);

        PointerInput finger =
                new PointerInput(
                        PointerInput.Kind.TOUCH,
                        "finger"
                );

        Sequence tap =
                new Sequence(finger, 0);

        int centerX =
                element.getRect().getWidth() / 2;

        int centerY =
                element.getRect().getHeight() / 2;

        tap.addAction(
                finger.createPointerMove(
                        Duration.ZERO,
                        PointerInput.Origin.fromElement(element),
                        centerX,
                        centerY
                )
        );

        tap.addAction(
                finger.createPointerDown(
                        PointerInput.MouseButton.LEFT.asArg()
                )
        );

        tap.addAction(
                finger.createPointerUp(
                        PointerInput.MouseButton.LEFT.asArg()
                )
        );

        driver.perform(
                Collections.singletonList(tap)
        );
    }

}