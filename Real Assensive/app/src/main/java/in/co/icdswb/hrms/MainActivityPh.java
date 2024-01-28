package in.co.icdswb.hrms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class MainActivityPh extends AppCompatActivity{
    PhoneStatReceiver phonecallReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainctivity_ph);
        phonecallReceiver.setOnReceiverReceiverConnected(this);

    }
}
