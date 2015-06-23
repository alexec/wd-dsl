# WebDriver Groovy DSL

An experiment at created a nice WebDriver DSL:

~~~
wd {
    // search for tutorials on Google
    go "http://www.google.com"
    set "#lst-ib", "WebDriver Tutorials"
    click "button[type='submit']"
    assert get("Selenium Webdriver Tutorials").displayed
    click("Selenium Webdriver Tutorials")
    println title
}
~~~
