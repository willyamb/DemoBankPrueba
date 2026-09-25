package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {

    private static ExtentReports extentReports;
    private static ExtentTest extentTest;

    @Override
    public void onStart(org.testng.ITestContext context) {

        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter(
                        "test-output/extent-report.html"
                );

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
    }

    @Override
    public void onTestStart(ITestResult result) {

        extentTest =
                extentReports.createTest(
                        result.getMethod().getMethodName()
                );
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        extentTest.pass("Prueba ejecutada correctamente.");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        extentTest.fail(
                "La prueba falló: "
                        + result.getThrowable().getMessage()
        );

        Object instance = result.getInstance();

        if (instance instanceof BaseTest) {

            BaseTest baseTest =
                    (BaseTest) instance;

            String screenshotPath =
                    ScreenshotUtils.takeScreenshot(
                            baseTest.driver,
                            result.getMethod().getMethodName()
                    );

            if (screenshotPath != null) {

                extentTest.fail(
                        "Screenshot del fallo",
                        MediaEntityBuilder
                                .createScreenCaptureFromPath(
                                        screenshotPath
                                )
                                .build()
                );
            }
        }
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {

        if (extentReports != null) {
            extentReports.flush();
        }
    }
}