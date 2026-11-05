package pageObjects;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import base.Base;

public class FlightSearchPage extends Base {

    private WebDriverWait wait;

    private By fromInput = By.xpath("//input[@aria-label='Where from?']");
    private By toInput = By.xpath("//input[contains(@aria-label,'Where to?')]");
    private By departureInput = By.xpath("//input[@aria-label='Departure']");
    private By doneButton = By.xpath("//button[.//span[normalize-space()='Done']]");
    private By searchButton = By.xpath("//button[@aria-label='Search' or @aria-label='Search for flights' or .//span[normalize-space()='Search']]");
    private By resultsList = By.xpath("//ul[@role='list']//li[.//*[contains(text(),'\u20B9')]]");
    private By calendarDialog = By.xpath("//div[@role='dialog']//div[@role='gridcell']");
    private By priceSpans = By.xpath("//span[@role='text' and contains(@aria-label,'Indian rupees')]");

    public FlightSearchPage() {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private WebElement firstVisible(By locator) {
        return wait.until(d -> d.findElements(locator).stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null));
    }

    private void click(By locator) {
        WebElement el = firstVisible(locator);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void selectCity(By inputLocator, String city) {
        try {
            WebElement field = firstVisible(inputLocator);
            field.click();
            Thread.sleep(1000);

            org.openqa.selenium.interactions.Actions actions =
                    new org.openqa.selenium.interactions.Actions(driver);

            // existing text (Rajamahendravaram) clear
            actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                   .sendKeys(Keys.BACK_SPACE).perform();
            Thread.sleep(500);

            // type city
            actions.sendKeys(city).perform();

            // suggestions load avvadaniki wait
            Thread.sleep(2500);

            // first suggestion select
            actions.sendKeys(Keys.ARROW_DOWN).perform();
            Thread.sleep(500);
            actions.sendKeys(Keys.ENTER).perform();
            Thread.sleep(1000);

            System.out.println("City selected: " + city);
        } catch (Exception e) {
            throw new RuntimeException("Could not select city: " + city, e);
        }
    }
    private void clickDate(String date) {
        String iso = LocalDate.parse(date, DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH)).toString();
        click(By.xpath("//div[@role='gridcell' and @data-iso='" + iso + "']"));
        System.out.println("Date selected: " + iso);
    }

    public void enterFromLocation(String from) {
        selectCity(fromInput, from);
    }

    public void enterToLocation(String to) {
        selectCity(toInput, to);
    }

    public String getFromValue() {
        return firstVisible(fromInput).getAttribute("value");
    }

    public String getToValue() {
        return firstVisible(toInput).getAttribute("value");
    }

    public void selectDepartureDate(String date) {
        click(departureInput);
        clickDate(date);
    }

    public void selectReturnDate(String date) {
        clickDate(date);
        click(doneButton);
    }

    public void clickSearchButton() {
        click(searchButton);
    }

    public boolean areFlightResultsDisplayed() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(calendarDialog));
            wait.until(ExpectedConditions.presenceOfElementLocated(resultsList));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
    
    public List<Integer> getAllFlightPrices() {
        wait.until(ExpectedConditions.presenceOfElementLocated(priceSpans));
        List<Integer> prices = new ArrayList<>();
        for (WebElement el : driver.findElements(priceSpans)) {
            try {
                String label = el.getAttribute("aria-label");
                if (label == null) continue;
                String digits = label.replaceAll("[^0-9]", "");
                if (!digits.isEmpty()) {
                    prices.add(Integer.parseInt(digits));
                }
            } catch (Exception e) {
                // stale element skip
            }
        }
        System.out.println("All prices found: " + prices);
        return prices;
    }

    public int getCheapestPrice() {
        List<Integer> prices = getAllFlightPrices();
        if (prices.isEmpty()) {
            throw new RuntimeException("No flight prices found");
        }
        int min = Collections.min(prices);
        System.out.println("Cheapest flight price: " + min);
        return min;
    }
}