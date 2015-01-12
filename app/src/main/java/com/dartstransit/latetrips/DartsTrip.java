package com.dartstransit.latetrips;

import android.graphics.Color;
import android.os.Debug;
import android.util.Log;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DartsTrip {
    public int _id;
    public String ClientID;
    public String ClientName;
    public String SchTime;
    public String Est_PickUp;
    public String Est_DropOff;
    public Date Late_PU;
    public String Late_DO;
    public String On_Board;
    public String Run_Name;
    public String Vehicle;
    public String Driver;
    public String SubTypeAbbr;
    public boolean IsLateTrip;
    public String MinsLate;
    public int EstPickupTime;
    public int EstDropOffTime;
    public int OnBoardTime;
    private boolean Bell;
    public int BackGroundColor;
    @Override
    public String toString()
    {
      return "_id= " + _id + ", ClientId= " + ClientID + ", Bell= " + Bell;
    }

    public boolean getBell()
    {
        return Bell;
    }

    public void setBell( boolean b)
    {
        Bell = b;
        if (Bell){
            BackGroundColor = Color.rgb(0,255, 229);
            MsgData.AddNotificationCounter();
        }else{
            BackGroundColor = Color.rgb(255,255, 255);
        }
    }

    public String GetPickUpTime()
    {
        Calendar c = SecondsToCalendar(EstPickupTime);
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
        return sd.format(c.getTime());
    }

    public String GetOnBoardTime()
    {
        Calendar c = SecondsToCalendar(OnBoardTime);
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
        return sd.format(c.getTime());
    }

    public String GetDropffTime(){
        Calendar c = SecondsToCalendar(EstDropOffTime);
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
        return sd.format(c.getTime());
    }

    //Return true if this is a long on board and the Client is already on the bus
    public boolean ClientIsOnBoard()
    {
        Calendar now = Calendar.getInstance();
        Calendar pickUp = SecondsToCalendar(EstPickupTime);

        return !IsLateTrip && (now.getTimeInMillis() > pickUp.getTimeInMillis());
    }

    private Calendar SecondsToCalendar(int secs)
    {
        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        int hours = secs / 3600;
        int remainder =  secs - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int seconds = remainder;
        c.set(Calendar.HOUR_OF_DAY,hours);
        c.set(Calendar.MINUTE, mins);
        c.set(Calendar.SECOND, seconds);
        return c;
    }
}
