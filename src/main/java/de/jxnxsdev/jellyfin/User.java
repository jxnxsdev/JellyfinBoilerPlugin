package de.jxnxsdev.jellyfin;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.jxnxsdev.jellyfin.responses.AuthenticateByNameResponse;
import de.jxnxsdev.jellyfin.responses.UserViewsResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class User {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static AuthenticateByNameResponse authenticateByName(String url, String username, String password) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/Users/AuthenticateByName"))
                    .header("X-Application", Headers.AuthenticateByNameHeaders.x_application)
                    .header("Accept-Charset", Headers.AuthenticateByNameHeaders.accept_charset)
                    .header("User-Agent", Headers.AuthenticateByNameHeaders.user_agent)
                    .header("Content-Type", Headers.AuthenticateByNameHeaders.content_type)
                    .header("Accept", Headers.AuthenticateByNameHeaders.accept)
                    .header("X-Emby-Authorization", Headers.AuthenticateByNameHeaders.x_emby_authorization)
                    .POST(HttpRequest.BodyPublishers.ofString("{\"Username\":\"" + username + "\",\"Pw\":\"" + password + "\"}"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Failed to authenticate user: " + response.statusCode());
                return null;
            }

            System.out.println("Authenticated user: " + username);

            System.out.println(response.body());

            return OBJECT_MAPPER.readValue(response.body(), AuthenticateByNameResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static UserViewsResponse getUserViews(String url, AuthenticateByNameResponse authenticateByNameResponse) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/Users/" + authenticateByNameResponse.user.id + "/Views"))
                    .header("User-Agent", Headers.UserViewsHeaders.user_agent)
                    .header("Accept", Headers.UserViewsHeaders.accept)
                    .header("X-Emby-Authorization", Headers.UserViewsHeaders.create_x_emby_authorization(authenticateByNameResponse))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Failed to get user views: " + response.statusCode());
                return null;
            }

            System.out.println("Got user views");

            return OBJECT_MAPPER.readValue(response.body(), UserViewsResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
