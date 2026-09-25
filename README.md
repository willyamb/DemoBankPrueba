# DemoBank Automation

Proyecto de automatización de pruebas para la aplicación Android **DemoBank**.

La idea de este proyecto fue automatizar los principales flujos de la aplicación usando Page Object Model (POM), Appium y TestNG, y complementar la validación con OCR y OpenCV cuando los elementos no podían validarse de forma confiable con selectores normales.

## 1. ¿Qué necesito para ejecutar el proyecto?

Para levantar el proyecto en otro equipo se necesita:

- Java JDK 11 o superior.
- Maven.
- Android Studio con Android SDK.
- Un emulador Android configurado.
- Appium Server 2.x.
- Appium Inspector.
- La APK standalone de DemoBank.

El proyecto está configurado para compilar con **Java 11** (`source/target 11`). Durante el desarrollo y las ejecuciones de prueba utilicé **JDK 26**.

## 2. Tecnologías utilizadas

| Tecnología | Versión |
|---|---|
| Java | 11+ |
| Appium Server | 2.x |
| Appium Java Client | 10.1.1 |
| Selenium WebDriver | 4.43.0 |
| TestNG | 7.12.0 |
| Tess4J | 5.20.0 |
| OpenCV / JavaCV | 4.13.0-1.5.13 |
| ExtentReports | 5.1.2 |
| Maven Surefire | 3.5.3 |

Para revisar la versión de Appium instalada:

```powershell
appium -v
```

## 3. Estructura del proyecto

La estructura principal quedó organizada de esta manera:

```text
src/
├── main/
│   └── java/
│       ├── pages/
│       │   ├── HomePage.java
│       │   ├── LoginPage.java
│       │   ├── MovementsPage.java
│       │   ├── PaymentConfirmationPage.java
│       │   ├── PaymentPage.java
│       │   ├── PaymentSuccessPage.java
│       │   ├── SplashPage.java
│       │   ├── TransferConfirmationPage.java
│       │   ├── TransferPage.java
│       │   └── TransferSuccessPage.java
│       └── utils/
│           ├── DriverFactory.java
│           ├── ImageMatchUtils.java
│           ├── OCRUtils.java
│           ├── TestDataProvider.java
│           └── WaitUtils.java
│
└── test/
    ├── java/
    │   └── tests/
    │       ├── BaseTest.java
    │       ├── BaselineGeneratorTest.java
    │       ├── HomeTest.java
    │       ├── ImageMatchTest.java
    │       ├── LoginTest.java
    │       ├── MovementsTest.java
    │       ├── PaymentTest.java
    │       └── TransferTest.java
    └── resources/
        ├── baselines/
        ├── testdata/
        └── testng.xml
```

## 4. Configuración del emulador

Para ejecutar las pruebas primero se debe iniciar un emulador Android.

En mi configuración trabajé con el dispositivo:

```text
emulator-5554
```

Para validar que ADB lo reconoce:

```powershell
adb devices
```

Debe aparecer el dispositivo con estado `device`.

## 5. Iniciar Appium

Con el emulador encendido, iniciar Appium Server:

```powershell
appium
```

El proyecto utiliza el driver **UiAutomator2** para Android y se conecta normalmente al servidor local:

```text
http://127.0.0.1:4723
```

## 6. Appium Inspector

Para inspeccionar los elementos de la aplicación utilicé Appium Inspector antes de crear los localizadores de los Page Objects.

Configuración utilizada:

```text
Remote Host: 127.0.0.1
Remote Port: 4723
Platform Name: Android
Automation Name: UiAutomator2
Device Name: emulator-5554
```

Las capabilities principales son:

```json
{
  "platformName": "Android",
  "appium:automationName": "UiAutomator2",
  "appium:deviceName": "emulator-5554",
  "appium:app": "C:\\ruta\\a\\DemoBank.apk",
  "appium:newCommandTimeout": 120,
  "appium:uiautomator2ServerInstallTimeout": 120000,
  "appium:uiautomator2ServerLaunchTimeout": 120000
}
```

La ruta de la APK debe cambiarse por la ruta donde se tenga almacenado el archivo en cada equipo.

## 7. Instalación de la APK

La aplicación utilizada para las pruebas es la APK standalone de DemoBank.

La instalación se puede hacer manualmente con ADB:

```powershell
adb install -r "C:\ruta\a\DemoBank.apk"
```

También se puede instalar directamente desde Android Studio o desde el emulador.

## 8. Datos de prueba

Las credenciales y los datos fijos de las pruebas están centralizados en `TestDataProvider.java`.

Credenciales:

```text
Correo: demo@demo.com
Contraseña: 1234
```

Cuentas mock:

```text
Cuenta Corriente: $1,500,000.00
Cuenta Ahorros:     $955,450.00
```

## 9. ¿Cómo ejecuto las pruebas?

Para ejecutar toda la suite:

```powershell
mvn clean test
```

Para ejecutar una clase específica:

```powershell
mvn -Dtest=HomeTest test
```

Para ejecutar un solo caso:

```powershell
mvn -Dtest=HomeTest#shouldLogoutAndReturnToLogin test
```

## 10. Validaciones con OCR

Usé Tess4J/Tesseract en las validaciones donde el valor mostrado en pantalla no era confiable para leerlo directamente mediante un locator.

Un ejemplo es el saldo consolidado de Home. Primero se obtiene una captura de la zona correspondiente y luego se procesa el texto con OCR para convertirlo a un valor que se pueda comparar programáticamente.

## 11. Validaciones con OpenCV

OpenCV se utiliza para la comparación visual de la pantalla de Home contra una imagen baseline.

Las imágenes de referencia están en:

```text
src/test/resources/baselines/
```

La validación se considera correcta con un Match Score igual o superior al 95%.

En la última ejecución obtuve:

```text
OpenCV Match Score: 99,97%
```

## 12. Reporte de ejecución

El proyecto genera un reporte HTML con ExtentReports en:

```text
test-output/extent-report.html
```

El reporte muestra los casos ejecutados y su resultado. Cuando una prueba falla, se configura la captura de pantalla para adjuntarla al reporte.

## 13. Resultado de la última ejecución

La última ejecución completa fue:

```text
Tests run: 24
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

Esto se obtuvo ejecutando:

```powershell
mvn clean test
```

## 14. Organización del código

El proyecto está trabajado con Page Object Model (POM).

La responsabilidad de cada parte quedó separada así:

- `pages`: localizadores y acciones sobre las pantallas.
- `tests`: casos de prueba y aserciones.
- `utils`: utilidades comunes como driver, esperas, OCR, OpenCV y datos de prueba.

También mantuve los valores fijos de prueba fuera de los métodos de test usando `TestDataProvider`.

Para la sincronización utilizo esperas explícitas con `WebDriverWait` a través de `WaitUtils`.

## 15. Flujo para ejecutar el proyecto desde cero

1. Abrir Android Studio e iniciar el emulador.
2. Confirmar el dispositivo con `adb devices`.
3. Instalar manualmente la APK de DemoBank.
4. Iniciar Appium Server.
5. Revisar la configuración de `DriverFactory` y la ruta de la APK.
6. Ejecutar:

```powershell
mvn clean test
```

7. Revisar el resultado en consola y el reporte en:

```text
test-output/extent-report.html
```

## 16. Nota sobre el desarrollo

Durante el desarrollo utilicé asistencia de IA como apoyo para revisar errores, proponer alternativas de implementación y mejorar la estructura del código. Las decisiones finales, la configuración del entorno y la ejecución de las pruebas se realizaron sobre este proyecto y su APK de DemoBank.
