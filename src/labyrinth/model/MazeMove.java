package labyrinth.model;

public record MazeMove(Direction direction, int index) {

    public MazeMove getOpposite() {
        return new MazeMove(direction.getOpposite(), index);
    }
}
