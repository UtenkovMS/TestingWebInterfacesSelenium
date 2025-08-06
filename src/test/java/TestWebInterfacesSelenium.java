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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestWebInterfacesSelenium {

    private WebDriver driver;

    @BeforeAll //Метод выполнится один раз перед началом всех тестов в классе.
    static void setupAll() {
        WebDriverManager.chromedriver().setup(); // метод chromedriver().setup() из библиотеки WebDriverManager,
        // который автоматически скачает и установит последнюю версию драйвера Chrome, подходящую для системы.
    }

    @BeforeEach
        // Метод выполняется перед каждым тестовым методом отдельно.

    void setUp() {
        ChromeOptions options = new ChromeOptions(); // Создается объект ChromeOptions, куда добавляются аргументы командной строки:
        options.addArguments("--disable-dev-shm-usage"); // Отключает использование /dev/shm, иногда полезно при работе на удаленных серверах.
        options.addArguments("--no-sandbox"); // Отключает песочницу безопасности Chrome (обычно применяется в CI/CD системах).
        options.addArguments("--headless"); // Позволяет запустить Chrome в headless режиме (без графического интерфейса), удобно для серверных окружений.
        driver = new ChromeDriver(options); // Создается новый экземпляр ChromeDriver с указанными параметрами.
        driver.get("http://localhost:9999"); // После запуска, браузер переходит на адрес http://localhost:9999. Обычно это локально запущенный сайт или приложение.
    }

    @AfterEach
        // Метод выполняется после каждого тестового метода.

    void tearDown() {
        driver.quit(); // Метод driver.quit() закрывает браузер и очищает ресурсы.
        driver = null; // Переменной driver присваивается значение null, чтобы убедиться, что ссылка больше не доступна.
    }

    @Test

    public void shouldFillOutAndSubmitForm() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Максим Утенков");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79375794633");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        assertTrue(result.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.getText().trim());
    }

    @Test

    public void shouldFillInNameFieldWithInvalidDataAndReceiveWarning() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Maxim");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79375794633");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'] span.input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }

    @Test

    public void shouldFillInPhoneFieldWithInvalidDataAndReceiveWarning() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Максим");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("12345678912");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] span.input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }

    @Test

    public void shouldFillFIFieldsWithValidDataButCcheckboxNotCheckedAndWarningDisplayed() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Максим");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("12345678912");
        form.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='agreement'] span.checkbox__text"));
        assertTrue(result.isDisplayed());
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", result.getText().trim());
    }

    @Test

    public void shouldSubmitFormIfNameFieldEmptyAndWarningDisplayed() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("12345678912");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'] span.input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения", result.getText().trim());
    }

    @Test

    public void shouldSubmitFormIfPhoneFieldEmptyAndWarningDisplayed() {

        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Максим");
        form.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        form.findElement(By.cssSelector(".button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'] span.input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения", result.getText().trim());
    }

}
