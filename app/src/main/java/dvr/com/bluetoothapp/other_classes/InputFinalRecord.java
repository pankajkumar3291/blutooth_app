package dvr.com.bluetoothapp.other_classes;

public class InputFinalRecord {
    private  String fam;
    private  String fsm;
    private String mom;
    private  String qtm;
    private  String heigh;
    private String qtr;
    private  String  note;

    public InputFinalRecord(String fam, String fsm, String mom, String qtm, String heigh, String qtr, String note) {
        this.fam = fam;
        this.fsm = fsm;
        this.mom = mom;
        this.qtm = qtm;
        this.heigh = heigh;
        this.qtr = qtr;
        this.note = note;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getFam() {
        return fam;
    }
    public void setFam(String fam) {
        this.fam = fam;
    }
    public String getMsm() {
        return fsm;
    }
    public void setMsm(String msm) {
        this.fsm = msm;
    }
    public String getMom() {
        return mom;
    }
    public void setMom(String mom) {
        this.mom = mom;
    }
    public String getQtm() {
        return qtm;
    }
    public void setQtm(String qtm) {
        this.qtm = qtm;
    }
    public String getHeigh() {
        return heigh;
    }
    public void setHeigh(String heigh) {
        this.heigh = heigh;
    }
    public String getQtr() {
        return qtr;
    }
    public void setQtr(String qtr) {
        this.qtr = qtr;
    }
}
