package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main_3_2_2 extends AppCompatActivity {
    private BarChart mChart;
    MyDBHelper myDB;
    ArrayList<Float>book_money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_2_2);

        mChart=(BarChart)findViewById(R.id.chart1);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setExtraTopOffset(-30f);
        mChart.setExtraBottomOffset(10f);
        mChart.setExtraLeftOffset(10f);
        mChart.setExtraRightOffset(10f);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(tfRegular);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(8);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        YAxis left = mChart.getAxisLeft();
        left.setDrawLabels(false);
        left.setSpaceTop(25f);
        left.setSpaceBottom(25f);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7f);
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        // THIS IS THE ORIGINAL DATA YOU WANT TO PLOT
        final List<Data>data=new ArrayList<>();
/****************************************日期數字增加****************************************/
        myDB = new MyDBHelper(Main_3_2_2.this);
        book_money = new ArrayList<>();
        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();
        /****************************************日期****************************************/
        String startDate_queryData = bundle.getString("startDate");
        String endDate_queryData = bundle.getString("endtDate");
        String data_query = "SELECT *,sum(金額) FROM library WHERE 日期 between '"+startDate_queryData+"' AND '"+endDate_queryData+"' GROUP BY 日期";
        Log.d("startDate",data_query);
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(data_query, null);
        }
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                book_money.add(cursor.getFloat(2));
            }
        }
        Log.d("金額日期????", String.valueOf(book_money));
        /****************************************日期****************************************/
        /*還沒用到*/String startDate = bundle.getString("startDate").substring(5,10);
        /*還沒用到*/String endDate = bundle.getString("endtDate").substring(5,10);
        /*querySettlement函式*/String startDate_querySettlement = bundle.getString("startDate");
        /*querySettlement函式*/String endDate_querySettlement = bundle.getString("endtDate");
        /*queryBalance函式*/String startDate_queryBalance = bundle.getString("startDate").substring(0,7);
        /*月份數字加上dash符號*/String month_string=bundle.getString("startDate").substring(5,8);
        /*日曆起始日期*/int start_Number = Integer.parseInt(bundle.getString("startDate").substring(8,10));
        /*日曆結束日期*/int end_Number= Integer.parseInt(bundle.getString("endtDate").substring(8,10));

        data.add(new Data(0.5f, queryBalance(startDate_queryBalance)-querySettlement(startDate_querySettlement,endDate_querySettlement), "前日餘絀"));//前日加期間等於累計
        float x_value= 1.5f;
        int i=0;
//        queryData();
        while (start_Number!=end_Number+1 ){
            String number_to_string="";
            number_to_string= String.valueOf(start_Number);
            /*日期逐一顯示*/data.add(new Data(x_value,88f,month_string+number_to_string));
            i=i+1;
            x_value=x_value+1;
            start_Number=start_Number+1;
            Log.d("數字檢查",number_to_string);
        }
        /*本期金額*/data.add(new Data(x_value, querySettlement(startDate_querySettlement,endDate_querySettlement), "本日結算"));
        /*累計餘絀*/data.add(new Data(x_value+1, queryBalance(startDate_queryBalance), "累計餘絀"));
/****************************************日期數字增加****************************************/
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return data.get(Math.min(Math.max((int) value, 0), data.size()-1)).xAxisValue;
            }
        });
        setData(data);
    }


    private  void  setData(List<Data> dataList){
        ArrayList<BarEntry> values=new ArrayList<>();
        List<Integer> colors=new ArrayList<>();

        int green= Color.rgb(110,190,102);
        int red =Color.rgb(211,87,44);

        for (int i=0;i<dataList.size();i++){
            Data d=dataList.get(i);
            BarEntry entry=new BarEntry(d.xValue,d.yValue);
            values.add(entry);

            if (d.yValue>=0){
                colors.add(red);
            }else {
                colors.add(green);
            }
        }

        BarDataSet set;
        set=new BarDataSet(values,"Values");
        set.setColors(colors);
        set.setValueTextColors(colors);

        BarData data=new BarData(set);
        data.setValueTextSize(13f);

        data.setValueFormatter(new ValueFormatter());
        data.setBarWidth(0.4f);

        mChart.setData(data);
        mChart.invalidate();
    }
    private  class Data{
        public String xAxisValue;
        public float xValue;
        public float yValue;

        public Data(float xValue,float yValue,String xAxisValue){
            this.xAxisValue=xAxisValue;
            this.xValue=xValue;
            this.yValue=yValue;

        }
    }
    private class ValueFormatter extends com.github.mikephil.charting.formatter.ValueFormatter implements IValueFormatter {
        private DecimalFormat mFormat;
        public  ValueFormatter(){
            mFormat=new DecimalFormat("#####.0");
        }
        public String getFormattedValue(float value, Entry entry, int daaSetIndex, ViewPortHandler viewPortHandler){
            return mFormat.format(value);
        }
    }
