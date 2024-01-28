package in.co.icdswb.hrms.AdapterList;

public class TaentryList {
   // private int id;
    private String taid;
    private String date;
    private String day;
    private String  purpose;
    private String amount;
    private String aproveamount;
    private String clamamount;
    private String statusram;
    private String statusacc;
    private String reject;
    private int status;
    public TaentryList(String taid,String date,String day, String purpose,String aproveamount,String clamamount, String  statusram,String statusacc,String reject){
        this.taid = taid;
        this.date = date;
        this.day = day;
        this.purpose = purpose;
        this.aproveamount =aproveamount;
        this.clamamount= clamamount;
        this.statusram = statusram;
        this.statusacc =statusacc;
        this.reject= reject;
    }

    public String getTaid() {
        return taid;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getAproveamount() {
        return aproveamount;
    }



    public String getClamamount() {
        return clamamount;
    }

    public String getStatusram() {
        return statusram;
    }

    public String getStatusacc() {
        return statusacc;
    }

    public int getStatus() {
        return status;
    }

    public String getReject() {
        return reject;
    }
}
