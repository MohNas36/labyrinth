package labyrinth.model;

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public Direction getOpposite(){
       return this.getRotated(2);
    }

    public Direction getRotated(int nbTimes){
        int x = this.ordinal() + nbTimes % 4 + 4;
        return values()[x % 4];
    }
}
