package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.PaymentConfirmationPage;
import pages.PaymentPage;
import pages.PaymentSuccessPage;
import utils.TestDataProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Modulo 5: Pagos de Servicios.
 *
 * Valida pagos exitosos, montos precargados, validaciones
 * de monto y saldo insuficiente, y el impacto del pago
 * sobre el saldo de la cuenta origen mediante OCR.
 */
public class PaymentTest extends BaseTest {

    /**
     * Verifica que el usuario pueda completar correctamente
     * un pago de Energía Eléctrica.
     */
    @Test
    public void shouldCompletePaymentSuccessfully() {

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

        homePage.tapPay();

        PaymentPage paymentPage = new PaymentPage(driver);

        Assert.assertTrue(
                paymentPage.isServiceListDisplayed(),
                "No se mostró la lista de servicios."
        );

        paymentPage.selectService(
                TestDataProvider.PAYMENT_SERVICE
        );

        Assert.assertTrue(
                paymentPage.isPaymentFormDisplayed(),
                "No se mostró el formulario de pago."
        );

        String preloadedAmount =
                paymentPage.getPreloadedAmount();

        Assert.assertFalse(
                preloadedAmount.isEmpty(),
                "El servicio no presenta un monto precargado."
        );

        Assert.assertEquals(
                preloadedAmount,
                TestDataProvider.PAYMENT_AMOUNT,
                "El monto precargado no es el esperado."
        );

        paymentPage.selectCheckingAccount();

        paymentPage.enterAmount(
                TestDataProvider.PAYMENT_AMOUNT
        );

        paymentPage.enterNote(
                TestDataProvider.PAYMENT_NOTE
        );

        paymentPage.tapContinue();

        PaymentConfirmationPage confirmationPage =
                new PaymentConfirmationPage(driver);

        Assert.assertTrue(
                confirmationPage.isConfirmationDisplayed(),
                "No se mostró la pantalla de confirmación del pago."
        );

        Assert.assertEquals(
                confirmationPage.getServiceName(),
                TestDataProvider.PAYMENT_SERVICE,
                "El servicio mostrado no corresponde al seleccionado."
        );

        Assert.assertEquals(
                confirmationPage.getSourceAccount(),
                TestDataProvider.CHECKING_ACCOUNT,
                "La cuenta de origen no corresponde a la seleccionada."
        );

        Assert.assertTrue(
                confirmationPage.getPaymentAmount()
                        .contains(TestDataProvider.PAYMENT_AMOUNT),
                "El monto mostrado en la confirmación no corresponde al ingresado."
        );

        confirmationPage.confirmPayment();

        PaymentSuccessPage successPage =
                new PaymentSuccessPage(driver);

        Assert.assertTrue(
                successPage.isSuccessDisplayed(),
                "No se mostró la pantalla de pago exitoso."
        );

        Assert.assertTrue(
                successPage.getPaymentSummary()
                        .contains(TestDataProvider.PAYMENT_SERVICE),
                "El servicio no aparece en el resumen del pago."
        );

        Assert.assertTrue(
                successPage.getPaymentSummary()
                        .contains(TestDataProvider.PAYMENT_AMOUNT),
                "El monto no aparece correctamente en el resumen del pago."
        );
    }

    /**
     * Verifica que la aplicación rechace un pago
     * cuando el monto ingresado es cero.
     */
    @Test
    public void shouldShowValidationForInvalidPaymentAmount() {

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

        homePage.tapPay();

        PaymentPage paymentPage =
                new PaymentPage(driver);

        paymentPage.selectService(
                TestDataProvider.PAYMENT_SERVICE
        );

        paymentPage.selectCheckingAccount();

        paymentPage.enterAmount(
                TestDataProvider.INVALID_AMOUNT
        );

        paymentPage.tapContinue();

        Assert.assertTrue(
                paymentPage.isInvalidAmountMessageDisplayed(),
                "No se mostró la validación para un monto inválido."
        );
    }

    /**
     * Verifica que la aplicación rechace un pago
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

        homePage.tapPay();

        PaymentPage paymentPage =
                new PaymentPage(driver);

        paymentPage.selectService(
                TestDataProvider.PAYMENT_SERVICE
        );

        paymentPage.selectCheckingAccount();

        paymentPage.enterAmount(
                TestDataProvider.INSUFFICIENT_BALANCE_PAYMENT
        );

        paymentPage.tapContinue();

        Assert.assertTrue(
                paymentPage.isInsufficientBalanceMessageDisplayed(),
                "No se mostró la validación de saldo insuficiente."
        );
    }

    /**
     * Verifica mediante OCR que el saldo de la Cuenta Corriente
     * se descuente exactamente después de realizar un pago.
     *
     * Este caso constituye la tercera validación mediante
     * Tesseract OCR requerida por la prueba.
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
         * Seleccionamos la cuenta origen.
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
         * FLUJO DE PAGO
         * =========================
         */
        homePage.tapPay();

        PaymentPage paymentPage =
                new PaymentPage(driver);

        Assert.assertTrue(
                paymentPage.isServiceListDisplayed(),
                "No se mostró la lista de servicios."
        );

        paymentPage.selectService(
                TestDataProvider.PAYMENT_SERVICE
        );

        Assert.assertTrue(
                paymentPage.isPaymentFormDisplayed(),
                "No se mostró el formulario de pago."
        );

        paymentPage.selectCheckingAccount();

        paymentPage.enterAmount(
                TestDataProvider.PAYMENT_AMOUNT
        );

        paymentPage.enterNote(
                TestDataProvider.PAYMENT_NOTE
        );

        paymentPage.tapContinue();

        PaymentConfirmationPage confirmationPage =
                new PaymentConfirmationPage(driver);

        Assert.assertTrue(
                confirmationPage.isConfirmationDisplayed(),
                "No se mostró la pantalla de confirmación del pago."
        );

        confirmationPage.confirmPayment();

        /*
         * =========================
         * PANTALLA DE ÉXITO
         * =========================
         */
        PaymentSuccessPage successPage =
                new PaymentSuccessPage(driver);

        Assert.assertTrue(
                successPage.isSuccessDisplayed(),
                "No se mostró la pantalla de pago exitoso."
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
         * Convertimos el monto del pago a número.
         */
        double paymentAmount =
                Double.parseDouble(
                        TestDataProvider.PAYMENT_AMOUNT
                );

        /*
         * =========================
         * SALDO ESPERADO
         * =========================
         */
        double expectedBalance =
                balanceBefore - paymentAmount;

        /*
         * =========================
         * VALIDACIÓN
         * =========================
         */
        Assert.assertEquals(
                balanceAfter,
                expectedBalance,
                0.01,
                "El saldo de la Cuenta Corriente no fue "
                        + "descontado correctamente después del pago."
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
                "No se encontró un valor monetario "
                        + "en el texto OCR: ["
                        + ocrText
                        + "]"
        );

        String numericValue =
                matcher.group(1)
                        .replace(",", "");

        try {

            return Double.parseDouble(numericValue);

        } catch (NumberFormatException e) {

            Assert.fail(
                    "No fue posible convertir el valor OCR "
                            + "a número: ["
                            + numericValue
                            + "]"
            );

            return 0.0;
        }
    }
}