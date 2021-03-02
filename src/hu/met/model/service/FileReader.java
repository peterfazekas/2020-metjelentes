package hu.met.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public List<String> read(String filename) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Path.of(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
