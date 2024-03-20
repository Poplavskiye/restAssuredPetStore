package com.example.restassuredpetstore;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class ApiTests {
    private final int unexistedPetId = 123095423;
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2/";

    }
    @Test
    public void petNotFound() {
        RestAssured.baseURI += "pet/" + unexistedPetId;
        RequestSpecification requestSpecification = given();
        Response responce = requestSpecification.get();
        System.out.println("Responce:" + responce.asPrettyString());
        ValidatableResponse validatableResponse = responce.then();;
        validatableResponse.statusCode(404);
        validatableResponse.statusLine("HTTP/1.1 404 Not Found");
        validatableResponse.body("message", equalTo("Pet not found"));
    }
    @Test
    public void petNotFound_BDD() {
        given().when()
                .get(baseURI + "pet/{id}", unexistedPetId)
                .then()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 Not Found")
                .body("message", equalTo("Pet not found"))
                .body("type", equalTo("error"));
    }
}