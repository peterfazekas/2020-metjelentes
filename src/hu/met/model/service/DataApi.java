package hu.met.model.service;

import hu.met.model.domain.Report;

import java.util.List;

public class DataApi {
    private final FileReader fileReader;
    private final DataParser dataParser;

    public DataApi(FileReader fileReader, DataParser dataParser) {
        this.fileReader = fileReader;
        this.dataParser = dataParser;
    }

    public List<Report> getData(String filename) {
        return dataParser.parse(fileReader.read(filename));
    }
}
