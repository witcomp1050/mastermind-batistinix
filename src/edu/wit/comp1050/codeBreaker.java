package edu.wit.comp1050;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

import static edu.wit.comp1050.codeMaker.codeSize;

public class codeBreaker {
    static int misses = 0;
    static int colorMatches = 0;
    static int exactMatches = 0;
    static boolean matched = false;
    static ArrayList<String> codeCopy = new ArrayList<>();
    static String[] guessColors = new String[codeSize];

    public static void guess(Peg[] code, HBox guess) {

        exactMatches = 0;
        colorMatches = 0;
        misses = 0;

        codeCopy.clear();

        for (int i = 0; i < codeSize; i++){
            codeCopy.add(code[i].color);
        }

        Circle c;
        int k = 0;
        for (Node n:guess.getChildren()) {
            c = (Circle) n;
            guessColors[k] = c.getFill().toString();
            k++;
        }

        for (int i = 0; i < codeCopy.size(); i++){
            matched = false;
            if (codeCopy.get(i).equals(guessColors[i])) {
                codeCopy.set(i , "matched");
                exactMatches++;
                matched = true;

            }
            else{
                for (int j = i + 1; j < codeCopy.size(); j++) {
                    if (codeCopy.get(j).equals(guessColors[i])) {
                        codeCopy.set(j , "matched");
                        colorMatches++;
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    for (int j = Math.max(i - 1, 0); j > -1; j--) {
                        if (codeCopy.get(j).equals(guessColors[i])) {
                            codeCopy.set(j , "matched");
                            colorMatches++;
                            matched = true;
                            break;
                        }
                    }
                }
            }
            if (!matched) misses++;
        }
    }

    public static void giveFeedback(HBox fb) {
        Circle c;
        Circle[] circles = new Circle[codeSize];
        int i = 0;
        for (Node n: fb.getChildren()){
            c = (Circle) n;
            circles[i] = c;
            i++;
        }
        i = 0;
        for (int j = exactMatches; j > 0; j--){
            for (Circle ignored : circles) {
                if (ignored.getFill().toString().equals("0x000000ff")) {
                    ignored.setFill(Paint.valueOf("0xff0000ff"));
                    break;
                }
            }
            if (i + 1 < 3)i++;
        }

        i = 0;
        for (int j = colorMatches; j > 0; j--){
            for (Circle ignored : circles){
                if (ignored.getFill().toString().equals("0x000000ff")){
                    ignored.setFill(Paint.valueOf("#ffffff"));
                    break;
                }
                if (i + 1 < 3)i++;
            }

        }
    }
}
