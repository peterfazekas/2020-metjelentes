package hu.met.model.service;

import hu.met.model.domain.Report;
import hu.met.model.domain.ReportTime;
import hu.met.model.domain.Wind;

import java.util.List;
import java.util.stream.Collectors;

public class DataParser {

    public List<Report> parse(List<String> lines) {
        return lines.stream()
                .map(this::createReport)
                .collect(Collectors.toList());
    }

    private Report createReport(String line) {
        String[] items = line.split(" ");
        String settlement = items[0];
        String time = items[1];
        int hour = getValue(time.substring(0, 2));
        int minute = getValue(time.substring(2));
        ReportTime reportTime = new ReportTime(hour, minute);
        String windValues = items[2];
        String windDirection = windValues.substring(0, 3);
        int windForce = getValue(windValues.substring(3));
        Wind wind = new Wind(windDirection, windForce);
        int temperature = getValue(items[3]);
        return new Report(settlement, reportTime, wind, temperature);
    }

    private int getValue(String text) {
        return Integer.parseInt(text);
    }
}
