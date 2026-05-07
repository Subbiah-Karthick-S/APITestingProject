package com.krct;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class FakeAPITest {
    @BeforeClass
    public void setup(){
        RestAssured.baseURI="https://api.escuelajs.co/api/v1";
    }

    @Test
    public void testGetProducts(){
        RestAssured.given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0));
    }

    @Test
    public void testFilterProductByPrice(){
        RestAssured.given()
                .queryParam("price","100")
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("[0].price", Matchers.equalTo(100));
    }

    @Test
    public void testFilterProductByPriceRange(){
        RestAssured.given()
                .queryParam("price_min", 900)
                .queryParam("price_max", 1000)
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .body("price", Matchers.everyItem(Matchers.greaterThanOrEqualTo(900)))
                .body("price", Matchers.everyItem(Matchers.lessThanOrEqualTo(1000)));
    }

    @Test
    public void testGetCategories(){
        RestAssured.given()
                .when()
                .get("/categories")
                .then()
                .statusCode(200)
                .body("$", Matchers.instanceOf(List.class));
    }

    @Test
    public void getCategoryById(){
        RestAssured.given()
                .pathParam("id",1)
                .when()
                .get("/categories/{id}")
                .then()
                .statusCode(200)
                .body("id",Matchers.equalTo(1));
    }

    @Test
    public void testCreateCategory(){
        String body = """
                {
                    "name": "Karthick",
                    "image": "https://placeimg.com/640/480/any"
                }
                """;
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/categories")
                .then()
                .log().all()
                .statusCode(201)
                .body("name", Matchers.equalTo("Karthick"))
                .body("image", Matchers.equalTo("https://placeimg.com/640/480/any"));
    }
}
