package auth;

public record LoginRequest(String username, String password, int expiresInMins) {
}
