package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object de la pantalla de Movimientos de DemoBank.
 *
 * Centraliza la búsqueda, los filtros y la consulta de los
 * movimientos mostrados en la pantalla.
 */
public class MovementsPage {

    private final AndroidDriver driver;

    @FindBy(xpath = "//android.widget.EditText[@hint='Buscar movimiento']")
    private WebElement searchField;

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Todos']")
    private WebElement allFilter;

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Ingresos']")
    private WebElement incomeFilter;

    @FindBy(xpath = "//android.view.ViewGroup[@content-desc='Gastos']")
    private WebElement expenseFilter;

    @FindBy(xpath = "//android.widget.TextView[@text='No hay movimientos que coincidan']")
    private WebElement emptyStateMessage;

    @FindBy(xpath = "//android.view.View[contains(@content-desc,'Inicio')]")
    private WebElement bottomHomeButton;

    @FindBy(xpath = "//android.view.View[contains(@content-desc,'Movimientos')]")
    private WebElement bottomMovementsButton;

    /*
     * Los importes se identifican por su signo para no depender
     * de valores concretos que puedan cambiar durante las pruebas.
     */
    private final By movementAmountsLocator = By.xpath(
            "//android.widget.TextView[" +
                    "starts-with(@text,'+$') or starts-with(@text,'-$')" +
                    "]"
    );

    /**
     * Inicializa el Page Object y sus elementos.
     *
     * @param driver instancia activa de AndroidDriver
     */
    public MovementsPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Verifica que la pantalla de Movimientos esté disponible.
     *
     * @return true si el campo de búsqueda está visible
     */
    public boolean isMovementsDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, searchField)
                .isDisplayed();
    }

    /**
     * Realiza una búsqueda de movimientos.
     *
     * @param searchTerm texto que se desea buscar
     */
    public void searchMovement(String searchTerm) {
        WebElement field = WaitUtils
                .waitForVisibility(driver, searchField);

        field.clear();
        field.sendKeys(searchTerm);
    }

    /**
     * Limpia el campo de búsqueda.
     */
    public void clearSearch() {
        WebElement field = WaitUtils
                .waitForVisibility(driver, searchField);

        field.clear();
    }

    /**
     * Selecciona el filtro "Todos".
     */
    public void selectAll() {
        WaitUtils
                .waitForClickable(driver, allFilter)
                .click();
    }

    /**
     * Selecciona el filtro "Ingresos".
     */
    public void selectIncome() {
        WaitUtils
                .waitForClickable(driver, incomeFilter)
                .click();
    }

    /**
     * Selecciona el filtro "Gastos".
     */
    public void selectExpenses() {
        WaitUtils
                .waitForClickable(driver, expenseFilter)
                .click();
    }

    /**
     * Obtiene los importes de los movimientos visibles.
     *
     * La aplicación puede reconstruir dinámicamente los elementos
     * después de aplicar un filtro. En ese caso, los WebElement
     * obtenidos previamente pueden quedar obsoletos, por lo que
     * se realiza un nuevo intento de lectura.
     *
     * @return lista de importes visibles
     */
    public List<String> getMovementAmounts() {

        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {

            try {
                List<WebElement> elements =
                        driver.findElements(movementAmountsLocator);

                return elements.stream()
                        .map(WebElement::getText)
                        .collect(Collectors.toList());

            } catch (StaleElementReferenceException e) {

                if (attempt == maxAttempts) {
                    throw e;
                }
            }
        }

        return List.of();
    }

    /**
     * Verifica si se muestra el estado vacío.
     *
     * @return true cuando no existen movimientos que coincidan
     */
    public boolean isEmptyStateDisplayed() {
        return WaitUtils
                .waitForVisibility(driver, emptyStateMessage)
                .isDisplayed();
    }

    /**
     * Regresa a la pestaña de Inicio.
     */
    public void tapBottomHome() {
        WaitUtils
                .waitForClickable(driver, bottomHomeButton)
                .click();
    }

    /**
     * Selecciona la pestaña inferior de Movimientos.
     */
    public void tapBottomMovements() {
        WaitUtils
                .waitForClickable(driver, bottomMovementsButton)
                .click();
    }
}