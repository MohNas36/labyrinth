package labyrinth.model;

import labyrinth.util.observable.ObservableList;
import labyrinth.util.observable.SimpleObservableList;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private SimpleObservableList<Integer> drawPile;
    private SimpleObservableList<Integer> discardPile;

    public Deck() {
        drawPile = new SimpleObservableList<>();
        discardPile = new SimpleObservableList<>();
    }

    public ObservableList<Integer> drawPileProperty(){
        return drawPile.asReadOnly();
    }

    public List<Integer> getDrawPile() {
        return drawPile.asReadOnly();
    }

    public ObservableList<Integer> discardPileProperty(){
        return discardPile.asReadOnly();
    }

    public List<Integer> getDiscardPile() {
        return discardPile.asReadOnly();
    }

    public void init(List<Integer> treasures){
        this.drawPile.clear();
        this.drawPile.addAll(treasures);
        this.discardPile.clear();
    }

    public void discard(){
        int carte = this.drawPile.removeFirst();
        this.discardPile.add(carte);

    }
}
