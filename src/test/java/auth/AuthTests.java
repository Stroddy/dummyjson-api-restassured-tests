package auth;

import config.ApiConfig;
import config.AuthCredentials;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthTests {
    @BeforeAll
    static void setup(){
        ApiConfig.apply();
        System.out.println("baseURI = " + RestAssured.baseURI);
    }

    private AuthClient client;

    @BeforeEach
    void setUp(){client = new AuthClient();}

    @Test
    void login_shouldReturnTokensAndUserInfo_withValidCredentials(){
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

    @Test
    void me_ShouldReturnUser_whenAccessTokenIsValid(){
        // Data for login request
        LoginRequest loginRequest = AuthCredentials.defaultLoginRequest();

        // LOGIN happens here:
        // - sends POST /auth/login
        // - server authenticates user
        // - server returns accessToken + refreshToken
        LoginResponse response = client.login(loginRequest);

        // Using accessToken received from login response
        // This token will be sent in Authorization header inside client.me()
        User user = client.me(response.getAccessToken());

        assertAll(
                () -> assertTrue(user.getId() > 0, "User id should be greater than 0"),
                () -> assertEquals(response.getUsername(), user.getUsername(), "Username from /me should match login response"),
                () -> assertFalse(user.getEmail().isBlank(), "User email should not be blank"),
                () -> assertTrue(user.getEmail().contains("@"), "User email should contain '@'")
        );
    }


    @Test
    void refresh_shouldSendNewTokens_withValidRefreshToken(){
        LoginResponse login = client.login(AuthCredentials.defaultLoginRequest());
        RefreshResponse refresh = client.refresh(new RefreshRequest(login.getRefreshToken()));


        assertAll(
                ()-> assertNotEquals(login.getAccessToken(),refresh.getAccessToken(), "Refresh endpoint should return a new access token"),
                ()-> assertNotEquals(login.getRefreshToken(),refresh.getRefreshToken(), "Refresh endpoint should return a new refresh token"),
                ()-> assertFalse(refresh.getAccessToken().isBlank(),"Refreshed access token should not be blank"),
                ()-> assertFalse(refresh.getRefreshToken().isBlank(),"Refreshed refresh token should not be blank")
        );
    }

    // Smoke-Flow Test
    @Test
    void smoke_loginMeRefreshMe_shouldWork(){
        // LOGIN
        LoginResponse login = client.login((AuthCredentials.defaultLoginRequest()));
        String accessToken1 = login.getAccessToken();
        String refreshToken = login.getRefreshToken();

        assertAll("Login response",
                () -> assertNotNull(login, "Login response should not be null"),
                () -> assertTrue(accessToken1.startsWith("ey"), "Access token should look like a JWT"),
                () -> assertTrue(refreshToken.startsWith("ey"), "Refresh token should look like a JWT")
        );

        // ME with access token from login
        User meBeforeRef = client.me(accessToken1);
        assertAll("Me (before refresh)",
                () -> assertNotNull(meBeforeRef, "User returned from /me should not be null"),
                () -> assertTrue(meBeforeRef.getId() > 0, "User id should be greater than 0"),
                () -> assertNotNull(meBeforeRef.getUsername(), "Username should not be null"),
                () -> assertFalse(meBeforeRef.getUsername().isBlank(), "Username should not be blank")
        );

        //Refresh
        RefreshResponse refresh= client.refresh(new RefreshRequest(refreshToken));
        String accessToken2 = refresh.getAccessToken();
        assertAll(
                () -> assertFalse(refresh.getAccessToken().isBlank(), "Refreshed access token should not be blank"),
                () -> assertFalse(refresh.getRefreshToken().isBlank(), "Refreshed refresh token should not be blank"),
                () -> assertNotEquals(accessToken1, accessToken2, "Access token after refresh should differ from the previous one"
                )
        );

        // ME with access token from refresh
        User meAfterRef = client.me(accessToken2);
        assertAll(
                () -> assertNotNull(meAfterRef, "User returned from /me after refresh should not be null"),
                () -> assertTrue(meAfterRef.getId() > 0, "User id after refresh should be greater than 0"),
                () -> assertEquals(meBeforeRef.getId(), meAfterRef.getId(), "User id should remain the same after token refresh"),
                () -> assertEquals(meBeforeRef.getUsername(), meAfterRef.getUsername(), "Username should remain the same after token refresh")
        );
    }

}
