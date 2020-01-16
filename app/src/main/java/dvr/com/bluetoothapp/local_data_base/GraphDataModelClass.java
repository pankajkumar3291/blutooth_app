package dvr.com.bluetoothapp.local_data_base;

import java.io.Serializable;

import dvr.com.bluetoothapp.other_classes.InputModel;

public class GraphDataModelClass  implements Serializable {
    private  InputModel fop;
    private  InputModel fos;
    private  InputModel msp;
    private  InputModel mss;
    private  InputModel afp;
    private  InputModel afs;
    private  String fere;
    private  String aft;
    private  String oneP;
    private  String twoP;
    private  String threeP;
    private  String fourP;
    private  String fiveP;
    private  String oneS;
    private  String twoS;
    private  String threeS;
    private  String fourS;
    private  String FiveS;


    public GraphDataModelClass( InputModel fop, InputModel fos, InputModel msp, InputModel mss, InputModel afp, InputModel afs, String fere, String aft, String oneP, String twoP, String threeP, String fourP, String fiveP, String oneS, String twoS, String threeS, String fourS, String fiveS) {
        this.fop = fop;
        this.fos = fos;
        this.msp = msp;
        this.mss = mss;
        this.afp = afp;
        this.afs = afs;
        this.fere = fere;
        this.aft = aft;
        this.oneP = oneP;
        this.twoP = twoP;
        this.threeP = threeP;
        this.fourP = fourP;
        this.fiveP = fiveP;
        this.oneS = oneS;
        this.twoS = twoS;
        this.threeS = threeS;
        this.fourS = fourS;
        FiveS = fiveS;
    }

    public InputModel getFop() {
        return fop;
    }

    public void setFop(InputModel fop) {
        this.fop = fop;
    }

    public InputModel getFos() {
        return fos;
    }

    public void setFos(InputModel fos) {
        this.fos = fos;
    }

    public InputModel getMsp() {
        return msp;
    }

    public void setMsp(InputModel msp) {
        this.msp = msp;
    }

    public InputModel getMss() {
        return mss;
    }

    public void setMss(InputModel mss) {
        this.mss = mss;
    }

    public InputModel getAfp() {
        return afp;
    }

    public void setAfp(InputModel afp) {
        this.afp = afp;
    }

    public InputModel getAfs() {
        return afs;
    }

    public void setAfs(InputModel afs) {
        this.afs = afs;
    }

    public String getFere() {
        return fere;
    }

    public void setFere(String fere) {
        this.fere = fere;
    }

    public String getAft() {
        return aft;
    }

    public void setAft(String aft) {
        this.aft = aft;
    }

    public String getOneP() {
        return oneP;
    }

    public void setOneP(String oneP) {
        this.oneP = oneP;
    }

    public String getTwoP() {
        return twoP;
    }

    public void setTwoP(String twoP) {
        this.twoP = twoP;
    }

    public String getThreeP() {
        return threeP;
    }

    public void setThreeP(String threeP) {
        this.threeP = threeP;
    }

    public String getFourP() {
        return fourP;
    }

    public void setFourP(String fourP) {
        this.fourP = fourP;
    }

    public String getFiveP() {
        return fiveP;
    }

    public void setFiveP(String fiveP) {
        this.fiveP = fiveP;
    }

    public String getOneS() {
        return oneS;
    }

    public void setOneS(String oneS) {
        this.oneS = oneS;
    }

    public String getTwoS() {
        return twoS;
    }

    public void setTwoS(String twoS) {
        this.twoS = twoS;
    }

    public String getThreeS() {
        return threeS;
    }

    public void setThreeS(String threeS) {
        this.threeS = threeS;
    }

    public String getFourS() {
        return fourS;
    }

    public void setFourS(String fourS) {
        this.fourS = fourS;
    }

    public String getFiveS() {
        return FiveS;
    }

    public void setFiveS(String fiveS) {
        FiveS = fiveS;
    }
}
