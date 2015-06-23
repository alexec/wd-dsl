wd {
    go "http://www.google.com"
    set "#lst-ib", "WebDriver Tutorials"
    click "button[type='submit']"
    assert get("Selenium Webdriver Tutorials").displayed
    click("Selenium Webdriver Tutorials")
    println title
}
