package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtils;

public class SplashPage {

    private final AndroidDriver driver;

    @FindBy(xpath = "//android.widget.TextView[@text='DB']")
    private WebElement logo;

    public SplashPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isDisplayed() {
        try {
            return WaitUtils.waitForVisibility(driver, logo).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}