package in.co.icdswb.hrms.SetGetActivity;

public class UploadsetGetImage {
    private String taimg;
    private String tblid;
    public UploadsetGetImage(String taimg,String tblid){
        this.taimg=taimg;
        this.tblid=tblid;
    }

    public String getTaimg() {
        return taimg;
    }

    public void setTaimg(String taimg) {
        this.taimg = taimg;
    }

    public String getTblid() {
        return tblid;
    }

    public void setTblid(String tblid) {
        this.tblid = tblid;
    }
}