/****************************************日期金額****************************************/
    void queryData(){
        Intent myIntent=getIntent();
        Bundle bundle = myIntent.getExtras();
        String startDate_queryData = bundle.getString("startDate");
        String endDate_queryData = bundle.getString("endtDate");
        String data_query = "SELECT sum(金額) FROM library WHERE 日期 between '"+startDate_queryData+"' AND '"+endDate_queryData+"' GROUP BY 日期";
        Log.d("startDate",data_query);
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(data_query, null);
        }
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                book_money.add(cursor.getFloat(2));
            }
        }
//        return;
    }
/****************************************日期金額****************************************/

/****************************************本期結算金額****************************************/
    float querySettlement(String startDate_querySettlement, String endDate_querySettlement) {
        float num_float=0;
        int num=0;
        String income = "SELECT * FROM library WHERE (日期 between '"+startDate_querySettlement+"' AND '"+endDate_querySettlement+"')AND (狀態 = '收入' )";
        String expense ="SELECT * FROM library WHERE (日期 between '"+startDate_querySettlement+"' AND '"+endDate_querySettlement+"')AND (狀態 = '支出' )";
        Log.d("query1",income);
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = null;
        Cursor cursor1 = null;
        if (db != null) {
            cursor = db.rawQuery(income, null);
            cursor1 = db.rawQuery(expense, null);
        }
        if (cursor.getCount() == 0) {
            num=0;
        } else {
            while (cursor.moveToNext()) {
                num+=(cursor.getInt(2));
            }
        }
        if (cursor1.getCount() == 0) {
            num=0;
        } else {
            while (cursor1.moveToNext()) {
                num-=(cursor1.getInt(2));
            }
        }
        Log.d("string_querySettlement",income);
        num_float=num;
        return num_float;
    }
/****************************************本期結算金額****************************************/

/****************************************累計餘絀金額****************************************/
    float queryBalance(String startDate_queryBalance) {
        float num_float=0;
        int num=0;
        String income = "SELECT * FROM library WHERE (日期 LIKE '"+startDate_queryBalance+"%' )AND (狀態 = '收入' )";
        String expense ="SELECT * FROM library WHERE (日期 LIKE'"+startDate_queryBalance+"%')AND (狀態 = '支出' )";
        Log.d("累計收入",income);
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = null;
        Cursor cursor1 = null;
        if (db != null) {
            cursor = db.rawQuery(income, null);
            cursor1 = db.rawQuery(expense, null);
        }
        if (cursor.getCount() == 0) {
            num=0;
        } else {
            while (cursor.moveToNext()) {
                num+=(cursor.getInt(2));
            }
        }
        if (cursor1.getCount() == 0) {
            num=0;
        } else {
            while (cursor1.moveToNext()) {
                num-=(cursor1.getInt(2));
            }
        }
        num_float=num;
        return num_float;
    }
/****************************************累計餘絀金額****************************************/

///****************************************餘絀金額****************************************/
//    float yesterdayBalance(String startDate_yesterdayBalance) {
//        float num_float=0;
//        int num=0;
//        String income = "SELECT * FROM library WHERE (日期 LIKE '"+startDate_yesterdayBalance+"%' )AND (狀態 = '收入' )";
//        String expense ="SELECT * FROM library WHERE (日期 LIKE'"+startDate_yesterdayBalance+"%')AND (狀態 = '支出' )";
//        Log.d("累計收入",income);
//        SQLiteDatabase db = myDB.getReadableDatabase();
//        Cursor cursor = null;
//        Cursor cursor1 = null;
//        if (db != null) {
//            cursor = db.rawQuery(income, null);
//            cursor1 = db.rawQuery(expense, null);
//        }
//        if (cursor.getCount() == 0) {
//            num=0;
//        } else {
//            while (cursor.moveToNext()) {
//                num+=(cursor.getInt(2));
//            }
//        }
//        if (cursor1.getCount() == 0) {
//            num=0;
//        } else {
//            while (cursor1.moveToNext()) {
//                num-=(cursor1.getInt(2));
//            }
//        }
//        num_float=num;
//        return num_float;
//    }
///****************************************累計餘絀金額****************************************/
}