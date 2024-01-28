package in.co.icdswb.hrms.AdapterList;

public class OdApprovalList {
    private String id;
    private String date;
    private String day;
    private String inTime;
    private String outTime;
    private String location;
    private String duration;
    private String rmApprove;
    private String hrApprove;
    private String ceoApprove;
    private String finalStatus;
    public OdApprovalList(String id,String date,String day,String inTime,String outTime,String duration,String rmApprove,String hrApprove,String ceoApprove,String finalStatus){
    this.id = id;
    this.date = date;
    this.day = day;
    this.inTime = inTime;
    this.outTime = outTime;
    this.duration = duration;
    this.rmApprove = rmApprove;
    this.hrApprove = hrApprove;
    this.ceoApprove = ceoApprove;
    this.finalStatus = finalStatus;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getInTime() {
        return inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public String getLocation() {
        return location;
    }

    public String getDuration() {
        return duration;
    }

    public String getRmApprove() {
        return rmApprove;
    }

    public String getHrApprove() {
        return hrApprove;
    }

    public String getCeoApprove() {
        return ceoApprove;
    }

    public String getFinalStatus() {
        return finalStatus;
    }
}
