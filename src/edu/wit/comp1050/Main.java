package edu.wit.comp1050;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;

import static java.lang.System.exit;

public class Main extends Application {
    static int currentGuess = 0;

    public static void main(String[] args) {
        launch(args);
    }

    private static void allowDrop(HBox box){
        Circle c;
        for (Node n:box.getChildren()){
            c = (Circle) n;
            Circle finalC = c;
            c.setOnDragOver(event -> {
                if (event.getGestureSource() != finalC && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
            });

            Circle finalC1 = c;
            c.setOnDragDropped((DragEvent event) -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    finalC1.setFill(Paint.valueOf(db.getString()));
                    event.setDropCompleted(true);
                } else {
                    event.setDropCompleted(false);
                }
                event.consume();
            });
        }
    }

    private static void incrementGuess(){
        currentGuess++;
    }

    @Override
    public void start(Stage stage) {
        VBox guessBox = new VBox(10);
        VBox feedbackBox = new VBox(10);
        Pane pane = new StackPane();
        HBox pegs = new HBox(10);
        Button check = new Button("Check");


        pane.setStyle("-fx-background-color: white;");
        guessBox.setStyle("-fx-background-color: purple");
        guessBox.setSpacing(10);
        feedbackBox.setStyle("-fx-background-color: blue");
        check.setStyle("-fx-text-size: 3");

        guessBox.setMaxWidth(300.0);
        feedbackBox.setMaxWidth(100.0);
        feedbackBox.setTranslateX(250.0);


        Circle redPeg = new Circle(20);
        redPeg.setFill(Color.RED);
        Circle bluePeg = new Circle(20);
        bluePeg.setFill(Color.BLUE);
        Circle greenPeg = new Circle(20);
        greenPeg.setFill(Color.GREEN);
        Circle yellowPeg = new Circle(20);
        yellowPeg.setFill(Color.YELLOW);

        Circle[] pegArray = new Circle[]{redPeg, bluePeg, greenPeg, yellowPeg};


        //creates the pegs the user will drag and adds appropriate events
        for (Circle c : pegArray) {
            pegs.getChildren().add(c);
            c.setOnDragDetected(event -> {
                Dragboard db = c.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(c.getFill().toString());
                db.setContent(content);

            });
        }
        pegs.getChildren().add(check);

        pane.getChildren().addAll(guessBox, feedbackBox);

        int maxGuesses = 0;

        Configurations configs = new Configurations();
        try {
            BaseConfiguration config = configs.properties(new File("mmind.properties"));
            maxGuesses = config.getInt("maxGuesses");
            if (maxGuesses < 8) maxGuesses = 8;
            if (maxGuesses > 12) maxGuesses = 12;
            if (maxGuesses > 8 && maxGuesses < 12) maxGuesses = 10;
        } catch (Exception e) {
            System.out.println("Something went wrong");
            exit(0);
        }

        HBox[] guesses = new HBox[maxGuesses];
        HBox[] feedback = new HBox[maxGuesses];


        //creates rows for each guess, fills them with black circles and sets the spacing
        for (int i = 0; i < maxGuesses; i++) {
            guesses[i] = new HBox();
            guesses[i].setSpacing(10.0);
            for (int j = 0; j < 4; j++) {
                guesses[i].getChildren().add(new Circle(20, Color.BLACK));
            }
        }

        for (int i = 0; i < maxGuesses; i++) {
            feedback[i] = new HBox();
            for (int j = 0; j < 4; j++) {
                feedback[i].getChildren().add(new Circle(20, Color.BLACK));
            }
        }

        for (int i = maxGuesses - 1; i > -1; i--) {
            feedbackBox.getChildren().add(feedback[i]);
        }

        Peg[] code = codeMaker.createCode();

        //adds all the HBoxes created above to the VBox
        for (int i = maxGuesses - 1; i > -1; i--) {
            guessBox.getChildren().add(guesses[i]);
        }

        guessBox.getChildren().add(pegs);
        allowDrop(guesses[currentGuess]);

        check.setOnAction(actionEvent ->  {
            if(currentGuess == codeMaker.maxGuesses){
                System.exit(0);
            }
            codeBreaker.guess(code, guesses[currentGuess]);
            codeBreaker.giveFeedback(feedback[currentGuess]);
            incrementGuess();
            if (currentGuess != codeMaker.maxGuesses)allowDrop(guesses[currentGuess]);
        });



        stage.setTitle("Mastermind");

        stage.setScene(new Scene(pane, 800, 600));
        stage.show();
    }
}

