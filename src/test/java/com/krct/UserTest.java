package com.krct;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class UserTest {
    private int id;
    @BeforeClass
    public void setup(){
        RestAssured.baseURI="https://api.escuelajs.co/api/v1";
    }

    @Test(priority=1)
    public void createUserTest()
    {
        String email ="john@mail.com";
        String password = "changeme";
        String name = "Jhon";
        String role = "customer";
        String avatar = "https://i.imgur.com/LDOO4Qs.jpg";
        Map body = Map.of(
                "email", email,
                "password",password,
                "name",name,
                "role",role,
                "avatar",avatar
        );
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/users");
        response.then()
                .log().all()
                .statusCode(201)
                .body("name", Matchers.equalTo(name));
        id = response.jsonPath().getInt("id");
    }


    @Test(priority=2)
    private void getUserTest(){
        RestAssured.given()
                .pathParam("id",id)
                .when()
                .get("users/{id}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", Matchers.equalTo(id));
    }

    @Test(priority=3)
    private void updateUserTest(){
        String email ="john@mail.com";
        String password = "changeme";
        String name = "Change name";
        String role = "customer";
        String avatar = "https://i.imgur.com/LDOO4Qs.jpg";
        Map body = Map.of(
                "email", email,
                "password",password,
                "name",name,
                "role",role,
                "avatar",avatar
        );
        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id",id)
                //.formParam("name",name)
                .body(body)
                .when()
                .put("/users/{id}")
                .then()
                .statusCode(200)
                .body("name", Matchers.equalTo(name));
    }

    @Test(priority=4)
    private void deleteUserTest(){
        RestAssured.given()
                .pathParam("id",id)
                .when()
                .delete("/users/{id}")
                .then()
                .log().all()
                .statusCode(200);
    }
}
