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
    public JFXButton resetBtn;

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
        //   allMovies = Movie.initializeMovies();
        allMovies = MovieAPI.getMovies();
        //  printMovies(allMovies);


        //       String schaumamal=MovieAPI.getDataBaseFromInternet(BASE);
        //       System.out.println(schaumamal);
        //             String urlmitquery=MovieAPI.getDataBaseFromInternet(BASE);
        //             System.out.println(urlmitquery);
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
        genreComboBox.setPromptText("Filter by Genre");
/*
        //https://stackoverflow.com/questions/27226795/adding-a-list-of-years-inside-a-jcombobox 4.4.23
        ArrayList<Integer> years_tmp = new ArrayList<Integer>();
        years_tmp=getAllExistingReleaseYears();
        for(int years = Calendar.getInstance().get(Calendar.YEAR); years>= 1878; years--) {
            years_tmp.add(Integer.valueOf(years));
        }
        */
        releaseYearComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        releaseYearComboBox.getItems().addAll(getAllExistingReleaseYears(allMovies));
        releaseYearComboBox.setPromptText("Filter by Release Year");

        ArrayList<Double> ratingList = new ArrayList<Double>();
        for(int i = 9; i>=0; i--) {
            Double rate=0.0+i;
            System.out.println(rate);
            ratingList.add(rate);
        }
        System.out.println(ratingList.toString());
        ratingComboBox.getItems().add("No filter");  // add "no filter" to the combobox
     /*   ArrayList<String> ratingStringList= new ArrayList<>();
        for (int i = 0; i < ratingList.size(); i++) {
            ratingStringList.set(i, "from " + ratingList.get(i));
        }*/
        ratingComboBox.getItems().addAll(ratingList);    // add all genres to the combobox
        ratingComboBox.setPromptText("Filter by Rating");
    }

    //SEARCH BUTTON
    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim().toLowerCase();
        Object genre = genreComboBox.getSelectionModel().getSelectedItem();
        applyAllFilters(searchQuery, genre);
        if(sortedState != SortedState.NONE) {
            sortMovies();
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

    //TESTPRINTER
    void printMovies(List<Movie> allMovies){
        for (Movie m:allMovies){
            System.out.println(m.getYearReleased());
            System.out.println(m.getId());
            System.out.println(m.getImgUrl());
            System.out.println(m.getLengthInMinutes());
            System.out.println(m.getDirectors());
            System.out.println(m.getWriters());
            System.out.println(m.getMainCast());
            System.out.println(m.getRating());
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Java Streams
    public List<Integer>  getAllExistingReleaseYears(List<Movie> movies) {
        List<Integer> allYears = movies.stream()
                .map(movie -> movie.getYearReleased())
                .sorted(Comparator.reverseOrder())
                .distinct()
                .collect(Collectors.toList());
        //    System.out.println(allYears);
        return allYears;
    }
    void methodToTryStreams(){
        countMoviesFrom(observableMovies,"Frank Capra" );
        getMoviesBetweenYears(observableMovies, 1995, 2000);
        getLongestMovieTitle(observableMovies);
        getMostPopularActor(observableMovies);
    }
    public String  getMostPopularActor(List<Movie> movies) {
        List<String> actors = movies.stream()
                .map(movie -> movie.getMainCast())
                .flatMap(movie -> movie.stream())
                .collect(Collectors.toList());
        //8.4.23 https://www.geeksforgeeks.org/stream-flatmap-java-examples/ and
        // https://stackoverflow.com/questions/71051152/iterate-over-a-list-of-lists-and-check-for-a-condition-using-java-8
        Map<String, Long> actorsMap = actors.stream()
                .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));//8.4.23 https://www.techiedelight.com/count-frequency-elements-list-java/
        List<String> actor = actorsMap.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .stream().limit(1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
//OMG this was hard! 8.4.23
// https://www.tutorialspoint.com/java8/java8_streams.htm,
// https://www.swtestacademy.com/java-streams-comparators/,
// https://www.tutorialspoint.com/java-8-stream-terminal-operations
        System.out.println(actor.toString());
        return actor.toString();
    }
    public int getLongestMovieTitle(List<Movie> movies) {
        var result = movies.stream()
                .mapToInt(movie->movie.getTitle().length())
                .max()
                .stream().limit(1)
                .sum();
        System.out.println("the longest movie titel hast number of letters: "+result);
        return result;
    }
    public List<Movie> getMoviesBetweenYears(List<Movie> movies, int yearStart, int yearEnd) {
        var result = movies.stream()
                .filter(movie -> movie.getYearReleased()> yearStart)
                .filter(movie -> movie.getYearReleased()< yearEnd)
                .collect(Collectors.toList());
        System.out.println("the following movies were made between " +yearStart+" and "+yearEnd);
        printMovies(result);
        return result;
    }
    public long countMoviesFrom(List<Movie> movies, String director) {
        var result = movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
        System.out.println("there are "+result+" movies of director "+director+" in the movie list");
        return result;
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //queryFilter method - for staticList
    public List<Movie> filterByQuery(List<Movie> movies, String query){
        if(query == null || query.isEmpty()) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase())
                )
                .toList();
    }

    //genreFilter method - for staticList
    public List<Movie> filterByGenre(List<Movie> movies, Genre genre){
        if(genre == null) return movies;

        if(movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movie -> movie.getGenres().contains(genre))
                .toList();
    }

    //allFILTER methods query+genre - for staticList
    public void applyAllFilters(String searchQuery, Object genre) {
        List<Movie> filteredMovies = allMovies;

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }

        observableMovies.clear();
        observableMovies.addAll(filteredMovies);
    }


}