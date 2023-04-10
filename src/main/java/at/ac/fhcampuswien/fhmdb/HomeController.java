package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;


    @FXML
    public TextField searchField;
    public TextField idField;


    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    protected SortedState sortedState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initializeState();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initializeLayout();
    }

    //prepare lists  for UI
    public void initializeState() throws IOException {
        allMovies = MovieAPI.getAllMovies();
        observableMovies.clear();
        observableMovies.addAll(allMovies); // add all movies to the observable list
        sortedState = SortedState.NONE;
    }

    //SET meaning of BUTTONS in UI
    public void initializeLayout() {
        movieListView.setItems(observableMovies);   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell()); // apply custom cells to the listview

        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        releaseYearComboBox.getItems().add("No filter");
        List<Integer> releaseYears = allMovies.stream()
                .map(Movie::getReleaseYear)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        releaseYearComboBox.getItems().addAll(releaseYears);
        releaseYearComboBox.setPromptText("Filter by Release Year");


        ratingComboBox.setPromptText("Filter by Rating");
        IntStream.rangeClosed(0, 9).forEach(ratingComboBox.getItems()::add);
    }

    //SEARCH BUTTON
    public void searchBtnClicked(ActionEvent actionEvent) throws IOException {
        String id = idField.getText().trim().toLowerCase();
        String searchQuery = searchField.getText().trim().toLowerCase();
        Object genre = genreComboBox.getSelectionModel().getSelectedItem();
        Object releaseYear = releaseYearComboBox.getSelectionModel().getSelectedItem();
        Object ratingFrom = ratingComboBox.getSelectionModel().getSelectedItem();

        String genreStr = null;
        String releaseYearStr = null;
        String ratingStr = null;

        //NULL Handling cause i hate red text
        if (genre != null) {
            genreStr = genre.toString();
        }
        if (releaseYear != null) {
            releaseYearStr = releaseYear.toString();
        }

        if (ratingFrom != null) {
            ratingStr = ratingFrom.toString();
        }

        if (!id.equals("")) {
            Movie movie = MovieAPI.getSpecificMovie(id);
            observableMovies.clear();
            observableMovies.add(movie);
        } else {

            observableMovies.addAll(allMovies);
            List<Movie> movies = MovieAPI.getMovies(searchQuery, genreStr, releaseYearStr, ratingStr);
            observableMovies.clear();
            observableMovies.addAll(movies);

            // DUMMY calling extra Functions
            /*System.out.println(getLongestMovieTitle(movies));
            System.out.println(getMostPopularActor(movies));
            System.out.println(countMoviesFrom(movies, "Quentin Tarantino"));
            System.out.println(getMoviesBetweenYears(movies, 2008, 2019));
            System.out.println(getBusiestWriter(movies));*/
        }
    }

    //SORT BUTTON
    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    //SORT method - still valid
    /* sort movies based on sortedState
     by default sorted state is NONE
     afterwards it switches between ascending and descending
     */
    public void sortMovies() {
        if (sortedState == SortedState.NONE || sortedState == SortedState.DESCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle));
            sortedState = SortedState.ASCENDING;
        } else if (sortedState == SortedState.ASCENDING) {
            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            sortedState = SortedState.DESCENDING;
        }
    }


    String getMostPopularActor(List<Movie> movies) {
        return movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");


    }


    int getLongestMovieTitle(List<Movie> movies){
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);

    }

    long countMoviesFrom(List<Movie> movies, String director){
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear){
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }

    String getBusiestWriter(List<Movie> movies){
        return movies.stream()
                .flatMap(movie -> movie.getWriters().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("");
    }



}