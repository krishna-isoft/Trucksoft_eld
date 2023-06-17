package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.trucksoft.isoft.isoft_elog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Iftahome_activity extends AppCompatActivity {
    private TextView txtfirstquarterfrom,txtfirstquarterto;
    private TextView txtsecondquarterfrom,txtsecondquarterto;
    private TextView txtthirdquarterfrom,txtthirdquarterto;
    private TextView txtfourthquarterfrom,txtfourthquarterto;
    private LinearLayout linfirst,linsecond,linthird,linfour;
    private ImageView imgback;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityhome_ifta);
        linfirst=findViewById(R.id.lin_first);
        linsecond=findViewById(R.id.lin_second);
        linthird=findViewById(R.id.lin_third);
        linfour=findViewById(R.id.lin_four);
        txtfirstquarterfrom=findViewById(R.id.txt_ffromdate);
        txtfirstquarterto=findViewById(R.id.txt_ftodate);
        txtsecondquarterfrom=findViewById(R.id.txt_sfromdate);
        txtsecondquarterto=findViewById(R.id.txt_stodate);
        txtthirdquarterfrom=findViewById(R.id.txt_tfromdate);
        txtthirdquarterto=findViewById(R.id.txt_ttodate);
        txtfourthquarterfrom=findViewById(R.id.txt_fffromdate);
        txtfourthquarterto=findViewById(R.id.txt_fftodate);
        imgback=findViewById(R.id.driver_list_iv_back);
fetchquarter();
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//        Intent mIntent = new Intent(
//                Ifta_activity.this,
//                Home_activity_bluetooth.class);
//        startActivity(mIntent);
            }
        });
