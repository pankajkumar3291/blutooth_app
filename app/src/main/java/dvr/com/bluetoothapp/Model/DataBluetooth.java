package dvr.com.bluetoothapp.Model;

/**
 * Created by android on 19/5/18.
 */

public class DataBluetooth {


    public String getNum1() {
        return num1;
    }

    public String getNum2() {
        return num2;
    }

    private String num1;
    private String num2;

    public String getNum3() {
        return num3;
    }

    private String num3;

    public DataBluetooth(String num1, String num2, String num3) {
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
    }


}
