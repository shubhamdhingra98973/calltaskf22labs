package in.dhingra_shubham.calltaskf22labs;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    private static int timeHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private static int timeMinute = Calendar.getInstance().get(Calendar.MINUTE);
    Button btn1,btn2;
    TextView txt1;
    EditText ext1;
    String str1,str2;
    NotificationManager notificationManager;
    boolean isButtonPressed = false;
    AlarmManager manager;
    Intent alarmIntent;
    PendingIntent pendingIntent1;
    int notID = 0;
    AlertReceiver alertReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.callbutton);
        btn2 = (Button) findViewById(R.id.setbutton);
        txt1 = (TextView) findViewById(R.id.msg1);
        ext1 = (EditText)findViewById(R.id.phonenumber);

        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    str1 = ext1.getText().toString();
                    if (str1.isEmpty())
                    {
                            Toast.makeText(MainActivity.this, "Please enter phone Number", Toast.LENGTH_LONG).show();
                    }
                    else {
                        str2="tel:"+str1;
                        Log.i(str2, "VALUE OF STRING ");
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(str2));
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            return;
                        }
                        startActivity(callIntent);
                    }

                }

            });

        View.OnClickListener listener1 = new View.OnClickListener() {
            public void onClick(View view) {
                txt1.setText("");
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.HOUR, timeHour);
                bundle.putInt(Constants.MINUTE, timeMinute);
                MyDialogFragment fragment = new MyDialogFragment(new MyHandler());
                fragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(fragment, Constants.TIME_PICKER);
                transaction.commit();
                }
        };
        btn2.setOnClickListener(listener1);


}
    class MyHandler extends Handler {
        @Override
        public void handleMessage (Message msg){
            Bundle bundle = msg.getData();
            timeHour = bundle.getInt(Constants.HOUR);
            timeMinute = bundle.getInt(Constants.MINUTE);
            txt1.setText(timeHour + ":" + timeMinute);
            Toast.makeText(MainActivity.this, "Reminder Set", Toast.LENGTH_SHORT).show();

            //     setAlarm();
        }
    }
    /*private void setAlarm() {
        Calendar   calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);
    }*/


    private class PhoneCallListener extends PhoneStateListener {
        String LOG_TAG = "LOGGING 123 CHECK PHONE CALL";
        private boolean iscallConnect = false;
        private boolean makeCallOnce = false;

        public void onCallStateChanged(int state, String incoming_no) {
            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // active
                Log.i(LOG_TAG, "RINGING");
                iscallConnect = true;
                makeCallOnce = true;
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");
                iscallConnect = true;
                makeCallOnce = true;
            }
            if (TelephonyManager.CALL_STATE_IDLE == state) {
                Log.i(LOG_TAG, "CALL STATE IDLE");

                if (makeCallOnce) {
                    alertNotification();
                    Toast.makeText(MainActivity.this, "CALL DISCONNECTED", Toast.LENGTH_SHORT).show();

                }
            }
        }

    }

    public void alertNotification() {
        Calendar   calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timeHour);
        calendar.set(Calendar.MINUTE, timeMinute);
        //calendar.setTimeInMillis(System.currentTimeMillis());
       // Toast.makeText(this, "Alert Notification", LENGTH_LONG).show();
        alarmIntent = new Intent(MainActivity.this, AlertReceiver.class);
        pendingIntent1 = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent1);
       // Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        isButtonPressed = true;
    }

    public void stopAlertNotification(View v) {
        if (manager != null) {
            manager.cancel(pendingIntent1);
            Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
        }
        if(manager == null)
        {
            Toast.makeText(this, "NO reminder", Toast.LENGTH_SHORT).show();
        }
        if(pendingIntent1==null)
        {

        }
    }
        }