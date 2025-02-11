package de.jxnxsdev.jellyfin;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.jxnxsdev.jellyfin.responses.AuthenticateByNameResponse;
import de.jxnxsdev.jellyfin.responses.ShowsEpisodesResponse;
import de.jxnxsdev.jellyfin.responses.ShowsSeasonResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Shows {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static ShowsSeasonResponse getSeasons(String url, AuthenticateByNameResponse authenticateByNameResponse, String seriesId) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/Shows/" + seriesId + "/Seasons"))
                    .header("User-Agent", Headers.UserViewsHeaders.user_agent)
                    .header("Accept", Headers.UserViewsHeaders.accept)
                    .header("X-Emby-Authorization", Headers.UserViewsHeaders.create_x_emby_authorization(authenticateByNameResponse))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return null;
            }

            return OBJECT_MAPPER.readValue(response.body(), ShowsSeasonResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ShowsEpisodesResponse getEpisodes(String url, AuthenticateByNameResponse authenticateByNameResponse, String seriesId, String seasonId) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/Shows/" + seriesId + "/Episodes?SeasonId=" + seasonId))
                    .header("User-Agent", Headers.UserViewsHeaders.user_agent)
                    .header("Accept", Headers.UserViewsHeaders.accept)
                    .header("X-Emby-Authorization", Headers.UserViewsHeaders.create_x_emby_authorization(authenticateByNameResponse))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return null;
            }

            return OBJECT_MAPPER.readValue(response.body(), ShowsEpisodesResponse.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
