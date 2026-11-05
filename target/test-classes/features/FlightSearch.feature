Feature: Flight Search on Google Flights

  Scenario: Search for a flight from Hyderabad to Bengaluru
    Given user is on Google Flights home page
    When user selects Hyderabad as the departure city
    And user selects Bengaluru as the destination city
    And user selects departure date as "16 November 2026"
    And user selects return date as "17 November 2026"
    And user clicks on Search button
    Then available flights should be displayed
    And the cheapest flight price should be identified