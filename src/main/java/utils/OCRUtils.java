package utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Utilidad para realizar reconocimiento óptico de caracteres
 * mediante Tess4J/Tesseract.
 *
 * Esta clase centraliza la configuración y ejecución del OCR.
 * Las aserciones y validaciones pertenecen exclusivamente
 * a la capa de tests.
 */
public final class OCRUtils {

    /*
     * Directorio que contiene los archivos traineddata utilizados
     * por Tesseract.
     *
     * Inicialmente se utiliza la carpeta del proyecto:
     * src/test/resources/tessdata
     */
    private static final String TESSDATA_PATH =
            "src/test/resources/tessdata";

    /*
     * Idioma utilizado por Tesseract.
     *
     * Para las primeras validaciones utilizaremos inglés, ya que
     * los textos que necesitamos leer contienen principalmente
     * cifras, símbolos monetarios y caracteres básicos.
     */
    private static final String LANGUAGE = "eng";

    private OCRUtils() {
        // Evita la creación de instancias de esta clase de utilidad.
    }

    /**
     * Crea una instancia configurada de Tesseract.
     *
     * @return instancia de Tesseract configurada
     */
    private static ITesseract createTesseract() {

        Tesseract tesseract = new Tesseract();

        tesseract.setDatapath(TESSDATA_PATH);
        tesseract.setLanguage(LANGUAGE);

        return tesseract;
    }

    /**
     * Extrae texto desde un archivo de imagen.
     *
     * @param imagePath ruta de la imagen
     * @return texto reconocido por Tesseract
     */
    public static String extractText(Path imagePath) {

        try {
            BufferedImage image =
                    ImageIO.read(imagePath.toFile());

            if (image == null) {
                throw new IllegalArgumentException(
                        "No se pudo leer la imagen: " + imagePath
                );
            }

            return extractText(image);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al cargar la imagen para OCR: " + imagePath,
                    e
            );
        }
    }

    /**
     * Extrae texto desde una imagen en memoria.
     *
     * @param image imagen que será procesada
     * @return texto reconocido por Tesseract
     */
    public static String extractText(BufferedImage image) {

        ITesseract tesseract = createTesseract();

        try {
            return tesseract.doOCR(image).trim();

        } catch (TesseractException e) {
            throw new RuntimeException(
                    "Error durante la ejecución de Tesseract OCR.",
                    e
            );
        }
    }

    /**
     * Captura un elemento de la interfaz y ejecuta OCR
     * directamente sobre su screenshot.
     *
     * Este método será útil para componentes cuyo texto
     * no sea confiable mediante localizadores nativos.
     *
     * @param element elemento que será capturado
     * @return texto reconocido por Tesseract
     */
    public static String extractTextFromElement(
            WebElement element
    ) {

        byte[] screenshot =
                element.getScreenshotAs(OutputType.BYTES);

        try {
            BufferedImage image =
                    ImageIO.read(
                            new ByteArrayInputStream(screenshot)
                    );

            if (image == null) {
                throw new IllegalArgumentException(
                        "No se pudo convertir el screenshot del elemento."
                );
            }

            return extractText(image);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al procesar el screenshot para OCR.",
                    e
            );
        }
    }

    /**
     * Extrae texto desde un archivo de imagen.
     *
     * Este método es un alias descriptivo para facilitar
     * su uso desde los tests.
     *
     * @param imageFile archivo de imagen
     * @return texto reconocido por Tesseract
     */
    public static String extractText(File imageFile) {

        return extractText(imageFile.toPath());
    }
}