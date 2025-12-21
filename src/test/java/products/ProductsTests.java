package products;

import config.ApiConfig;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ProductsTests {
    @BeforeAll
    static void setup() {
        ApiConfig.apply();
        System.out.println("baseURI = " + RestAssured.baseURI);
    }

    private ProductsClient client;
    @BeforeEach
    void setUp()
    {
        client = new ProductsClient();
    }


   @Test
    void getAllProductsTest_ShouldReturnAllProducts()
    {

        ProductsResponse response = client.getAllProducts(); // GET + deserialization

        assertAll(
                ()-> assertTrue(response.getTotal()>0),
                ()-> assertTrue(response.getSkip()>=0),
                ()-> assertTrue(response.getLimit()>0),
                ()-> assertNotNull(response.getProducts()),
                ()-> assertFalse(response.getProducts().isEmpty()),
                ()-> assertTrue(response.getProducts().size()<= response.getLimit())
        );
    }

    @Test
    void getProductByIdTest_ShouldReturnProduct_whenIdIsValid()
    {
        int id=1;

        Product product = client.getProductById(id);

        assertAll(
                ()-> assertNotNull(product),
                ()-> assertEquals(id,product.getId()),
                ()-> assertNotNull(product.getTitle()),
                ()-> assertFalse(product.getTitle().isBlank()),
                ()-> assertNotNull(product.getCategory()),
                ()-> assertTrue(product.getPrice()>=0)
        );
    }

    @Test
    void searchProductsByName_ShouldReturnProduct_byValidQueryName()
    {
        String query="phone";
        ProductsResponse response = client.searchProductsByName(query);
        assertAll(
                ()-> assertNotNull(response, "Response should not be null"),
                ()-> assertNotNull(response.getProducts(), "Products list should not be null"),
                ()-> assertFalse(response.getProducts().isEmpty(), "Products list should not be empty"),
                () -> assertTrue(response.getTotal() >= response.getProducts().size(), "Total should be >= products.size()")
        );

        Product firstProduct = response.getProducts().get(0);
        assertAll(
                ()-> assertNotEquals(0, firstProduct.getId(), "Product id should not be 0"),
                () -> assertNotNull(firstProduct.getTitle(), "Product title should not be null"),
                () -> assertFalse(firstProduct.getTitle().isBlank(), "Product title should not be blank")
        );
    }


    @Test
    void getProductsByCategory_ShouldReturnProduct_byValidCategoryName()
    {
        String category="smartphones";
        ProductsResponse response = client.getProductsByCategory(category);

        assertAll(
                ()-> assertNotNull(response, "Response should not be null"),
                ()-> assertNotNull(response.getProducts(), "Products list should not be null"),
                ()-> assertFalse(response.getProducts().isEmpty(), "Products list should not be empty"),
                () -> assertTrue(response.getTotal() >= response.getProducts().size(), "Total should be >= products.size()")
        );

        Product firstProduct = response.getProducts().get(0);
        assertAll(
                ()-> assertEquals(category, firstProduct.getCategory(), "Product category should match requested category"),
                ()-> assertNotEquals(0, firstProduct.getId(), "Product id should not be 0"),
                () -> assertNotNull(firstProduct.getTitle(), "Product title should not be null"),
                () -> assertFalse(firstProduct.getTitle().isBlank(), "Product title should not be blank")
        );
    }

    @Test
    void addProduct_shouldReturnCreatedProduct()
    {

        // Create request object (POJO)
        Product reqObj = new Product();
        reqObj.setTitle("S-class");
        reqObj.setPrice(1234);
        reqObj.setDiscountPercentage(20);


        // POST request (serialization + deserialization)
        Product createdProduct = client.addProduct(reqObj);
        assertAll(
                ()-> assertNotNull(createdProduct),
                ()-> assertTrue(createdProduct.getId()>0, "Created product id should be greater than 0"),
                () -> assertEquals(reqObj.getTitle(), createdProduct.getTitle()),
                () -> assertEquals(reqObj.getPrice(), createdProduct.getPrice()),
                () -> assertEquals(reqObj.getDiscountPercentage(), createdProduct.getDiscountPercentage())
        );
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct()
    {
        int id=1;
        UpdateProductRequest reqUpd = new UpdateProductRequest();
        reqUpd.setTitle("Sofa");
        reqUpd.setPrice(35);;

        Product updatedProduct = client.updateProduct(id,reqUpd);
        assertAll(
                ()-> assertNotNull(updatedProduct),
                ()-> assertEquals(id,updatedProduct.getId()),
                ()-> assertEquals(reqUpd.getTitle(),updatedProduct.getTitle()),
                ()-> assertEquals(reqUpd.getPrice(),updatedProduct.getPrice())
        );
    }


    @Test
    void deleteProduct_shouldReturnUpdatedProduct()
    {
        int id=1;
        Product deletedProduct = client.deleteProduct(id);

        assertAll(
                ()-> assertNotNull(deletedProduct),
                ()-> assertEquals(id,deletedProduct.getId()),
                ()-> assertTrue(deletedProduct.getIsDeleted()),
                () -> assertNotNull(deletedProduct.getDeletedOn())
        );
    }
}
