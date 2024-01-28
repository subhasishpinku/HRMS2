package in.co.icdswb.hrms.AdapterList;

public class OutdoorviewList {
    private String puspose;
    private String time;
    private String odtype;
    private String location;

    public OutdoorviewList(String puspose,String time,String odtype,String location){
        this.puspose = puspose;
        this.time = time;
        this.odtype =odtype;
        this.location = location;

    }
    public String getPuspose(){
        return puspose;
    }
    public String getTime(){
        return time;
    }
    public String getOdtype(){
        return odtype;
    }
    public String getLocation(){
        return location;
    }
}
