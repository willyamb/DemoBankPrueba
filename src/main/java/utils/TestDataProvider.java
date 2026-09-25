package utils;

/**
 * Centraliza los datos utilizados por las pruebas automatizadas.
 *
 * Evita valores fijos dentro de los tests y facilita
 * el mantenimiento de los diferentes escenarios.
 */
public final class TestDataProvider {

    private TestDataProvider() {
        // Evita la creación de instancias de esta clase.
    }

    // Credenciales de prueba definidas para DemoBank.
    public static final String VALID_EMAIL = "demo@demo.com";
    public static final String VALID_PASSWORD = "1234";

    // Datos de las cuentas.
    public static final String CHECKING_ACCOUNT = "Cuenta Corriente";
    public static final String SAVINGS_ACCOUNT = "Cuenta Ahorros";

    // Sufijos utilizados para identificar las cuentas enmascaradas.
    public static final String CHECKING_ACCOUNT_SUFFIX = "4821";
    public static final String SAVINGS_ACCOUNT_SUFFIX = "7735";

    // Saldos oficiales de las cuentas mockeadas.
    public static final double CHECKING_ACCOUNT_BALANCE = 1500000.00;
    public static final double SAVINGS_ACCOUNT_BALANCE = 955450.00;

    // Saldo consolidado esperado de ambas cuentas.
    public static final double EXPECTED_TOTAL_BALANCE =
            CHECKING_ACCOUNT_BALANCE + SAVINGS_ACCOUNT_BALANCE;

    // Datos utilizados para transferencias.
    public static final String TRANSFER_RECIPIENT = "María López";
    public static final String TRANSFER_AMOUNT = "25000";
    public static final String TRANSFER_NOTE = "monto prueba";

    // Datos utilizados para pagos.
    public static final String PAYMENT_SERVICE = "Energía Eléctrica";
    public static final String PAYMENT_AMOUNT = "65.40";
    public static final String PAYMENT_NOTE = "pago luz prueba";

    // Datos para escenarios negativos.
    public static final String INVALID_AMOUNT = "0";
    public static final String INSUFFICIENT_BALANCE_TRANSFER = "2000000";
    public static final String INSUFFICIENT_BALANCE_PAYMENT = "3000000";

    // Datos utilizados para las búsquedas de movimientos.
    public static final String MOVEMENT_SEARCH_TERM = "netflix";
    public static final String NO_MATCH_SEARCH_TERM = "abcd";
    public static final double CHECKING_BALANCE = 1500000.00;
    public static final double SAVINGS_BALANCE = 955450.00;
}