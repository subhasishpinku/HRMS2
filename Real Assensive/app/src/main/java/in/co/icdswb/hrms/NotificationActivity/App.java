package in.co.icdswb.hrms.NotificationActivity;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CH1 = "CHANEL1";
    public static final String CH2 = "CHANEL2";

    @Override
    public void onCreate() {
        super.onCreate();
        create();
    }
    private void create(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel =new NotificationChannel(
                    CH1, "chanel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("This is channel 1");

            NotificationChannel channe2 =new NotificationChannel(
                    CH2, "chanel 2",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channe2.setDescription("This is channel 1");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            manager.createNotificationChannel(channe2);

        }
    }
}
