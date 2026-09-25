package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.TestDataProvider;

/**
 * Modulo 1: Login.
 */
public class LoginTest extends BaseTest {

    /**
     * Autenticacion Exitosa
     */
    @Test
    public void shouldLoginSuccessfully() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(
                homePage.isHomeDisplayed(),
                "La pantalla Home no se mostró después del login."
        );
    }
    /**
     * validación email vacío.
     */
    @Test
    public void shouldShowValidationWhenEmailIsEmpty() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterEmail("");
        loginPage.enterPassword(TestDataProvider.VALID_PASSWORD);
        loginPage.tapLogin();

        Assert.assertTrue(
                loginPage.isEmptyFieldsMessageDisplayed(),
                "No se mostró la validación cuando el correo está vacío."
        );
    }

    /**
     * Validación contraseña  vacía.
     */
    @Test
    public void shouldShowValidationWhenPasswordIsEmpty() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterEmail(TestDataProvider.VALID_EMAIL);
        loginPage.enterPassword("");
        loginPage.tapLogin();

        Assert.assertTrue(
                loginPage.isEmptyFieldsMessageDisplayed(),
                "No se mostró la validación cuando la contraseña está vacía."
        );
    }

    /**
     * Funcionalidad  del Toggle de  contraseña
     */

    @Test
    public void shouldTogglePasswordVisibility() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterPassword(
                TestDataProvider.VALID_PASSWORD
        );

        Assert.assertTrue(
                loginPage.isPasswordMasked(),
                "La contraseña debería estar oculta y esta visible."
        );

        loginPage.togglePasswordVisibility();

        Assert.assertFalse(
                loginPage.isPasswordMasked(),
                "La contraseña no es visible después de pulsar el toggle."
        );

        loginPage.togglePasswordVisibility();

        Assert.assertTrue(
                loginPage.isPasswordMasked(),
                "La contraseña no se oculta después de pulsar el toggle."
        );
    }

}