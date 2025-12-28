package auth;

import config.ApiConfig;
import config.AuthCredentials;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTests {

    private AuthClient client;
    private static final int EXPIRES_IN_MINS = 60;

    @BeforeAll
    static void setup() {
        ApiConfig.apply();
        System.out.println("baseURI = " + RestAssured.baseURI);
    }

    @BeforeEach
    void setUp() {
        client = new AuthClient();
    }

    // =====================================================================
    // LOGIN: POSITIVE
    // =====================================================================

    @Test
    void login_shouldReturnTokensAndUserInfo_withValidCredentials() {
        LoginRequest req = AuthCredentials.defaultLoginRequest();
        LoginResponse response = client.login(req);

        assertAll(
                () -> assertTrue(response.getId() > 0, "User id should be greater than 0"),
                () -> assertEquals(req.username(), response.getUsername(), "Returned username should match login request"),
                () -> assertFalse(response.getAccessToken().isBlank(), "Access token should not be blank"),
                () -> assertFalse(response.getRefreshToken().isBlank(), "Refresh token should not be blank"),
                () -> assertTrue(response.getAccessToken().startsWith("ey"), "Access token should look like a JWT"),
                () -> assertTrue(response.getRefreshToken().startsWith("ey"), "Refresh token should look like a JWT")
        );
    }

    // =====================================================================
    // LOGIN: NEGATIVE
    // =====================================================================

    @Test
    void login_ShouldFail_InvalidUsername() {
        LoginRequest valid = AuthCredentials.defaultLoginRequest();
        LoginRequest invalid = new LoginRequest("wrong_username", valid.password(), EXPIRES_IN_MINS);
        Response response = client.loginRaw(invalid);

        assertAll(
                () -> assertEquals(400, response.statusCode()),
                () -> assertFalse(response.asString().contains("accessToken")),
                () -> assertFalse(response.asString().contains("refreshToken")),
                () -> assertTrue(response.asString().toLowerCase().contains("message")
                        || response.asString().toLowerCase().contains("invalid credentials"))
        );
    }

    @Test
    void login_ShouldFail_InvalidPassword() {
        LoginRequest valid = AuthCredentials.defaultLoginRequest();
        LoginRequest invalid = new LoginRequest(valid.username(), "wrong_password", EXPIRES_IN_MINS);
        Response response = client.loginRaw(invalid);

        assertAll(
                () -> assertEquals(400, response.statusCode()),
                () -> assertFalse(response.asString().contains("accessToken")),
                () -> assertFalse(response.asString().contains("refreshToken")),
                () -> assertTrue(response.asString().toLowerCase().contains("message")
                        || response.asString().toLowerCase().contains("invalid credentials"))
        );
    }

    @Test
    void login_ShouldFail_EmptyCredentials() {
        LoginRequest invalid = new LoginRequest("", "", EXPIRES_IN_MINS);
        Response response = client.loginRaw(invalid);

        assertAll(
                () -> assertEquals(400, response.statusCode()),
                () -> assertFalse(response.asString().contains("accessToken")),
                () -> assertFalse(response.asString().contains("refreshToken")),
                () -> assertTrue(response.asString().toLowerCase().contains("message")
                        || response.asString().toLowerCase().contains("username and password required"))
        );
    }

    @Test
    void login_ShouldFail_NoCredentials() {
        Response response = client.loginRaw(Map.of());

        assertAll(
                () -> assertEquals(400, response.statusCode()),
                () -> assertFalse(response.asString().contains("accessToken")),
                () -> assertFalse(response.asString().contains("refreshToken")),
                () -> assertTrue(response.asString().toLowerCase().contains("message")
                        || response.asString().toLowerCase().contains("username and password required"))
        );
    }

    @Test
    void login_ShouldFail_MissingUsername() {
        LoginRequest valid = AuthCredentials.defaultLoginRequest();
        Response response = client.loginRaw(
                Map.of(
                        "password",valid.password(),
                        "expiresInMins",EXPIRES_IN_MINS

        ));

        assertAll(
                () -> assertEquals(400, response.statusCode()),
                () -> assertFalse(response.asString().contains("accessToken")),
                () -> assertFalse(response.asString().contains("refreshToken")),
                () -> assertTrue(response.asString().toLowerCase().contains("message")
                        || response.asString().toLowerCase().contains("username and password required"))
        );
    }

    @Test
    void login_ShouldFail_MissingPassword() {
        LoginRequest valid = AuthCredentials.defaultLoginRequest();
        Response response = client.loginRaw(
                Map.of(
                        "username",valid.username(),
                        "expiresInMins",EXPIRES_IN_MINS

                ));

        assertAll(
                () -> assertEquals(400, response.statusCode()),
                () -> assertFalse(response.asString().contains("accessToken")),
                () -> assertFalse(response.asString().contains("refreshToken")),
                () -> assertTrue(response.asString().toLowerCase().contains("message")
                        || response.asString().toLowerCase().contains("username and password required"))
        );
    }

    // =====================================================================
    // ME: POSITIVE
    // =====================================================================

    @Test
    void me_ShouldReturnUser_whenAccessTokenIsValid() {
        LoginRequest loginRequest = AuthCredentials.defaultLoginRequest();
        LoginResponse response = client.login(loginRequest);

        User user = client.me(response.getAccessToken());

        assertAll(
                () -> assertTrue(user.getId() > 0, "User id should be greater than 0"),
                () -> assertEquals(response.getUsername(), user.getUsername(), "Username from /me should match login response"),
                () -> assertFalse(user.getEmail().isBlank(), "User email should not be blank"),
                () -> assertTrue(user.getEmail().contains("@"), "User email should contain '@'")
        );
    }


    // =====================================================================
    // ME: NEGATIVE
    // =====================================================================

    // No Token
    @Test
    void me_ShouldFail_WhenAuthorizationHeaderIsMissing(){
        Response response = client.meRaw();
        assertAll(
                ()-> assertEquals(401,response.statusCode(),"Server should return 401:Unauthorized"),
                ()-> assertTrue(response.asString().toLowerCase().contains("message")
                || response.asString().toLowerCase().contains("access token is required"))
        );
    }

    // Wrong token
    @Test
    void  me_ShouldFail_WhenTokenIsInvalid(){
        Response response = client.meRaw("wrong token");
        assertAll(
                ()-> assertEquals(401,response.statusCode(),"Server should return 401:Unauthorized"),
                ()-> assertTrue(response.asString().toLowerCase().contains("message")
                        || response.asString().toLowerCase().contains("access token is required"))
        );
    }

    //Empty Token

    @Test
    void me_ShouldFail_WhenTokenIsEmpty(){
        Response response = client.meRaw("");
        assertAll(
                ()-> assertEquals(401,response.statusCode(),"Server should return 401:Unauthorized"),
                ()-> assertTrue(response.asString().toLowerCase().contains("message")
                        || response.asString().toLowerCase().contains("access token is required"))
        );
    }




    // =====================================================================
    // REFRESH: POSITIVE
    // =====================================================================

    @Test
    void refresh_shouldSendNewTokens_withValidRefreshToken() {
        LoginResponse login = client.login(AuthCredentials.defaultLoginRequest());
        RefreshResponse refresh = client.refresh(new RefreshRequest(login.getRefreshToken()));

        assertAll(
                () -> assertNotEquals(login.getAccessToken(), refresh.getAccessToken(), "Refresh endpoint should return a new access token"),
                () -> assertNotEquals(login.getRefreshToken(), refresh.getRefreshToken(), "Refresh endpoint should return a new refresh token"),
                () -> assertFalse(refresh.getAccessToken().isBlank(), "Refreshed access token should not be blank"),
                () -> assertFalse(refresh.getRefreshToken().isBlank(), "Refreshed refresh token should not be blank")
        );
    }

    // =====================================================================
    // REFRESH: Negative
    // =====================================================================

    // No Refresh Token
    @Test
    void me_ShouldFail_RefreshTokenMissing(){
        Response response = client.refreshRaw(Map.of());
        assertAll(
                ()-> assertEquals(401,response.statusCode(),"Server should return 401:Unauthorized"),
                ()-> assertTrue(response.asString().toLowerCase().contains("message")
                        || response.asString().toLowerCase().contains("refresh token required"))
        );
    }

    //Wrong Refresh Token
    @Test
    void me_ShouldFail_RefreshTokenInvalid(){
        Response response = client.refreshRaw(Map.of("refreshToken", "wrong_refresh"));
        assertAll(
                ()-> assertEquals(403,response.statusCode(),"Server should return 403:Forbidden"),
                ()-> assertTrue(response.asString().toLowerCase().contains("message")
                        || response.asString().toLowerCase().contains("invalid refresh token"))
        );
    }



    // =====================================================================
    // SMOKE FLOW
    // =====================================================================

    @Test
    void smoke_loginMeRefreshMe_shouldWork() {
        // LOGIN
        LoginResponse login = client.login(AuthCredentials.defaultLoginRequest());
        String accessToken1 = login.getAccessToken();
        String refreshToken = login.getRefreshToken();

        assertAll("Login response",
                () -> assertNotNull(login, "Login response should not be null"),
                () -> assertTrue(accessToken1.startsWith("ey"), "Access token should look like a JWT"),
                () -> assertTrue(refreshToken.startsWith("ey"), "Refresh token should look like a JWT")
        );

        // ME (before refresh)
        User meBeforeRef = client.me(accessToken1);
        assertAll("Me (before refresh)",
                () -> assertNotNull(meBeforeRef, "User returned from /me should not be null"),
                () -> assertTrue(meBeforeRef.getId() > 0, "User id should be greater than 0"),
                () -> assertNotNull(meBeforeRef.getUsername(), "Username should not be null"),
                () -> assertFalse(meBeforeRef.getUsername().isBlank(), "Username should not be blank")
        );

        // REFRESH
        RefreshResponse refresh = client.refresh(new RefreshRequest(refreshToken));
        String accessToken2 = refresh.getAccessToken();

        assertAll("Refresh response",
                () -> assertFalse(refresh.getAccessToken().isBlank(), "Refreshed access token should not be blank"),
                () -> assertFalse(refresh.getRefreshToken().isBlank(), "Refreshed refresh token should not be blank"),
                () -> assertNotEquals(accessToken1, accessToken2, "Access token after refresh should differ from the previous one")
        );

        // ME (after refresh)
        User meAfterRef = client.me(accessToken2);
        assertAll("Me (after refresh)",
                () -> assertNotNull(meAfterRef, "User returned from /me after refresh should not be null"),
                () -> assertTrue(meAfterRef.getId() > 0, "User id after refresh should be greater than 0"),
                () -> assertEquals(meBeforeRef.getId(), meAfterRef.getId(), "User id should remain the same after token refresh"),
                () -> assertEquals(meBeforeRef.getUsername(), meAfterRef.getUsername(), "Username should remain the same after token refresh")
        );
    }
}
