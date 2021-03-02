package hu.met.model.domain;

import java.util.stream.IntStream;

public class Wind {

    private final String windDirection;
    private final int windForce;

    public Wind(String windDirection, int windForce) {
        this.windDirection = windDirection;
        this.windForce = windForce;
    }

    public boolean isCalm() {
        return "000".equals(windDirection) && windForce == 0;
    }

    public String getWindForce() {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, windForce)
                .forEach(i -> sb.append("#"));
        return sb.toString();


    }
}
