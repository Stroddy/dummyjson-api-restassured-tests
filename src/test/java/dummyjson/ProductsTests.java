package dummyjson;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductsTests {
    private final String dummyURL = "https://dummyjson.com";
    //200-Get all product
    @Test
    void getAllProducts_200(){
        Response response = given()
                .when()
                .get(dummyURL+"/products");
        assertEquals(200,response.getStatusCode());
    }



    //200-Get product by valid id
    @Test
    void getProductByID_200(){
        int productId = 153;
        Response response= given()
                .when().get(dummyURL+"/products/"+productId);
        assertEquals(200,response.getStatusCode());

        JsonPath jp = response.jsonPath();
        int id = jp.getInt("id");
        assertEquals(productId,id);
    }


    //404-Get product by invalid id
    @Test
    void getProductByID_404(){
        Response response = given()
                .when().get(dummyURL+"/products/999999");
        assertEquals(404,response.getStatusCode());
        JsonPath jp = response.jsonPath();
        assertEquals("Product with id '999999' not found",jp.getString("message"));
    }


    //201-Create a new product (valid)
    @Test
    void addNewProduct_201(){
        String requestBody = """
                {
                  "title": "G-wagon",
                  "price": 565474736
                }""";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post(dummyURL+"/products/add");

        //response.prettyPrint();

        JsonPath jp = response.jsonPath();
        assertAll(
                () -> assertEquals(201,response.getStatusCode()),
                ()-> assertEquals("G-wagon",jp.getString("title")),
                ()-> assertEquals("565474736",jp.getString("price"))
        );
    }


    //400-Create a new product (invalid JSON)
    @Test
    void addNewProduct_400() {
        String requestBody = """
                {
                  "title": "G-wagon",
                  "price": 565474736
                """;
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post(dummyURL+"/products/add");
        //response.prettyPrint();

        JsonPath jp = response.jsonPath();
        assertAll(
                ()-> assertEquals(400,response.getStatusCode()),
                ()-> assertTrue(jp.getString("message")
                        .contains("Expected ',' or '}' after property value in JSON"))
        );
    }

    //200-Update Product  (Valid ID)
    @Test
    void updateProduct_200(){
        int productId = 1; // Product we want to update

        // Values used in request
        String expectedTitle = "GLS";
        int expectedPrice = 12345;

        // Request body
        String requestBody = """
            {
                "title": "%s",
                "price": %d
            }
            """.formatted(expectedTitle, expectedPrice);

        // Send request
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(dummyURL + "/products/" + productId);

        // Parse response
        JsonPath jp = response.jsonPath();

        // Assertions
        assertAll(
                () -> assertEquals(200, response.getStatusCode()),
                () -> assertEquals(productId, jp.getInt("id")),
                () -> assertEquals(expectedTitle, jp.getString("title")),   // request title == response title
                () -> assertEquals(expectedPrice, jp.getInt("price"))       // request price == response price
        );
    }


    //404-Update Product  (Invalid ID)
    @Test
    void updateProduct_404(){
        String requestBody = """
                {
                    "title": "GLS"
                }""";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().put(dummyURL+"/products/195");
                //response.prettyPrint();
        JsonPath jp = response.jsonPath();
        assertAll(
                ()-> assertEquals(404,response.getStatusCode()),
                ()-> assertNotNull(jp.getString("message")),
                ()-> assertTrue(jp.getString("message").contains("not found"))
);
    }


    //200-Delete Product (Valid ID)
    @Test
    void deleteProduct_200(){
        int productToDelete = 45;
        Response response = given()
                .when().delete(dummyURL+"/products/"+productToDelete);

        JsonPath jp = response.jsonPath();
        assertAll(
                ()-> assertEquals(200,response.getStatusCode()),
                ()-> assertEquals(productToDelete,jp.getInt("id")),
                () -> assertNotNull(jp.get("isDeleted")),
                ()-> assertTrue(jp.getBoolean("isDeleted"))
        );
    }

    //404-Delete Product (Invalid ID)
    @Test
    void deleteProduct_404(){
        int productToDelete = 500;
        Response response = given()
                .when().delete(dummyURL+"/products/"+productToDelete);

        JsonPath jp = response.jsonPath();
        assertAll(
                ()-> assertEquals(404,response.getStatusCode()),
                ()-> assertNotNull(jp.getString("message")),
                ()-> assertTrue(jp.getString("message").contains("not found"))
        );
    }

}