linfirst.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        try {
            Intent mIntent = new Intent(
                    Iftahome_activity.this,
                    Ifta_activity.class);


            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

            Calendar c = Calendar.getInstance();
            Calendar d= Calendar.getInstance();

            c.setTime(sdf.parse(txtfirstquarterfrom.getText().toString()));
            d.setTime(sdf.parse(txtfirstquarterto.getText().toString()));

            SimpleDateFormat dfk = new SimpleDateFormat("yyyy-MM-dd");
            //String output = dfk.format(c.getTime());
            mIntent.putExtra("fromdate",""+dfk.format(c.getTime()));
            mIntent.putExtra("todate",""+dfk.format(d.getTime()));

            startActivity(mIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
});
        linsecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent mIntent = new Intent(
                            Iftahome_activity.this,
                            Ifta_activity.class);


                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

                    Calendar c = Calendar.getInstance();
                    Calendar d= Calendar.getInstance();

                    c.setTime(sdf.parse(txtsecondquarterfrom.getText().toString()));
                    d.setTime(sdf.parse(txtsecondquarterto.getText().toString()));

                    SimpleDateFormat dfk = new SimpleDateFormat("yyyy-MM-dd");
                    //String output = dfk.format(c.getTime());
                    mIntent.putExtra("fromdate",""+dfk.format(c.getTime()));
                    mIntent.putExtra("todate",""+dfk.format(d.getTime()));

                    startActivity(mIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

        linthird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent mIntent = new Intent(
                            Iftahome_activity.this,
                            Ifta_activity.class);


                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

                    Calendar c = Calendar.getInstance();
                    Calendar d= Calendar.getInstance();

                    c.setTime(sdf.parse(txtthirdquarterfrom.getText().toString()));
                    d.setTime(sdf.parse(txtthirdquarterto.getText().toString()));

                    SimpleDateFormat dfk = new SimpleDateFormat("yyyy-MM-dd");
                    //String output = dfk.format(c.getTime());
                    mIntent.putExtra("fromdate",""+dfk.format(c.getTime()));
                    mIntent.putExtra("todate",""+dfk.format(d.getTime()));

                    startActivity(mIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        linfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                Intent mIntent = new Intent(
                        Iftahome_activity.this,
                        Ifta_activity.class);


                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

                Calendar c = Calendar.getInstance();
                Calendar d= Calendar.getInstance();

                    c.setTime(sdf.parse(txtfourthquarterfrom.getText().toString()));
                    d.setTime(sdf.parse(txtfourthquarterto.getText().toString()));

                SimpleDateFormat dfk = new SimpleDateFormat("yyyy-MM-dd");
                //String output = dfk.format(c.getTime());
                mIntent.putExtra("fromdate",""+dfk.format(c.getTime()));
                mIntent.putExtra("todate",""+dfk.format(d.getTime()));

                startActivity(mIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fetchquarter()
    {

        Calendar myCal = new GregorianCalendar();
        int quarter = (myCal.get(Calendar.MONTH) / 3) + 1;

        if(quarter==1)
        {   LocalDate now = LocalDate.now();
            int monthval=now.getMonthValue();

            int totval=9+monthval;
            //Log.e("totval","@"+totval);
            LocalDate earlierOneMonth = now.minusMonths(totval);
            //Log.e("earlierOneMonth","@"+earlierOneMonth);


            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
            //first quarter
            Calendar prevYear = Calendar.getInstance();
            prevYear.add(Calendar.YEAR, -1);
            prevYear.set(Calendar.DATE, 1);
            prevYear.set(Calendar.MONTH, earlierOneMonth.getMonthValue());
            txtfirstquarterfrom.setText(""+df.format(prevYear.getTime()));
            YearMonth fzObject = YearMonth.of(earlierOneMonth.getYear(), 6);
            int dayzsend = fzObject.lengthOfMonth();
            prevYear.set(Calendar.DATE, dayzsend);
            prevYear.set(Calendar.MONTH, 5);
            txtfirstquarterto.setText(""+df.format(prevYear.getTime()));


//            //second quarter


            prevYear.set(Calendar.DATE, 1);
            prevYear.set(Calendar.MONTH, 6);
            txtsecondquarterfrom.setText(""+df.format(prevYear.getTime()));
            YearMonth fzzObject = YearMonth.of(earlierOneMonth.getYear(), 9);
            int dayzzsend = fzzObject.lengthOfMonth();
            prevYear.set(Calendar.DATE, dayzzsend);
            prevYear.set(Calendar.MONTH, 8);
            txtsecondquarterto.setText(""+df.format(prevYear.getTime()));
            prevYear.set(Calendar.DATE, 1);
            prevYear.set(Calendar.MONTH, 9);
            txtthirdquarterfrom.setText(""+df.format(prevYear.getTime()));
            YearMonth fzzzObject = YearMonth.of(earlierOneMonth.getYear(), 12);
            int dayzzzsend = fzzzObject.lengthOfMonth();
            prevYear.set(Calendar.DATE, dayzzsend);
            prevYear.set(Calendar.MONTH, 11);
            txtthirdquarterto.setText(""+df.format(prevYear.getTime()));

            //fourth quarter
            Calendar firstDayOfCurrentYear = Calendar.getInstance();
            firstDayOfCurrentYear.set(Calendar.DATE, 1);
            firstDayOfCurrentYear.set(Calendar.MONTH, 0);
            txtfourthquarterfrom.setText(""+df.format(firstDayOfCurrentYear.getTime()));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDateTime nowdate = LocalDateTime.now();
            txtfourthquarterto.setText(""+dtf.format(nowdate));


        }else if(quarter==2)
        {
            LocalDate now = LocalDate.now();
            int monthval=now.getMonthValue();
            //Log.e("monthval","@"+monthval);
            int totval=6+monthval;
            //Log.e("totval","@"+totval);
            LocalDate earlierOneMonth = now.minusMonths(totval);
            //Log.e("earlierOneMonth","@"+earlierOneMonth);

            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
            //first quarter
            Calendar prevYear = Calendar.getInstance();
            prevYear.add(Calendar.YEAR, -1);
            prevYear.set(Calendar.DATE, 1);
            prevYear.set(Calendar.MONTH, earlierOneMonth.getMonthValue());
            txtfirstquarterfrom.setText(""+df.format(prevYear.getTime()));
            YearMonth fzObject = YearMonth.of(earlierOneMonth.getYear(), 9);
            int dayzsend = fzObject.lengthOfMonth();
            prevYear.set(Calendar.DATE, dayzsend);
            prevYear.set(Calendar.MONTH, 8);
            txtfirstquarterto.setText(""+df.format(prevYear.getTime()));


//            //second quarter

          //  prevYear.add(Calendar.YEAR, -1);
            prevYear.set(Calendar.DATE, 1);
            prevYear.set(Calendar.MONTH, 9);
            txtsecondquarterfrom.setText(""+df.format(prevYear.getTime()));
            YearMonth fzzObject = YearMonth.of(earlierOneMonth.getYear(), 12);
            int dayzzsend = fzzObject.lengthOfMonth();
            prevYear.set(Calendar.DATE, dayzzsend);
            prevYear.set(Calendar.MONTH, 11);
            txtsecondquarterto.setText(""+df.format(prevYear.getTime()));

            //third quarter
            Calendar firstDayOfCurrentYear = Calendar.getInstance();
            firstDayOfCurrentYear.set(Calendar.DATE, 1);
            firstDayOfCurrentYear.set(Calendar.MONTH, 0);
            txtthirdquarterfrom.setText(""+df.format(firstDayOfCurrentYear.getTime()));
            YearMonth secondquarterObject = YearMonth.of(now.getYear(), 3);
            int secdaysend = secondquarterObject.lengthOfMonth();
            //Log.e("secdaysend","@"+secdaysend);
            firstDayOfCurrentYear.set(Calendar.DATE, secdaysend);
            firstDayOfCurrentYear.set(Calendar.MONTH, 2);
            txtthirdquarterto.setText(""+df.format(firstDayOfCurrentYear.getTime()));

            //fourth quarter

            firstDayOfCurrentYear.set(Calendar.DATE, 1);
            firstDayOfCurrentYear.set(Calendar.MONTH, 3);
            txtfourthquarterfrom.setText(""+df.format(firstDayOfCurrentYear.getTime()));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDateTime nowdate = LocalDateTime.now();
            txtfourthquarterto.setText(""+dtf.format(nowdate));



        }else if(quarter==3)
        {
            LocalDate now = LocalDate.now();
            int monthval=now.getMonthValue();
            int totval=3+monthval;
            LocalDate earlierOneMonth = now.minusMonths(totval);
            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");

            //first quarter
            Calendar prevYear = Calendar.getInstance();
            prevYear.add(Calendar.YEAR, -1);
            prevYear.set(Calendar.DATE, 1);
            prevYear.set(Calendar.MONTH, earlierOneMonth.getMonthValue());
            txtfirstquarterfrom.setText(""+df.format(prevYear.getTime()));
            YearMonth fzObject = YearMonth.of(earlierOneMonth.getYear(), 12);
            int dayzsend = fzObject.lengthOfMonth();
            prevYear.set(Calendar.DATE, dayzsend);
            prevYear.set(Calendar.MONTH, 11);
            txtfirstquarterto.setText(""+df.format(prevYear.getTime()));


            //second quarter
            Calendar firstDayOfCurrentYear = Calendar.getInstance();
            firstDayOfCurrentYear.set(Calendar.DATE, 1);
            firstDayOfCurrentYear.set(Calendar.MONTH, 0);
            txtsecondquarterfrom.setText(""+df.format(firstDayOfCurrentYear.getTime()));
            YearMonth firstquarterObject = YearMonth.of(now.getYear(), 3);
            int daysend = firstquarterObject.lengthOfMonth();
            firstDayOfCurrentYear.set(Calendar.DATE, daysend);
            firstDayOfCurrentYear.set(Calendar.MONTH, 2);
            txtsecondquarterto.setText(""+df.format(firstDayOfCurrentYear.getTime()));

            //third quarter

            firstDayOfCurrentYear.set(Calendar.DATE, 1);
            firstDayOfCurrentYear.set(Calendar.MONTH, 3);
            txtthirdquarterfrom.setText(""+df.format(firstDayOfCurrentYear.getTime()));
            YearMonth secondquarterObject = YearMonth.of(now.getYear(), 6);
            int secdaysend = secondquarterObject.lengthOfMonth();
            //Log.e("secdaysend","@"+secdaysend);
            firstDayOfCurrentYear.set(Calendar.DATE, secdaysend);
            firstDayOfCurrentYear.set(Calendar.MONTH, 5);
            txtthirdquarterto.setText(""+df.format(firstDayOfCurrentYear.getTime()));

            //fourth quarter

            firstDayOfCurrentYear.set(Calendar.DATE, 1);
            firstDayOfCurrentYear.set(Calendar.MONTH, 6);
            txtfourthquarterfrom.setText(""+df.format(firstDayOfCurrentYear.getTime()));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDateTime nowdate = LocalDateTime.now();
            txtfourthquarterto.setText(""+dtf.format(nowdate));


        }else if(quarter==4)
        {
            LocalDate now = LocalDate.now();
            int monthval=now.getMonthValue();
            //Log.e("monthval","@"+monthval);
            int totval=monthval;
            //Log.e("totval","@"+totval);
            LocalDate earlierOneMonth = now.minusMonths(totval);
            //Log.e("earlierOneMonth","@"+earlierOneMonth);
            YearMonth yearMonthObject = YearMonth.of(earlierOneMonth.getYear(), earlierOneMonth.getMonthValue());
            int daysInMonth = yearMonthObject.lengthOfMonth();

            //Log.e("daysInMonth","@"+daysInMonth);


            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");

// Create first day of year

            //first quarter
            Calendar firstDayOfCurrentYear = Calendar.getInstance();
            firstDayOfCurrentYear.set(Calendar.DATE, 1);
            firstDayOfCurrentYear.set(Calendar.MONTH, 0);
            txtfirstquarterfrom.setText(""+df.format(firstDayOfCurrentYear.getTime()));
            YearMonth firstquarterObject = YearMonth.of(now.getYear(), 3);
            int daysend = firstquarterObject.lengthOfMonth();
            firstDayOfCurrentYear.set(Calendar.DATE, daysend);
            firstDayOfCurrentYear.set(Calendar.MONTH, 2);
            txtfirstquarterto.setText(""+df.format(firstDayOfCurrentYear.getTime()));

            //secoond quarter

            firstDayOfCurrentYear.set(Calendar.DATE, 1);
            firstDayOfCurrentYear.set(Calendar.MONTH, 3);
            txtsecondquarterfrom.setText(""+df.format(firstDayOfCurrentYear.getTime()));
            YearMonth secondquarterObject = YearMonth.of(now.getYear(), 6);
            int secdaysend = secondquarterObject.lengthOfMonth();
            //Log.e("secdaysend","@"+secdaysend);
            firstDayOfCurrentYear.set(Calendar.DATE, secdaysend);
            firstDayOfCurrentYear.set(Calendar.MONTH, 5);
            txtsecondquarterto.setText(""+df.format(firstDayOfCurrentYear.getTime()));

            //third quarter

            firstDayOfCurrentYear.set(Calendar.DATE, 1);
            firstDayOfCurrentYear.set(Calendar.MONTH, 6);
            txtthirdquarterfrom.setText(""+df.format(firstDayOfCurrentYear.getTime()));
            YearMonth thirdquarterObject = YearMonth.of(now.getYear(), 9);
            int thirdaysend = thirdquarterObject.lengthOfMonth();
            //Log.e("secdaysend","@"+thirdaysend);
            firstDayOfCurrentYear.set(Calendar.DATE, thirdaysend);
            firstDayOfCurrentYear.set(Calendar.MONTH, 8);
            txtthirdquarterto.setText(""+df.format(firstDayOfCurrentYear.getTime()));

            //fourth quarter

            firstDayOfCurrentYear.set(Calendar.DATE, 1);
            firstDayOfCurrentYear.set(Calendar.MONTH, 9);
            txtfourthquarterfrom.setText(""+df.format(firstDayOfCurrentYear.getTime()));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDateTime nowdate = LocalDateTime.now();
            txtfourthquarterto.setText(""+dtf.format(nowdate));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}