package org.hbrs.se2.project.hellocar.test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("selenium")
@Disabled("Nur für Review ausführen")
public class RegistrationSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    void testSuccessfulRegistration() {
        driver.get("http://localhost:8080/register");

        String emailValue = "test+" + System.currentTimeMillis() + "@mail.com";

        // E-Mail
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("vaadin-email-field input")));
        email.sendKeys(emailValue);

        // Passwort + Passwort bestätigen (1. und 2. vaadin-password-field)
        List<WebElement> passwordFields = driver.findElements(By.cssSelector("vaadin-password-field input"));
        passwordFields.get(0).sendKeys("Test123!");
        passwordFields.get(1).sendKeys("Test123!");

        // PLZ, Ort, Straße, Hausnummer (in dieser Reihenfolge im Formular)
        List<WebElement> textFields = driver.findElements(By.cssSelector("vaadin-text-field input"));
        textFields.get(0).sendKeys("53721");        // PLZ
        textFields.get(1).sendKeys("Siegburg");      // Ort
        textFields.get(2).sendKeys("Musterstraße");  // Straße
        textFields.get(3).sendKeys("12");            // Hausnummer

        // Combobox "Ich bin..."
        WebElement comboInput = driver.findElement(By.cssSelector("vaadin-combo-box input"));
        comboInput.sendKeys("Student");
        comboInput.sendKeys(Keys.ENTER);

        // Datenschutz-Checkbox
        driver.findElement(By.cssSelector("vaadin-checkbox input")).click();

        // Submit-Button
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//vaadin-button[normalize-space()='Registrieren']")));
        submitButton.click();

        // Assertion: Redirect auf Login-Seite
        wait.until(ExpectedConditions.urlContains("/login"));
        assertTrue(driver.getCurrentUrl().contains("/login"),
                "Kein Redirect auf Login-Seite!");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}