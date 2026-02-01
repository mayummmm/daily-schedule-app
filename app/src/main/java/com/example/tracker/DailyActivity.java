package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DailyActivity extends AppCompatActivity {

    private TextView dateText;
    private Calendar currentDate;
    private Button prevBtn;
    private Button nextBtn;

    @Override
    //onclickへは初期値を記載
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily);

        //日付取得
        dateText = findViewById(R.id.dateText);
        //今日の日付
        currentDate = Calendar.getInstance();

        updateDateText();

        //ボタン取得
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);

        //クリック
        prevBtn.setOnClickListener(v -> onPrevDay());
        nextBtn.setOnClickListener(v -> onNextDay());

    }

    //
    private void updateDateText(){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);
        dateText.setText(sdf.format(currentDate.getTime()));
    }

    //前日
    private void onPrevDay(){
        currentDate.add(Calendar.DAY_OF_MONTH,-1);
        updateDateText();
    }

    //次日
    private void onNextDay(){
        currentDate.add(Calendar.DAY_OF_MONTH,+1);
        updateDateText();
    }



}