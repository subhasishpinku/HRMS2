package in.co.icdswb.hrms.AdapterList;

public class ODLogList {
    private String address;
    private String time;
    private String odtype;
    private String colorcode;
    private String lat;
    private String lang;


    public ODLogList(String address,String time,String odtype,String colorcode,String lat,String lang){
        this.address = address;
        this.time = time;
        this.odtype = odtype;
        this.colorcode = colorcode;
        this.lat=lat;
        this.lang=lang;
    }


   public String getOdtype(){
        return odtype;
   }
    public String getAddress() {
        return address;
    }

    public String getTime() {
        return time;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getColorcode() {
        return colorcode;
    }

    public void setOdtype(String odtype) {
        this.odtype = odtype;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
