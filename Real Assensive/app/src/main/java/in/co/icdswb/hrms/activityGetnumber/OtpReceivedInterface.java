package in.co.icdswb.hrms.activityGetnumber;

/**
 * Created on : May 21, 2019
 * Author     : AndroidWave
 */
public interface OtpReceivedInterface {
    void onOtpReceived(String otp);
    void onOtpTimeout();
}