package labyrinth.view;

import javafx.scene.image.Image;
import labyrinth.model.Direction;
import labyrinth.model.TileKind;

import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    private static ImageManager INSTANCE;
    private Map<String , Image> cache;

    private ImageManager(){
        cache = new HashMap<>();
    }

    public static ImageManager getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new ImageManager();
        }
        return INSTANCE;
    }

    public Image getImage(String path){
        if(!cache.containsKey(path)){
            Image img = new Image(getClass().getResourceAsStream(path));
            cache.put(path, img);
        }
        return cache.get(path);
    }

    public Image getTileImage(TileKind kind , Direction direction){
        String dir = "";
        String k = "";

        if(kind == TileKind.CORRIDOR){
            k = "corridor";
            if(direction == Direction.UP || direction == Direction.DOWN){
                dir = "0";
            }
            if(direction == Direction.RIGHT || direction == Direction.LEFT){
                dir = "90";
            }
        }

        if(kind == TileKind.ELBOW){
            k = "elbow";
            if(direction == Direction.UP){
                dir = "0";
            }
            if(direction == Direction.DOWN){
                dir = "180";
            }
            if(direction == Direction.RIGHT){
                dir = "90";
            }
            if (direction == Direction.LEFT){
                dir = "270";
            }
        }

        if(kind == TileKind.T_CROSS){
            k = "tcross";
            if(direction == Direction.UP){
                dir = "0";
            }
            if(direction == Direction.DOWN){
                dir = "180";
            }
            if(direction == Direction.RIGHT){
                dir = "90";
            }
            if (direction == Direction.LEFT){
                dir = "270";
            }
        }

        String path = "/img/tiles/" + k + "-" + dir + ".png";
        return getImage(path);
    }
}
