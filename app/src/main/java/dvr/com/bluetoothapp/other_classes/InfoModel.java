package dvr.com.bluetoothapp.other_classes;

import java.io.Serializable;

public class InfoModel implements Serializable {

    public InfoModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private  String name ;
    private  String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
