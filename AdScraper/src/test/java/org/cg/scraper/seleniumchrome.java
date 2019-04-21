package org.cg.scraper;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class seleniumchrome {

    @Ignore
    @Test
    public void testGoogleSearch() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        WebDriver driver = new ChromeDriver(options);

        driver.get("https://www.immobilienscout24.at/resultlist?useType=RESIDENTIAL&transferType=RENT&estateType=APARTMENT&price=500&spot=1020%20Wien&zipCode=1020&page=1&matchSubProperties=true&sort=LATEST");
        String src = driver.getPageSource();
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("ChromeDriver");
        searchBox.submit();

        driver.quit();
    }

}
