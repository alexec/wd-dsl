package dsl;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

public class WebDriverDSL implements AutoCloseable {

    private static final int TIME_OUT_IN_SECONDS = 1;
    private static final int SLEEP_IN_MILLIS = 50;
    private final WebDriver driver = new FirefoxDriver();
    private final WebDriverWait wait = new WebDriverWait(driver, TIME_OUT_IN_SECONDS, SLEEP_IN_MILLIS);
    private final Runtime runtime = Runtime.getRuntime();
    private final Thread hook = new Thread(driver::quit);

    {
        runtime.addShutdownHook(hook);
    }

    public static void run(InputStream resource) throws IOException {
        try (WebDriverDSL dsl = new WebDriverDSL();
             InputStreamReader in = new InputStreamReader(resource)) {
            new GroovyShell(new Binding(Collections.singletonMap("wd", dsl)))
                    .parse(in)
                    .run();
        }
    }

    public void go(String location) {
        driver.get(location);
    }

    public void set(String locator, String value) {
        WebElement element = get(locator);
        element.clear();
        element.sendKeys(value);
    }

    public WebElement get(String locator) {
        try {
            return locate(By.cssSelector(locator));
        } catch (NoSuchElementException | TimeoutException e) {
            return locate(byText(locator));
        }
    }


    private static By byText(String text) {
        return By.xpath(String.format("//*[normalize-space(.)='%s']", text));
    }

    private WebElement locate(By selector) {
        return wait.until((WebDriver d) -> driver.findElement(selector));
    }

    public void click(String locator) {
        get(locator).click();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    @SuppressWarnings("unused")
    void call(Closure cl) {
        cl.setDelegate(this);
        cl.setResolveStrategy(Closure.DELEGATE_ONLY);
        cl.call();
    }

    public static void run(String script) throws IOException {
        run(WebDriverDSL.class.getResourceAsStream(script));
    }

    @Override
    public void close() {
        Runtime.getRuntime().removeShutdownHook(hook);
        driver.quit();
    }
}
