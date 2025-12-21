package products;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;


public class ProductsClient {

    // GET /products → returns list of products
    public ProductsResponse getAllProducts() {

        return RestAssured
                .given()
                .contentType(ContentType.JSON)      // request will use JSON
                .when()
                .get("/products")                   // endpoint: GET /products
                .as(ProductsResponse.class);        // deserialization into POJO
    }

    // GET /products/{id} → returns single product by ID
    public Product getProductById(int id) {

        return RestAssured
                .given()
                .contentType(ContentType.JSON)       // request uses JSON
                .pathParam("id", id)
                .when()
                .get("/products/{id}")              // endpoint: GET /products/{id}
                .as(Product.class);                  // deserialization into POJO
    }

    // GET /products/search?q={query} → returns list of matched products
    public ProductsResponse searchProductsByName(String query) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .queryParam("q", query)
                .when()
                .get("/products/search")
                .as(ProductsResponse.class);
    }

    // GET /products/category/{category}
    public ProductsResponse getProductsByCategory(String category) {

        return RestAssured
                .given()
                .pathParam("category", category)
                .contentType(ContentType.JSON)
                .when()
                .get("/products/category/{category}")
                .as(ProductsResponse.class);
    }

    // POST /products/add - create a product
    public Product addProduct(Product product) {

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/products/add")
                .as(Product.class);
    }


    // PUT /products/{id} - update a product
    public Product updateProduct(int id, UpdateProductRequest request) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .body(request)
                .when()
                .put("/products/{id}")
                .as(Product.class);
    }


    // DELETE /products/{id} - delete a product
    public Product deleteProduct(int id) {
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .delete("/products/{id}")
                .as(Product.class);
    }
}
