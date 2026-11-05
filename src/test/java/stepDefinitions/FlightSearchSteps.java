package stepDefinitions;

import org.testng.Assert;

import base.Base;
import io.cucumber.java.en.*;
import pageObjects.FlightSearchPage;

public class FlightSearchSteps extends Base {

    FlightSearchPage flightPage;

    @Given("user is on Google Flights home page")
    public void user_is_on_google_flights_home_page() {
        driver.get("https://www.google.com/travel/flights?hl=en&curr=INR");
        flightPage = new FlightSearchPage();
    }

    @When("user selects Hyderabad as the departure city")
    public void user_selects_hyderabad() {
        flightPage.enterFromLocation("Hyderabad");
        String value = flightPage.getFromValue();
        Assert.assertTrue(value.contains("Hyderabad"),
                "From field lo expected Hyderabad but found: " + value);
    }

    @When("user selects Bengaluru as the destination city")
    public void user_selects_bengaluru() {
        flightPage.enterToLocation("Bengaluru");
        String value = flightPage.getToValue();
        Assert.assertTrue(value.contains("Bengaluru"),
                "To field lo expected Bengaluru but found: " + value);
    }

    @When("user selects departure date as {string}")
    public void user_selects_departure_date(String date) {
        flightPage.selectDepartureDate(date);
    }

    @When("user selects return date as {string}")
    public void user_selects_return_date(String date) {
        flightPage.selectReturnDate(date);
    }

    @When("user clicks on Search button")
    public void user_clicks_on_search_button() {
        flightPage.clickSearchButton();
    }

    @Then("available flights should be displayed")
    public void available_flights_should_be_displayed() {
        Assert.assertTrue(flightPage.areFlightResultsDisplayed(),
                "Flight results were not displayed");
    }
    
    @Then("the cheapest flight price should be identified")
    public void the_cheapest_flight_price_should_be_identified() {
        int cheapest = flightPage.getCheapestPrice();
        Assert.assertTrue(cheapest > 0, "Cheapest price was not identified");
    }
}