package com.dartstransit.latetrips;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MsgDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_details);
        Intent intent = getIntent();
        String message = intent.getStringExtra(Messages.EXTRA_DATA);
        try{
            int val = Integer.parseInt(message);
            DartsTrip lateTrip = MsgData.msgs.get(val);
            TextView tvClientId = (TextView) findViewById(R.id.clientId);
            tvClientId.setText(lateTrip.ClientID);
            TextView tvPassName = (TextView) findViewById(R.id.passenger_name);
            tvPassName.setText(lateTrip.ClientName);
            TextView tvSchTime = (TextView) findViewById(R.id.sch_time);
            tvSchTime.setText(lateTrip.SchTime);
            TextView tvEstPu = (TextView) findViewById(R.id.est_pu);
            tvEstPu.setText(lateTrip.Est_PickUp);
            TextView tvTime = (TextView) findViewById(R.id.time);
            tvTime.setText(lateTrip.Est_DropOff);
            if (lateTrip.Est_PickUp.isEmpty()){
                TextView tvEstDo = (TextView) findViewById(R.id.tvEst_Pu);
                tvEstDo.setText("Est DO");
                tvEstPu.setText(lateTrip.Est_DropOff);
                TextView tvLateOB = (TextView) findViewById(R.id.late_onBoard);
                tvLateOB.setText("On Board");
                tvTime.setText(lateTrip.On_Board);
            }

            TextView tvType = (TextView) findViewById(R.id.run_type);
            tvType.setText(lateTrip.SubTypeAbbr);

            TextView tvRunName = (TextView) findViewById(R.id.run_name);
            tvRunName.setText(lateTrip.Run_Name);
            TextView tvDriver = (TextView) findViewById(R.id.driver_name);
            tvDriver.setText(lateTrip.Driver);

        }catch (Exception e){
            Log.d("lolo", e.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.msg_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
