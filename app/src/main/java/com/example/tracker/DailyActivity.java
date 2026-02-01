package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

public class DailyActivity extends AppCompatActivity {

   //private Map<String,String> dailyDataMap;//日付ごとのデータ取得
    private  Map<String, List<String>> dailyDataMap;//複数タスクへ
    private TextView dateText;
    private Calendar currentDate;
    private Button prevBtn;
    private Button nextBtn;

    private TextView dailyTextView;
    private EditText editDailyText;

    private Button addBtn;
    private Button saveBtn;

    @Override
    //onclickへは初期値を記載
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        //初期化
        dailyDataMap = new HashMap<>();
        currentDate = Calendar.getInstance();

        //仮
        //dailyDataMap.put("2026/02/01","勉強１");
        //dailyDataMap.put("2026/02/02","勉強２");

        //viewの取得
        dateText = findViewById(R.id.dateText);
        dailyTextView = findViewById(R.id.dailyTextview);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        editDailyText = findViewById(R.id.editDailyText);
        addBtn = findViewById(R.id.addBtn);

        //初期表示
        updateDateText();
        updateDailyText();

        //クリック
        //prevBtn.setOnClickListener(v -> onPrevDay());
        prevBtn.setOnClickListener(v -> {
            currentDate.add(Calendar.DAY_OF_MONTH,-1);
            updateDateText(); //日付表示
            updateDailyText(); //中身表示
        });

        //nextBtn.setOnClickListener(v -> onNextDay());
        nextBtn.setOnClickListener(v -> {
            currentDate.add(Calendar.DAY_OF_MONTH,1);
            updateDateText(); //日付表示
            updateDailyText(); //中身表示
        });

        //追加ボタン
        addBtn.setOnClickListener(v -> {
            String key = getDateKey(currentDate);
            String input = editDailyText.getText().toString().trim();

            if (input.isEmpty()){
                return;
            }

            List<String> list = dailyDataMap.get(key);
            if (list == null){
               list =new ArrayList<>();
            }

            list.add(input);//追記
            dailyDataMap.put(key,list);//Map保存
            editDailyText.setText("");//入力欄を空に
            updateDailyText();//画面表示の更新
        });
    }

    //textviewへ表示
    private void updateDateText(){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);
        dateText.setText(sdf.format(currentDate.getTime()));
    }

    //日付変換
    private String getDateKey(Calendar date){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd", Locale.JAPAN);
        return sdf.format(date.getTime());
    }

    //内容表示を更新
    private void updateDailyText(){
        String dateKey = getDateKey(currentDate);
        List<String> list = dailyDataMap.get(dateKey);//タスク取得
        //タスクなしの場合
        if(list == null || list.isEmpty()){
            dailyTextView.setText("タスクなし");
            return;
        }

        //表示形式
        StringBuilder sb = new StringBuilder();
        for(String task : list){
            sb.append("・").append(task).append("\n");
        }
        //表示
        dailyTextView.setText(sb.toString());
    }
}