package labyrinth.model;

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public Direction getOpposite(){
       if (values()[this.ordinal()] == UP) {
           return Direction.DOWN;
       }
       if (values()[this.ordinal()] == DOWN){
           return Direction.UP;
       }
       if(values()[this.ordinal()] == LEFT){
           return Direction.RIGHT;
       }
       if(values()[this.ordinal()] == RIGHT){
           return Direction.LEFT;
       }
       return null;
    }
}
