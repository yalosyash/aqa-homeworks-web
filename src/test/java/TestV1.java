import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestV1 {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup(); //https://github.com/bonigarcia/webdrivermanager
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
//        driver = new ChromeDriver();
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    //Задание 1 --------------------------------------------------------------------------------------------------------
    @Test
    void shouldSendForm() throws InterruptedException {
        WebElement form = driver.findElement(By.tagName("form"));
        List<WebElement> elements = driver.findElements(By.className("input__control"));

        elements.get(0).sendKeys("Петр");
        elements.get(1).sendKeys("+12345678901");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    //Задание 2 --------------------------------------------------------------------------------------------------------
    @Test
    void shouldValidateSendingEmptyForm() throws InterruptedException {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.tagName("form"));

        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=name] [class=input__sub]")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldValidateEmptyInputPhone() throws InterruptedException {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.tagName("form"));

        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петр");
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=phone] [class=input__sub]")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    //Input [data-test-id=name] ----------------------------------------------------------------------------------------
    @Test
    void inputNameShouldValidateLatin() throws InterruptedException {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.tagName("form"));

        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("lox");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=name] [class=input__sub]")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void inputNameShouldValidateDigits() throws InterruptedException {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.tagName("form"));

        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("1234567890");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=name] [class=input__sub]")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void inputNameShouldValidateChar() throws InterruptedException {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.tagName("form"));

        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("+!@#$%^&*()");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=name] [class=input__sub]")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    //Input [data-test-id=phone] ----------------------------------------------------------------------------------------
    @Test
    void inputPhoneShouldValidateLatin() throws InterruptedException {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.tagName("form"));
        List<WebElement> elements = driver.findElements(By.className("input__control"));

        elements.get(0).sendKeys("Петр");
        elements.get(1).sendKeys("asdfghjklzx");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=phone] [class=input__sub]")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void inputPhoneShouldValidateChar() throws InterruptedException {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.tagName("form"));
        List<WebElement> elements = driver.findElements(By.className("input__control"));

        elements.get(0).sendKeys("Петр");
        elements.get(1).sendKeys("!@#$%^&*()_+");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=phone] [class=input__sub]")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void inputPhoneShouldValidateLessThan11() throws InterruptedException {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.tagName("form"));
        List<WebElement> elements = driver.findElements(By.className("input__control"));

        elements.get(0).sendKeys("Петр");
        elements.get(1).sendKeys("+1234567890");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=phone] [class=input__sub]")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void inputPhoneShouldValidateMoreThan11() throws InterruptedException {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.tagName("form"));
        List<WebElement> elements = driver.findElements(By.className("input__control"));

        elements.get(0).sendKeys("Петр");
        elements.get(1).sendKeys("+123456789012");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=phone] [class=input__sub]")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void inputPhoneShouldValidateIfHaveNotPlus() throws InterruptedException {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.tagName("form"));
        List<WebElement> elements = driver.findElements(By.className("input__control"));

        elements.get(0).sendKeys("Петр");
        elements.get(1).sendKeys("12345678901");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=phone] [class=input__sub]")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldValidateCheckbox() throws InterruptedException {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.tagName("form"));
        List<WebElement> elements = driver.findElements(By.className("input__control"));

        elements.get(0).sendKeys("Петр");
        elements.get(1).sendKeys("+12345678901");
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.className("input_invalid")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text.trim());
    }
}