package labyrinth.service.dialog;

import com.sun.webkit.Timer;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class FileRequest {

    private String request;
    private Mode mode;
    private CompletableFuture<File> fileFuture;

    public FileRequest(String request, Mode mode) {
        this.request = request;
        this.mode = mode;
        this.fileFuture = new CompletableFuture<>();
    }

    public String getRequest() {
        return request;
    }

    public Mode getMode() {
        return mode;
    }

    public CompletableFuture<File> getFile() {
        return fileFuture;
    }

    public void setFile(File file) {
        this.fileFuture.complete(file);
    }

}
