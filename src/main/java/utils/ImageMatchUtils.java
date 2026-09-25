package utils;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;

import java.io.File;

import static org.bytedeco.opencv.global.opencv_core.minMaxLoc;
import static org.bytedeco.opencv.global.opencv_imgproc.TM_CCOEFF_NORMED;
import static org.bytedeco.opencv.global.opencv_imgproc.matchTemplate;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

/**
 * Utilidad para realizar comparaciones visuales mediante OpenCV.
 *
 * Centraliza la lectura de imágenes y el cálculo del Match Score.
 *
 * El resultado se expresa como porcentaje:
 *
 * 100.0 = coincidencia perfecta
 * 95.0  = umbral mínimo requerido por la prueba técnica
 * 0.0   = ausencia de coincidencia
 */
public final class ImageMatchUtils {

    private ImageMatchUtils() {
        // Evita la creación de instancias.
    }

    /**
     * Compara una imagen actual contra una imagen base.
     *
     * Utiliza TM_CCOEFF_NORMED de OpenCV para obtener
     * un coeficiente de correlación normalizado.
     *
     * @param actualPath ruta de la captura actual
     * @param baselinePath ruta de la imagen base
     * @return Match Score expresado como porcentaje
     */
    public static double compareImages(
            String actualPath,
            String baselinePath
    ) {

        validateFile(actualPath, "imagen actual");
        validateFile(baselinePath, "imagen base");

        Mat actual = imread(actualPath);
        Mat baseline = imread(baselinePath);

        validateImage(actual, actualPath);
        validateImage(baseline, baselinePath);

        /*
         * Para una comparación de regresión visual directa,
         * ambas imágenes deben tener las mismas dimensiones.
         */
        if (actual.rows() != baseline.rows()
                || actual.cols() != baseline.cols()) {

            throw new IllegalArgumentException(
                    "Las imágenes no tienen las mismas dimensiones. "
                            + "Actual: "
                            + actual.cols()
                            + "x"
                            + actual.rows()
                            + ", Baseline: "
                            + baseline.cols()
                            + "x"
                            + baseline.rows()
            );
        }

        /*
         * El resultado de matchTemplate tendrá una sola posición
         * cuando la imagen y el template tengan exactamente
         * el mismo tamaño.
         */
        Mat result = new Mat();

        matchTemplate(
                actual,
                baseline,
                result,
                TM_CCOEFF_NORMED
        );

        double[] minValue = new double[1];
        double[] maxValue = new double[1];

        Point minLocation = new Point();
        Point maxLocation = new Point();

        minMaxLoc(
                result,
                minValue,
                maxValue,
                minLocation,
                maxLocation,
                null
        );

        /*
         * TM_CCOEFF_NORMED devuelve un valor entre -1 y 1.
         *
         * Convertimos el resultado a porcentaje.
         */
        double score = maxValue[0] * 100.0;

        /*
         * Protección ante pequeñas variaciones numéricas.
         */
        if (score < 0.0) {
            score = 0.0;
        }

        if (score > 100.0) {
            score = 100.0;
        }

        return score;
    }

    /**
     * Verifica que el archivo exista y sea un archivo válido.
     *
     * @param path ruta del archivo
     * @param description descripción del archivo
     */
    private static void validateFile(
            String path,
            String description
    ) {

        if (path == null || path.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "La ruta de la "
                            + description
                            + " no puede estar vacía."
            );
        }

        File file = new File(path);

        if (!file.exists()) {

            throw new IllegalArgumentException(
                    "No existe la "
                            + description
                            + ": "
                            + path
            );
        }

        if (!file.isFile()) {

            throw new IllegalArgumentException(
                    "La ruta indicada para la "
                            + description
                            + " no corresponde a un archivo: "
                            + path
            );
        }
    }

    /**
     * Verifica que OpenCV haya podido cargar correctamente
     * la imagen.
     *
     * @param image imagen cargada por OpenCV
     * @param path ruta utilizada
     */
    private static void validateImage(
            Mat image,
            String path
    ) {

        if (image == null || image.empty()) {

            throw new IllegalArgumentException(
                    "OpenCV no pudo cargar la imagen: "
                            + path
            );
        }
    }
}