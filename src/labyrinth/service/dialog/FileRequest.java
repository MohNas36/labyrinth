package labyrinth.service.dialog;

import com.sun.webkit.Timer;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class FileRequest {

    private String request;
    private CompletableFuture<File> fileFuture;

    public FileRequest(String request, Mode mode) {
        this.request = request;
        this.fileFuture = fileFuture;
    }

}
