package in.co.icdswb.hrms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class DemoActivity extends AppCompatActivity implements View.OnTouchListener {
    Button start_time, end_time, exit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        start_time = (Button) findViewById(R.id.start_time);
        end_time = (Button) findViewById(R.id.end_time);
        exit = (Button) findViewById(R.id.exit);
        start_time.setOnTouchListener(this);
        end_time.setOnTouchListener(this);
        exit.setOnTouchListener(this);
    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.start_time:
                if (v.getId() == R.id.start_time) {
                    //   SetImage1();
                    start_time.setEnabled(true);
                    end_time.setEnabled(false);
                    exit.setEnabled(false);
                } else {
                    end_time.setVisibility(View.GONE);

                }
                break;

            case R.id.end_time:
                if (v.getId() == R.id.end_time) {
                    //   SetImage1();
                    end_time.setEnabled(true);
                    start_time.setEnabled(false);
                    end_time.setEnabled(false);
                } else {
                    start_time.setVisibility(View.GONE);

                }
                break;

            case R.id.exit:

                break;
            default:
        }
        return false;
    }

}
