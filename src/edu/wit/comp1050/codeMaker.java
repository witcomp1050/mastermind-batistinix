package edu.wit.comp1050;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;
import java.util.Random;

import static java.lang.System.exit;

public class codeMaker {
    static int codeSize = 4;
    static int maxGuesses = 10;
    private static boolean allowDupes = true;
    private static boolean allowBlanks;
    private static final String[] colorArray = {"0xff0000ff", "0x0000ffff", "0x008000ff", "0xffff00ff", "0x000000ff"};
    //Red, Blue, Green, Yellow and Black respectively
    private static Random r = new Random();
    static int range;

    public static Peg[] createCode() {


        Configurations configs = new Configurations();
        try
        {
            BaseConfiguration config = configs.properties(new File("mmind.properties"));
            maxGuesses = config.getInt("maxGuesses");
            if (maxGuesses < 8) maxGuesses = 8;
            if (maxGuesses > 12) maxGuesses = 12;
            if (maxGuesses > 8 && maxGuesses < 12) maxGuesses = 10;
            allowDupes = config.getBoolean("allowDupes");
            allowBlanks = config.getBoolean("allowBlanks");
        }

        catch (Exception e) {
            System.out.println("Something went wrong");
            exit(0);
        }



        Peg[] code = new Peg[codeSize];

        if (allowBlanks) range = 5;
        else range = 4;
        for (int i = 0; i < codeSize; i++) code[i] = new Peg();
        pickColors(code);

        return code;
    }

    static void pickColors(Peg[] code) {
        if (allowDupes) {
            for (Peg p : code) {
                p.color = colorArray[r.nextInt(range)];
            }
        } else {
            boolean[] takenColors = new boolean[range];
            int chosenColorIndex;
            for (Peg p : code) {
                chosenColorIndex = r.nextInt(range);
                while (takenColors[chosenColorIndex]) {
                    chosenColorIndex = r.nextInt(range);
                }
                p.color = colorArray[chosenColorIndex];
                takenColors[chosenColorIndex] = true;
            }
        }
    }
}
