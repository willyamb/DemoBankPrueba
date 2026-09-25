package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.TransferConfirmationPage;
import pages.TransferPage;
import pages.TransferSuccessPage;
import utils.TestDataProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Modulo4: Transferencias
 *
 * Valida transferencias exitosas, validaciones de monto,
 * validaciones de saldo insuficiente y el impacto de la
 * transferencia sobre el saldo de la cuenta origen mediante OCR.
 */
public class TransferTest extends BaseTest {

    /**
     * Verifica que el usuario pueda completar una transferencia
     * y visualizar correctamente la pantalla de éxito.
     */
    @Test
    public void shouldCompleteTransferSuccessfully() {

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

        homePage.tapTransfer();

        TransferPage transferPage = new TransferPage(driver);

        Assert.assertTrue(
                transferPage.isRecipientListDisplayed(),
                "No se mostró la lista de destinatarios."
        );

        transferPage.selectRecipient(
                TestDataProvider.TRANSFER_RECIPIENT
        );

        Assert.assertTrue(
                transferPage.isTransferFormDisplayed(),
                "No se mostró el formulario de transferencia."
        );

        transferPage.selectCheckingAccount();

        transferPage.enterAmount(
                TestDataProvider.TRANSFER_AMOUNT
        );

        transferPage.enterNote(
                TestDataProvider.TRANSFER_NOTE
        );

        transferPage.tapContinue();

        TransferConfirmationPage confirmationPage =
                new TransferConfirmationPage(driver);

        Assert.assertTrue(
                confirmationPage.isConfirmationDisplayed(),
                "No se mostró la pantalla de confirmación."
        );

        Assert.assertEquals(
                confirmationPage.getRecipientName(),
                TestDataProvider.TRANSFER_RECIPIENT,
                "El destinatario mostrado no corresponde al seleccionado."
        );

        Assert.assertEquals(
                confirmationPage.getSourceAccount(),
                TestDataProvider.CHECKING_ACCOUNT,
                "La cuenta de origen no corresponde a la seleccionada."
        );

        Assert.assertTrue(
                confirmationPage.getTransferAmount()
                        .contains(TestDataProvider.TRANSFER_AMOUNT),
                "El monto mostrado en la confirmación no corresponde al ingresado."
        );

        confirmationPage.confirmTransfer();

        TransferSuccessPage successPage =
                new TransferSuccessPage(driver);

        Assert.assertTrue(
                successPage.isSuccessDisplayed(),
                "No se mostró la pantalla de transferencia exitosa."
        );

        Assert.assertTrue(
                successPage.getTransferSummary()
                        .contains(TestDataProvider.TRANSFER_RECIPIENT),
                "El destinatario no aparece en el resumen de la transferencia."
        );

        Assert.assertTrue(
                successPage.getTransferSummary()
                        .contains(TestDataProvider.TRANSFER_AMOUNT),
                "El monto no aparece correctamente en el resumen."
        );
    }

    /**
     * Verifica que la aplicación rechace una transferencia
     * cuando el monto ingresado es cero.
     */
    @Test
    public void shouldShowValidationForInvalidTransferAmount() {

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

        homePage.tapTransfer();

        TransferPage transferPage =
                new TransferPage(driver);

        transferPage.selectRecipient(
                TestDataProvider.TRANSFER_RECIPIENT
        );

        transferPage.selectCheckingAccount();

        transferPage.enterAmount(
                TestDataProvider.INVALID_AMOUNT
        );

        transferPage.tapContinue();

        Assert.assertTrue(
                transferPage.isInvalidAmountMessageDisplayed(),
                "No se mostró la validación para un monto inválido."
        );
    }

    /**
     * Verifica que la aplicación rechace una transferencia
     * cuando el monto supera el saldo disponible.
     */
    @Test
    public void shouldShowValidationForInsufficientBalance() {

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

        homePage.tapTransfer();

        TransferPage transferPage =
                new TransferPage(driver);

        transferPage.selectRecipient(
                TestDataProvider.TRANSFER_RECIPIENT
        );

        transferPage.selectCheckingAccount();

        transferPage.enterAmount(
                TestDataProvider.INSUFFICIENT_BALANCE_TRANSFER
        );

        transferPage.tapContinue();

        Assert.assertTrue(
                transferPage.isInsufficientBalanceMessageDisplayed(),
                "No se mostró la validación de saldo insuficiente."
        );
    }

