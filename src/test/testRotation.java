import labyrinth.model.Direction;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class testRotation {
    @Test
    public void testGetRotated(){
        Direction etat = Direction.UP;
        etat.getRotated(3);
        assertEquals(Direction.LEFT , etat);
    }

    @Test
    public void testRotatedNegatif(){
        Direction etat = Direction.UP;
        etat.getRotated(-1);
        assertEquals(Direction.LEFT , etat);
    }

    @Test
    public void testRotation2(){
        Direction etat = Direction.UP;
        etat.getRotated(7);
        assertEquals(Direction.LEFT , etat);
    }

    @Test
    public void testGetOpposite(){
        Direction etat = Direction.UP;
        etat.getOpposite();
        assertEquals(etat , Direction.DOWN);
    }
}
