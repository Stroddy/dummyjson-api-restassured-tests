package auth;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;

public class AuthClient {

    public LoginResponse login (LoginRequest loginRequest){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/auth/login")
                .as(LoginResponse.class);
    }

    public User me (String accessToken){
        return RestAssured
                .given()
                .header("Authorization","Bearer "+accessToken)
                .when()
                .get("/auth/me")
                .as(User.class);
    }


    public RefreshResponse refresh (RefreshRequest refreshRequest){

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(refreshRequest)
                .when()
                .post("/auth/refresh")
                .as(RefreshResponse.class);
    }

}
