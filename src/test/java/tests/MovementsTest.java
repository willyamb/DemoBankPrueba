package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.MovementsPage;
import utils.TestDataProvider;

import java.util.List;

/**
 * Modulo 3 :Movimientos.
 *
 * Valida la búsqueda de movimientos (case sensitive), los filtros de ingresos
 * y gastos, y el estado vacío cuando no existen coincidencias.
 */
public class MovementsTest extends BaseTest {

    /**
     * Verifica que la búsqueda de movimientos funcione forma (case sensitive)
     */
    @Test
    public void shouldSearchMovementsIgnoringCase() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);
        homePage.tapMovements();

        MovementsPage movementsPage =
                new MovementsPage(driver);

        Assert.assertTrue(
                movementsPage.isMovementsDisplayed(),
                "La pantalla de Movimientos no está visible."
        );

        movementsPage.searchMovement(
                TestDataProvider.MOVEMENT_SEARCH_TERM
        );

        Assert.assertFalse(
                movementsPage.getMovementAmounts().isEmpty(),
                "La búsqueda no encontró movimientos."
        );

        movementsPage.clearSearch();

        movementsPage.searchMovement(
                TestDataProvider.MOVEMENT_SEARCH_TERM.toUpperCase()
        );

        Assert.assertFalse(
                movementsPage.getMovementAmounts().isEmpty(),
                "La búsqueda no ignoró mayúsculas y minúsculas."
        );
    }

    /**
     * Verifica que el filtro avanzado de "Ingresos" muestre únicamente
     * movimientos con importes positivos.
     */
    @Test
    public void shouldDisplayOnlyIncomeMovements() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);
        homePage.tapMovements();

        MovementsPage movementsPage =
                new MovementsPage(driver);

        movementsPage.selectIncome();

        List<String> amounts =
                movementsPage.getMovementAmounts();

        Assert.assertFalse(
                amounts.isEmpty(),
                "No se encontraron movimientos de ingresos."
        );

        for (String amount : amounts) {

            Assert.assertTrue(
                    amount.startsWith("+$"),
                    "Se encontró un importe que no corresponde a un ingreso: "
                            + amount
            );
        }
    }

    /**
     * Verifica que el filtro "Gastos" muestre únicamente
     * movimientos con importes negativos.
     */
    @Test
    public void shouldDisplayOnlyExpenseMovements() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);
        homePage.tapMovements();

        MovementsPage movementsPage =
                new MovementsPage(driver);

        movementsPage.selectExpenses();

        List<String> amounts =
                movementsPage.getMovementAmounts();

        Assert.assertFalse(
                amounts.isEmpty(),
                "No se encontraron movimientos de gastos."
        );

        for (String amount : amounts) {

            Assert.assertTrue(
                    amount.startsWith("-$"),
                    "Se encontró un importe que no corresponde a un gasto: "
                            + amount
            );
        }
    }

    /**
     * Verifica que se muestre el estado vacío cuando
     * la búsqueda no encuentra movimientos coincidentes.
     */
    @Test
    public void shouldDisplayEmptyStateWhenNoMovementMatches() {

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                TestDataProvider.VALID_EMAIL,
                TestDataProvider.VALID_PASSWORD
        );

        HomePage homePage = new HomePage(driver);
        homePage.tapMovements();

        MovementsPage movementsPage =
                new MovementsPage(driver);

        movementsPage.searchMovement(
                TestDataProvider.NO_MATCH_SEARCH_TERM
        );

        Assert.assertTrue(
                movementsPage.isEmptyStateDisplayed(),
                "No se mostró el estado vacío para una búsqueda sin coincidencias."
        );
    }
}