    /**
     * Verifica mediante OCR que el saldo de la Cuenta Corriente
     * se descuente exactamente después de una transferencia.
     *
     * Flujo:
     * Login
     *   ↓
     * Home
     *   ↓
     * Cuenta Corriente
     *   ↓
     * OCR saldo antes
     *   ↓
     * Transferencia
     *   ↓
     * Confirmación
     *   ↓
     * Éxito
     *   ↓
     * Home
     *   ↓
     * OCR saldo después
     *   ↓
     * saldoAntes - monto = saldoDespués
     */
    @Test
    public void shouldValidateSourceBalanceImpactUsingOCR() {

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

        /*
         * Seleccionamos explícitamente la cuenta origen.
         */
        homePage.selectCheckingAccount();

        /*
         * =========================
         * SALDO PRE - OCR
         * =========================
         */
        String balanceBeforeOCR =
                homePage.getSelectedAccountDetailsByOCR();

        double balanceBefore =
                extractBalance(balanceBeforeOCR);

        /*
         * =========================
         * TRANSFERENCIA
         * =========================
         */
        homePage.tapTransfer();

        TransferPage transferPage =
                new TransferPage(driver);

        Assert.assertTrue(
                transferPage.isRecipientListDisplayed(),
                "No se mostró la lista de destinatarios."
        );

        transferPage.selectRecipient(
                TestDataProvider.TRANSFER_RECIPIENT
        );

        Assert.assertTrue(
                transferPage.isTransferFormDisplayed(),
                "No se mostró el formulario de transferencia."
        );

        transferPage.selectCheckingAccount();

        transferPage.enterAmount(
                TestDataProvider.TRANSFER_AMOUNT
        );

        transferPage.enterNote(
                TestDataProvider.TRANSFER_NOTE
        );

        transferPage.tapContinue();

        TransferConfirmationPage confirmationPage =
                new TransferConfirmationPage(driver);

        Assert.assertTrue(
                confirmationPage.isConfirmationDisplayed(),
                "No se mostró la pantalla de confirmación."
        );

        confirmationPage.confirmTransfer();

        /*
         * =========================
         * PANTALLA DE ÉXITO
         * =========================
         */
        TransferSuccessPage successPage =
                new TransferSuccessPage(driver);

        Assert.assertTrue(
                successPage.isSuccessDisplayed(),
                "No se mostró la pantalla de transferencia exitosa."
        );

        /*
         * Regresamos a Home.
         */
        successPage.tapBackToHome();

        Assert.assertTrue(
                homePage.isHomeDisplayed(),
                "No se pudo regresar a la pantalla Home."
        );

        /*
         * Seleccionamos nuevamente la cuenta origen.
         */
        homePage.selectCheckingAccount();

        /*
         * =========================
         * SALDO POST - OCR
         * =========================
         */
        String balanceAfterOCR =
                homePage.getSelectedAccountDetailsByOCR();

        double balanceAfter =
                extractBalance(balanceAfterOCR);

        /*
         * Convertimos el monto de transferencia
         * definido en TestDataProvider.
         */
        double transferAmount =
                Double.parseDouble(
                        TestDataProvider.TRANSFER_AMOUNT
                );

        /*
         * Calculamos el saldo que debería existir
         * después de la transferencia.
         */
        double expectedBalance =
                balanceBefore - transferAmount;

        /*
         * =========================
         * VALIDACIÓN FINAL
         * =========================
         */
        Assert.assertEquals(
                balanceAfter,
                expectedBalance,
                0.01,
                "El saldo de la Cuenta Corriente no fue "
                        + "descontado correctamente después "
                        + "de la transferencia."
        );
    }

    /**
     * Extrae un valor monetario desde el texto reconocido
     * mediante OCR.
     *
     * Ejemplo:
     *
     * **** 4821 · $1500000.00
     *
     * devuelve:
     *
     * 1500000.00
     *
     * @param ocrText texto obtenido mediante Tesseract
     * @return saldo numérico
     */
    private double extractBalance(String ocrText) {

        Assert.assertNotNull(
                ocrText,
                "El resultado del OCR no puede ser null."
        );

        Assert.assertFalse(
                ocrText.trim().isEmpty(),
                "El resultado del OCR está vacío."
        );

        Pattern pattern =
                Pattern.compile(
                        "(\\d[\\d,]*\\.\\d{2})"
                );

        Matcher matcher =
                pattern.matcher(ocrText);

        Assert.assertTrue(
                matcher.find(),
                "No se encontró un valor monetario en el texto OCR: ["
                        + ocrText + "]"
        );

        String numericValue =
                matcher.group(1)
                        .replace(",", "");

        try {
            return Double.parseDouble(numericValue);

        } catch (NumberFormatException e) {

            Assert.fail(
                    "No fue posible convertir el valor OCR a número: ["
                            + numericValue
                            + "]"
            );

            return 0.0;
        }
    }
}