package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.MovementsPage;
import pages.PaymentPage;
import pages.TransferPage;
import utils.TestDataProvider;

/**
 * Modulo 2: Home.
 *
 * Valida el saldo consolidado mediante OCR, la interactividad
 * de las cuentas, los accesos rápidos y el cierre de sesión.
 */
public class HomeTest extends BaseTest {

    /**
     * Valida que el saldo total coincida con la suma de las cuentas,
     * utilizando OCR para leer el valor mostrado en el componente gráfico.
     */
    @Test
    public void shouldValidateConsolidatedBalanceUsingOCR() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(
                homePage.isHomeDisplayed(),
                "La pantalla Home no está visible."
        );

        // Capturamos el saldo consolidado mediante OCR.
        String ocrBalance =
                homePage.getTotalBalanceByOCR();

        // Normalizamos el texto reconocido por Tesseract.
        String normalizedBalance = ocrBalance
                .replace("$", "")
                .replace(",", "")
                .replaceAll("\\s+", "")
                .trim();

        double actualBalance;

        try {

            actualBalance =
                    Double.parseDouble(normalizedBalance);

        } catch (NumberFormatException e) {

            Assert.fail(
                    "No fue posible interpretar el saldo obtenido mediante OCR: ["
                            + ocrBalance
                            + "]",
                    e
            );

            return;
        }

        // Calculamos el saldo esperado sumando las cuentas individuales.
        double expectedBalance =
                TestDataProvider.CHECKING_BALANCE
                        + TestDataProvider.SAVINGS_BALANCE;

        Assert.assertEquals(
                actualBalance,
                expectedBalance,
                0.01,
                "El saldo consolidado obtenido mediante OCR "
                        + "no coincide con la suma de las cuentas."
        );
    }

    /**
     * Verifica que el usuario pueda cambiar entre las pestañas de las
     * cuentas disponibles y que la información mostrada se actualice.
     */
    @Test
    public void shouldSwitchBetweenAccounts() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(
                homePage.isHomeDisplayed(),
                "La pantalla Home no está visible."
        );

        // Seleccionamos Cuenta Corriente.
        homePage.selectCheckingAccount();

        String checkingDetails =
                homePage.getSelectedAccountDetails();

        Assert.assertTrue(
                checkingDetails.contains(
                        TestDataProvider.CHECKING_ACCOUNT_SUFFIX
                ),
                "La Cuenta Corriente no muestra la información esperada."
        );

        // Seleccionamos Cuenta Ahorros.
        homePage.selectSavingsAccount();

        String savingsDetails =
                homePage.getSelectedAccountDetails();

        Assert.assertTrue(
                savingsDetails.contains(
                        TestDataProvider.SAVINGS_ACCOUNT_SUFFIX
                ),
                "La Cuenta Ahorros no muestra la información esperada."
        );

        Assert.assertNotEquals(
                savingsDetails,
                checkingDetails,
                "La información de la cuenta no se actualizó "
                        + "al cambiar de cuenta."
        );

        // Regresamos a Cuenta Corriente.
        homePage.selectCheckingAccount();

        String checkingDetailsAgain =
                homePage.getSelectedAccountDetails();

        Assert.assertTrue(
                checkingDetailsAgain.contains(
                        TestDataProvider.CHECKING_ACCOUNT_SUFFIX
                ),
                "No se pudo regresar correctamente a Cuenta Corriente."
        );
    }

    /**
     * Verifica que el acceso rápido "Transferir" redirija
     * correctamente al flujo de transferencias.
     */
    @Test
    public void shouldNavigateToTransferFromQuickAccess() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(
                homePage.isHomeDisplayed(),
                "La pantalla Home no está visible."
        );

        // Pulsamos el acceso rápido Transferir.
        homePage.tapTransfer();

        TransferPage transferPage =
                new TransferPage(driver);

        Assert.assertTrue(
                transferPage.isRecipientListDisplayed(),
                "El acceso rápido Transferir no redirigió "
                        + "correctamente a la pantalla de transferencias."
        );
    }

    /**
     * Verifica que el acceso rápido "Pagar" redirija
     * correctamente al flujo de pagos.
     */
    @Test
    public void shouldNavigateToPaymentFromQuickAccess() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(
                homePage.isHomeDisplayed(),
                "La pantalla Home no está visible."
        );

        // Pulsamos el acceso rápido Pagar.
        homePage.tapPay();

        PaymentPage paymentPage =
                new PaymentPage(driver);

        Assert.assertTrue(
                paymentPage.isServiceListDisplayed(),
                "El acceso rápido Pagar no redirigió "
                        + "correctamente a la pantalla de pagos."
        );
    }

    /**
     * Verifica que el acceso rápido "Movimientos" redirija
     * correctamente al módulo de movimientos.
     */
    @Test
    public void shouldNavigateToMovementsFromQuickAccess() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(
                homePage.isHomeDisplayed(),
                "La pantalla Home no está visible."
        );

        // Pulsamos el acceso rápido Movimientos.
        homePage.tapMovements();

        MovementsPage movementsPage =
                new MovementsPage(driver);

        Assert.assertTrue(
                movementsPage.isMovementsDisplayed(),
                "El acceso rápido Movimientos no redirigió "
                        + "correctamente al módulo de movimientos."
        );
    }

    /**
     * Verifica que el usuario pueda cerrar sesión desde Home
     * y regresar correctamente a la pantalla Login.
     */
    @Test
    public void shouldLogoutAndReturnToLogin() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(
                homePage.isHomeDisplayed(),
                "La pantalla Home no está visible antes del Logout."
        );

        // Ejecutamos la acción de cierre de sesión.
        homePage.logout();

        // Verificamos que la aplicación regrese a Login.
        Assert.assertTrue(
                loginPage.isLoginDisplayed(),
                "Después del Logout la aplicación no regresó a la pantalla Login."
        );
    }
}