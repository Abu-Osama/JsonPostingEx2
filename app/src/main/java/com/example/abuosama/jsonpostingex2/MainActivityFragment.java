package com.example.abuosama.jsonpostingex2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    EditText et1,et2,et3;
    Button b1,b2;
    TextView textView;

    public  class  MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            String reult=bundle.getString("result");
            textView.setText("server "+reult);

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        MyReceiver myReceiver=new MyReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("Task_Done");
        getActivity().registerReceiver(myReceiver,intentFilter);
        super.onCreate(savedInstanceState);
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_main, container, false);
        b1= (Button) v.findViewById(R.id.button1);
        b2= (Button) v.findViewById(R.id.button2);
        et1= (EditText) v.findViewById(R.id.edittext1);
        et2= (EditText) v.findViewById(R.id.edittext2);
        et3= (EditText) v.findViewById(R.id.edittext3);
        textView= (TextView) v.findViewById(R.id.textview1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),MyService.class);
                intent.putExtra("name",et1.getText().toString());
                intent.putExtra("country",et2.getText().toString());
                intent.putExtra("twitter",et3.getText().toString());
                getActivity().startService(intent);

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager alarmManager= (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent=new Intent(getActivity(),MyService.class);
                intent.putExtra("name",et1.getText().toString());
                intent.putExtra("country",et2.getText().toString());
                intent.putExtra("twitter",et3.getText().toString());
                PendingIntent pendingIntent=PendingIntent.getActivity(getActivity(),0,intent,0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+60000,60000,pendingIntent);
                Toast.makeText(getActivity(), "alram strated", Toast.LENGTH_SHORT).show();

            }
        });
        return v;
    }
}
