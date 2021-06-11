package com.github.rhdesouza.ifood.mp;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class PratoResourceTest {

    @Test
    public void testHelloEndpoint() {

        //Aqui dará erro se não utilizar testContainers

        String body = given()
                .when().get("/prato")
                .then()
                .statusCode(200).extract().asString();
        System.out.println(body);
    }

}