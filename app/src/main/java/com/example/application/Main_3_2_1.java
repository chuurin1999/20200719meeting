package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Main_3_2_1 extends AppCompatActivity {

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_2_1);

        pieChart=(PieChart)findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.15f);//轉盤旋轉快慢

        pieChart.setDrawHoleEnabled(true);//true空心;false畫滿
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);//中心空心圓半徑白色透明部分

        ArrayList<PieEntry> yValues=new ArrayList<>();

        yValues.add(new PieEntry(34f,"PertyA"));
        yValues.add(new PieEntry(23F,"USA"));
        yValues.add(new PieEntry(14f,"UK"));
        yValues.add(new PieEntry(35f,"India"));
        yValues.add(new PieEntry(40f,"Russia"));
        yValues.add(new PieEntry(23f,"Japan"));

        Description description=new Description();
        description.setText("這是單日圓餅圖");
        description.setTextSize(15);
        pieChart.setDescription(description);

        pieChart.animateY(1400, Easing.EaseInOutCubic);

        PieDataSet dataSet=new PieDataSet(yValues,"收支狀態");
        dataSet.setSliceSpace(1f);//間隔線條粗細
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);

        PieData data=new PieData((dataSet));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
    }
}
