package dvr.com.bluetoothapp.other_classes;

import java.io.Serializable;

public class InputModel implements Serializable {
    private String value_first;
    private String value_second;
    private String value_third;


    public InputModel(String value_first, String value_second, String value_third) {
        this.value_first = value_first;
        this.value_second = value_second;
        this.value_third = value_third;
    }

    public String getValue_first() {
        return value_first;
    }

    public void setValue_first(String value_first) {
        this.value_first = value_first;
    }

    public String getValue_second() {
        return value_second;
    }

    public void setValue_second(String value_second) {
        this.value_second = value_second;
    }

    public String getValue_third() {
        return value_third;
    }

    public void setValue_third(String value_third) {
        this.value_third = value_third;
    }
}
