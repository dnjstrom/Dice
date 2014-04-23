package se.nielstrom.dice.app;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.Random;

/**
 * Created by Daniel on 2014-04-17.
 */
public class Die {
    private LayoutInflater inflater;

    private int color,
                nrOfSides,
                currentSide;


    public Die() {
        this(6);
    }

    public Die(int nrOfSides) {
        this(nrOfSides, Color.WHITE);
    }

    public Die(int nrOfSides, int color) {
        this.nrOfSides = nrOfSides;
        this.color = color;
        currentSide = nrOfSides;
    }


    public int getColor() {
        return color;
    }

    public Die setColor(int color) {
        this.color = color;
        return this;
    }

    public int getNrOfSides() {
        return nrOfSides;
    }

    public Die setNrOfSides(int nrOfSides) {
        this.nrOfSides = nrOfSides;
        return this;
    }

    public int getCurrentSide() {
        return currentSide;
    }

    public Die setCurrentSide(int currentSide) {
        this.currentSide = currentSide;
        return this;
    }


    public int roll() {
        currentSide = new Random().nextInt(nrOfSides) + 1;
        return currentSide;
    }
}
