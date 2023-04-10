package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



public class MovieAPI {

    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";

    private static OkHttpClient client = new OkHttpClient();
    private static Gson gson = new Gson();
    private static final String DELIMITER = "&";

    public static List<Movie> getMovies(String query, String genre, String releaseYear, String ratingFrom) throws IOException {
        String url = createURl(query, genre, releaseYear, ratingFrom).toString();

        Request request = new Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "http.agent")
                .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            Gson gson = new Gson();
            Movie[] movies = gson.fromJson(responseBody, Movie[].class);

            return Arrays.asList(movies);

        } catch (Exception e) {
            System.err.println(e.getMessage());

        }

        return new ArrayList<>();

    }
    public static List<Movie> getAllMovies() throws IOException {
        return getMovies(null, null, null, null);
    }

    public static List<Movie> createURl(String query, String genre, String releaseYear, String ratingFrom) {
        StringBuilder url = new StringBuilder(BASE_URL);

        if ((query != null && !query.isEmpty()) || genre != null || releaseYear != null || ratingFrom != null) {
            url.append("?");
            if (query != null && !query.isEmpty()) {
                url.append("query=").append(query).append(DELIMITER);
            }
            if (genre != null && !genre.equals("No filter")) {
                url.append("genre=").append(genre).append(DELIMITER);
            }
            if (releaseYear != null && !releaseYear.equals("No filter")) {
                url.append("releaseYear=").append(releaseYear).append(DELIMITER);
            }
            if (ratingFrom != null) {
                url.append("ratingFrom=").append(ratingFrom).append(DELIMITER);
            }

        }
        return null;
    }
    private static String createURL(String id){
        StringBuilder url = new StringBuilder(BASE_URL);
        if(id != null){
            url.append("/").append(id);
        }
        return url.toString();
    }
    public static Movie getSpecificMovie(String id) {
        String url = createURL(id);
        Request request = new Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "http.agent")
                .build();

        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            Gson gson = new Gson();
            Movie movie = gson.fromJson(responseBody, Movie.class);

            return movie;

        } catch (Exception e) {
            System.err.println(e.getMessage());

        }return null;
    }


    //  private static final String DELIMITER = "&";

    //https://square.github.io/okhttp/#requirements 5.4.23

    /*
        public String buildUrl(String queryText, String chosenGenre, int chosenReleaseYear, int chosenRatingFrom) throws IOException, NullPointerException {
            HttpUrl urlWithQueryParameters = new HttpUrl.Builder()
                    .scheme("https")
                    .host("prog2.fh-campuswien.ac.at/movies")
                    //     .addPathSegment("movies")
                    //      .addPathSegment("v1")
                    .addQueryParameter("query", queryText)
                    .addQueryParameter("genre", chosenGenre)
                    .addQueryParameter("releaseYear", Integer.toString(chosenReleaseYear))
                    .addQueryParameter("ratingFrom", Integer.toString(chosenRatingFrom))
                    .build();

            System.out.println(buildUrl().toString());

            Request requesthttp = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .url(httpUrl) // <- Finally put httpUrl in here
                    .build();

            Response response = client.newCall(requesthttp).execute();
            return response.body().string();
        }
    }

    */


  /*  Movie outputMovie = new Gson().fromJson(jsonInput, Movie.class);
    outputMovie.toString();
    allMovies = run(BASE);


  String jsonInput = "{\"imdbId\":\"tt0472043\",\"actors\":" +
          "[{\"imdbId\":\"nm2199632\",\"dateOfBirth\":\"1982-09-21T12:00:00+01:00\"," +
          "\"filmography\":[\"Apocalypto\",\"Beatdown\",\"Wind Walkers\"]}]}";
*/
}

