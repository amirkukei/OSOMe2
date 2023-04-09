package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



public class MovieAPI {

    /*public List<Movie> allMovies;
    URL url = HttpUrl.parse("http://prog2.fh-campuswien.ac.at/movies").url();*/ //https://stackoverflow.com/questions/39498767/build-url-in-java 4.4.23

    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";

    private static OkHttpClient client = new OkHttpClient();
    private static Gson gson = new Gson();

    public static List<Movie> getMovies() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("User-Agent", "FHMDb")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            Movie[] moviesArray = gson.fromJson(responseBody, Movie[].class);
            return Arrays.asList(moviesArray);
        }
    }
    public static List<Movie> getAllMovies() throws IOException {
        return getMovies();
    }
    public List<Movie> searchMovies(String query, String genre, String releaseYear, String ratingFrom) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "movies").newBuilder();
        if (query != null && !query.isEmpty()) {
            urlBuilder.addQueryParameter("query", query);
        }
        if (genre != null && !genre.isEmpty()) {
            urlBuilder.addQueryParameter("genre", genre);
        }
        if (releaseYear != null && !releaseYear.isEmpty()) {
            urlBuilder.addQueryParameter("releaseYear", releaseYear);
        }
        if (ratingFrom != null && !ratingFrom.isEmpty()) {
            urlBuilder.addQueryParameter("ratingFrom", ratingFrom);
        }

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "http.agent")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                Gson gson = new Gson();
                return gson.fromJson(responseBody.string(), new TypeToken<List<Movie>>() {
                }.getType());
            }
            return Collections.emptyList();
        }
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

