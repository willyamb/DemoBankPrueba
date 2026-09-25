package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

/**
 * Page Object de la pantalla de inicio de sesión.
 *
 * Centraliza los elementos y acciones disponibles en el formulario
 * de autenticación de DemoBank.
 */
public class LoginPage {

    private final AndroidDriver driver;

    @FindBy(xpath = "//android.widget.EditText[@hint='Correo electrónico']")
    private WebElement emailField;

    @FindBy(xpath = "//android.widget.EditText[@hint='Contraseña']")
    private WebElement passwordField;

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Iniciar sesión']")
    private WebElement loginButton;

    /*
     * El mismo mensaje se utiliza para validar los campos obligatorios
     * cuando el correo o la contraseña están vacíos.
     */
    @FindBy(xpath = "//android.widget.TextView[contains(@text,'Ingresa tu correo y contraseña')]")
    private WebElement emptyFieldsMessage;

    /*
     * El icono del ojo está implementado como un TextView dentro
     * de un ViewGroup que actúa como elemento clicable.
     */
    @FindBy(xpath = "//android.widget.EditText[@hint='Contraseña']/following-sibling::android.view.ViewGroup[@clickable='true'][1]")
    private WebElement passwordToggle;

    /**
     * Inicializa el Page Object y sus elementos.
     *
     * @param driver instancia activa de AndroidDriver
     */
    public LoginPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Ingresa el correo electrónico.
     *
     * @param email correo que se desea ingresar
     * @return instancia actual de LoginPage
     */
    public LoginPage enterEmail(String email) {
        WebElement field =
                WaitUtils.waitForVisibility(driver, emailField);

        field.clear();
        field.sendKeys(email);

        return this;
    }

    /**
     * Ingresa la contraseña.
     *
     * @param password contraseña que se desea ingresar
     * @return instancia actual de LoginPage
     */
    public LoginPage enterPassword(String password) {
        WebElement field =
                WaitUtils.waitForVisibility(driver, passwordField);

        field.clear();
        field.sendKeys(password);

        return this;
    }

    /**
     * Pulsa el botón de inicio de sesión.
     */
    public void tapLogin() {
        WaitUtils
                .waitForClickable(driver, loginButton)
                .click();
    }

    /**
     * Ejecuta el flujo completo de inicio de sesión.
     *
     * @param email correo electrónico
     * @param password contraseña
     * @return instancia actual de LoginPage
     */
    public LoginPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        tapLogin();

        return this;
    }

    /**
     * Cambia la visibilidad de la contraseña.
     */
    public void togglePasswordVisibility() {
        WaitUtils
                .waitForClickable(driver, passwordToggle)
                .click();
    }

    /**
     * Verifica si la contraseña está actualmente oculta.
     *
     * La aplicación expone la propiedad "password" del EditText:
     * true cuando está oculta y false cuando está visible.
     *
     * @return true si la contraseña está oculta
     */
    public boolean isPasswordMasked() {
        WebElement field =
                WaitUtils.waitForVisibility(driver, passwordField);

        return Boolean.parseBoolean(
                field.getAttribute("password")
        );
    }

    /**
     * Verifica que el campo de correo esté visible.
     *
     * @return true si el campo está visible
     */
    public boolean isEmailFieldDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, emailField)
                .isDisplayed();
    }

    /**
     * Verifica que el campo de contraseña esté visible.
     *
     * @return true si el campo está visible
     */
    public boolean isPasswordFieldDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, passwordField)
                .isDisplayed();
    }

    /**
     * Verifica que se muestre el mensaje de validación
     * cuando uno de los campos obligatorios está vacío.
     *
     * @return true si el mensaje está visible
     */
    public boolean isEmptyFieldsMessageDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, emptyFieldsMessage)
                .isDisplayed();
    }
    /**
     * Verifica que la pantalla de Login esté visible.
     *
     * @return true cuando el campo de correo electrónico está visible
     */
    public boolean isLoginDisplayed() {

        return WaitUtils
                .waitForVisibility(driver,emailField)
                .isDisplayed();
    }

}