package config;
import auth.LoginRequest;

public class AuthCredentials {

    private AuthCredentials(){}
    public static LoginRequest defaultLoginRequest(){
        String username = System.getenv("DUMMYJSON_USERNAME");
        String password = System.getenv("DUMMYJSON_PASSWORD");

        if (username==null|| password==null) {
            throw new IllegalStateException("DUMMYJSON_USERNAME or DUMMYJSON_PASSWORD is not set"
            );
        }

        return  new LoginRequest(username,password,60);



    }

}
