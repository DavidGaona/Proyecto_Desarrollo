package model;

public class DataChart {
    private String valueX;
    private long valueY;

    public DataChart(String valueX, long valueY) {
        this.valueX = valueX;
        this.valueY = valueY;
    }

    public String getValueX() {
        return valueX;
    }

    public long getValueY() {
        return valueY;
    }
}
