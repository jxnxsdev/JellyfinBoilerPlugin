package de.jxnxsdev.jellyfin;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.jxnxsdev.jellyfin.responses.SystemInfoPublicResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

public class Helpers {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * Validates a given URL by checking its format and sending a test request.
     *
     * @param url The URL to validate.
     * @return true if the URL is valid and reachable, false otherwise.
     */
    public static boolean validateURL(String url) {
        System.out.println("Validating URL: " + url);
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/System/Info/Public"))
                    .GET()
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

            if (response.statusCode() != 200) {
                System.out.println("Invalid URL: " + response.statusCode());
                return false;
            }
            return true;
        } catch (IOException | InterruptedException e) {
            System.out.println("Error validating URL: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves system information from a Jellyfin server.
     *
     * @param url The base URL of the Jellyfin server.
     * @return A SystemInfoPublicResponse object containing system information, or null if an error occurs.
     */
    public static SystemInfoPublicResponse getSystemInfo(String url) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/System/Info/Public"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return null;
            }

            // Parse JSON response into SystemInfoPublicResponse object
            return OBJECT_MAPPER.readValue(response.body(), SystemInfoPublicResponse.class);
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    public static String getImage(String url, String id, int width, int height, String quality) {

        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/Items/" + id + "/Images/Primary?fillHeight=" + height + "&fillWidth=" + width + "&quality=" + quality))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() != 200) {
                HttpRequest request2 = HttpRequest.newBuilder()
                        .uri(URI.create(url + "/web/assets/img/banner-light.png"))
                        .GET()
                        .build();

                HttpResponse<byte[]> response2 = client.send(request2, HttpResponse.BodyHandlers.ofByteArray());

                return Base64.getEncoder().encodeToString(response2.body());
            }

            return Base64.getEncoder().encodeToString(response.body());
        } catch (IOException | InterruptedException e) {
            return null;
        }

    }

}
