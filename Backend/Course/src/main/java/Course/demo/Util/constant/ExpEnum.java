package Course.demo.Util.constant;

public enum ExpEnum {
    ABOVE_1_YEAR("Trên 1 năm"),
    ABOVE_2_YEARS("Trên 2 năm"),
    ABOVE_3_YEARS("Trên 3 năm"),
    ABOVE_5_YEARS("Trên 5 năm"),
    ABOVE_10_YEARS("Trên 10 năm"),
    ABOVE_20_YEARS("Trên 20 năm");

    private final String label;

    ExpEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
