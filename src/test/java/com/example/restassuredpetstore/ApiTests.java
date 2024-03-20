package com.example.restassuredpetstore;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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
    @DisplayName("Verifying that pet not found message returned if unexisted pet was inputed")
    public void petNotFound_BDD() {
        given().when()
                .get(baseURI + "pet/{id}", unexistedPetId)
                .then()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 Not Found")
                .body("message", equalTo("Pet not found"))
                .body("type", equalTo("error"));
    }
    @Test
    @DisplayName("Changing ID for pet in database")
    public void changePetID() {
        Integer id = 12;
        String name = "Ostin";
        String status = "sold";
        Map<String, String> request = new HashMap<>();
        request.put("id", id.toString());
        request.put("name", name);
        request.put("status", status);
        given().contentType("application/json")
                .body(request)
                .when()
                .post(baseURI + "pet/")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id));
    }
    @Test
    @DisplayName("Place order for a pet")
    public void placeOrder() {
        Map<String, String> request = new HashMap<>();
        Integer id = 10;
        Integer quantity = 100;
        Integer petId = 12;
        request.put("id", id.toString());
        request.put("petId", petId.toString());
        request.put("quantity", quantity.toString());
        given().contentType("application/json")
                .body(request)
                .when()
                .post(baseURI + "store/order")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id));
    }
}