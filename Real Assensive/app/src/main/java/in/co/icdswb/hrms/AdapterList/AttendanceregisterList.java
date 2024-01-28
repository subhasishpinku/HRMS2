package in.co.icdswb.hrms.AdapterList;

public class AttendanceregisterList {
    private int id;
    private String date;
    private String day;
    private String intime;
    private String outtime;
    private String remarks;
    private String inbranch;
    private String outbranch;
    public AttendanceregisterList(String date,String day,String intime, String outtime,String remarks,String inbranch,String outbranch){
     //   this.id = id;
        this.date = date;
        this.day = day;
        this.intime = intime;
        this.outtime = outtime;
        this.remarks = remarks;
        this.inbranch = inbranch;
        this.outbranch = outbranch;
    }
    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getIntime() {
        return intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getInbranch(){
        return inbranch;
    }

    public String getOutbranch(){
        return outbranch;
    }


}
