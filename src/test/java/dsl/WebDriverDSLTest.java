package dsl;

import org.junit.Test;

public class WebDriverDSLTest {
    @Test
    public void smokeScript() throws Exception {
        WebDriverDSL.run("/example.groovy");
    }
}