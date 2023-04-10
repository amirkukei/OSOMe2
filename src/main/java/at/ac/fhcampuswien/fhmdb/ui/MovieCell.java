package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.stream.Collectors;

public class MovieCell extends ListCell<Movie> {

    private final Label title = new Label();
    private final Label genre = new Label();
    private final Label allNumbers = new Label(); //releaseYear
    private final Label detail = new Label();
    private final Label imgUrl = new Label();
 //   private final Label lengthInMinutes = new Label();
    private final Label directors = new Label();
    private final Label writers = new Label();
    private final Label mainCast = new Label();
    private final Label people = new Label();

  //  private final Label rating = new Label();

    private final VBox layout = new VBox(title,  imgUrl, allNumbers,
            detail, directors, writers, mainCast, genre, people); //rating, releaseYear, lengthInMinutes,
    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setGraphic(null);
            setText(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            String genres = movie.getGenres()
                    .stream()
                    .map(Enum::toString)
                    .collect(Collectors.joining(", "));
            genre.setText(genres);
            allNumbers.setText("release year: "+ String.valueOf(movie.getReleaseYear())+"     Duration: "
                    +String.valueOf(movie.getLengthInMinutes())+"     Rating: "+String.valueOf(movie.getRating()));
            //     releaseYear.setText("release year: "+ String.valueOf(movie.getReleaseYear()));
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            //      imgUrl.setText(movie.getImgUrl());
            //       lengthInMinutes.setText("Duration: "+String.valueOf(movie.getLengthInMinutes()));
            String director = movie.getDirectors()
                    .stream()
                    .collect(Collectors.joining(", "));
            directors.setText("Directors: "+director);
            String writer = movie.getWriters()
                    .stream()
                    .collect(Collectors.joining(", "));
            writers.setText("Writers: "+writer);
            String cast = movie.getMainCast()
                    .stream()
                    .collect(Collectors.joining(", "));
            mainCast.setText("Cast: "+cast);
            //      rating.setText("Rating: "+String.valueOf(movie.getRating()));


            // color scheme
            title.getStyleClass().add("text-yellow");
            allNumbers.getStyleClass().add("text-white");
            //releaseYear.getStyleClass().add("text-white");
            //    lengthInMinutes.getStyleClass().add("text-white");
            directors.getStyleClass().add("text-white");
            detail.getStyleClass().add("text-white");
            //    rating.getStyleClass().add("text-white");
            writers.getStyleClass().add("text-white");
            mainCast.getStyleClass().add("text-white");
            //    imgUrl.getStyleClass().add("text-black");
            genre.getStyleClass().add("text-white");
            genre.setStyle("-fx-font-style: italic");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

            // layout
            title.fontProperty().set(title.getFont().font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 70);
            detail.setWrapText(true);
            detail.fontProperty().set(detail.getFont().font(15));
            layout.setPadding(new Insets(10));
            layout.spacingProperty().set(15);
            layout.alignmentProperty().set(Pos.CENTER_LEFT);
            setGraphic(layout);
        }
    }
}

