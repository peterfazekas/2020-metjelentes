package hu.met.model.domain;

import java.util.List;

public class ReportTime implements Comparable<ReportTime>{

    public static final List<Integer> REPORT_HOURS = List.of(1, 7, 13, 19);

    private final int hour;
    private final int minute;

    public ReportTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public boolean isReportHour() {
        return REPORT_HOURS.contains(hour);
    }

    private Integer toMinutes() {
        return hour * 60 + minute;
    }

    @Override
    public int compareTo(ReportTime o) {
        return this.toMinutes().compareTo(o.toMinutes());
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}
