package auth;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {

// ===== RAW =====
    public Response loginRaw (Object body){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/auth/login");
    }

    public Response meRaw (String accessToken){
        return RestAssured
                .given()
                .header("Authorization","Bearer "+accessToken)
                .when()
                .get("/auth/me");
    }


    public Response refreshRaw (Object body){

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/auth/refresh");
    }

    // RAW request without Authorization header (for negative tests)
    public Response meRaw() {
        return RestAssured
                .given()
                .when()
                .get("/auth/me");
    }



    // ===== POJO (happy-path) =====
    public LoginResponse login(LoginRequest loginRequest){
        return loginRaw(loginRequest).as(LoginResponse.class);
    }

    public User me (String accessToken){
        return meRaw(accessToken).as(User.class);
    }

    public RefreshResponse refresh(RefreshRequest refreshRequest){
        return refreshRaw(refreshRequest).as(RefreshResponse.class);
    }
}
