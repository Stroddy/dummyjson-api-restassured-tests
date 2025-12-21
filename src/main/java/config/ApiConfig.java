package config;

import io.restassured.RestAssured;

public class ApiConfig {
    public static final String DUMMY_URL = "https://dummyjson.com";

    public static void apply() {
        RestAssured.baseURI = DUMMY_URL;
    }
}
