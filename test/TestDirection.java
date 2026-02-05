import labyrinth.model.Direction;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class TestDirection {
    @Test
    public void testGetRotated(){
        Direction etat = Direction.UP;
        Direction rotate = etat.getRotated(3);
        assertEquals(Direction.LEFT , rotate);
    }

    @Test
    public void testRotatedNegatif(){
        Direction etat = Direction.UP;
        Direction rotate = etat.getRotated(-1);
        assertEquals(Direction.LEFT , rotate);
    }

    @Test
    public void testRotation2(){
        Direction etat = Direction.UP;
        Direction rotate = etat.getRotated(7);
        assertEquals(Direction.LEFT , rotate);
    }

    @Test
    public void testGetOpposite(){
        Direction etat = Direction.UP;
        Direction opposite = etat.getOpposite();
        assertEquals(opposite , Direction.DOWN);
    }
}
