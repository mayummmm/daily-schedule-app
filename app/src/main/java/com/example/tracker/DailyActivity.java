package com.example.tracker;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.CheckBox;

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

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class DailyActivity extends AppCompatActivity {

   //private Map<String,String> dailyDataMap;//日付ごとのデータ取得
    private  Map<String, List<Task>> dailyDataMap;//複数タスクへ
    private TextView dateText;
    private Calendar currentDate;
    private Button prevBtn;
    private Button nextBtn;

    private EditText editDailyText;

    private Button addBtn;
    private LinearLayout taskContainer;

    private Button backBtn;

    @Override
    //onclickへは初期値を記載
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        //初期化
        currentDate = Calendar.getInstance();
        loadData();   // ★ 起動時に復元

        //仮
        //dailyDataMap.put("2026/02/01","勉強１");
        //dailyDataMap.put("2026/02/02","勉強２");

        //viewの取得
        dateText = findViewById(R.id.dateText);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        editDailyText = findViewById(R.id.editDailyText);
        addBtn = findViewById(R.id.addBtn);
        taskContainer = findViewById(R.id.taskContainer);
        backBtn = findViewById(R.id.backBtn);

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

            List<Task> list = dailyDataMap.get(key);
            if (list == null){
               list =new ArrayList<>();
            }

            list.add(new Task(input));//追記
            dailyDataMap.put(key,list);//Map保存

            saveData();

            editDailyText.setText("");//入力欄を空に
            updateDailyText();//画面表示の更新
        });

        backBtn.setOnClickListener(v -> {
            finish();
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
        List<Task> list = dailyDataMap.get(dateKey);//タスク取得

        taskContainer.removeAllViews();//中身削除

        //タスクなしの場合
        if(list == null || list.isEmpty()){
            TextView emptyText = new TextView(DailyActivity.this);
            emptyText.setText("タスクなし");
            taskContainer.addView(emptyText);//コンテナ追加
            return;
        }

        //タスクあり
        for (Task task : list) {
            // 横並び用レイアウト
            LinearLayout row = new LinearLayout(DailyActivity.this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            // チェックボックス
            CheckBox checkBox = new CheckBox(DailyActivity.this);
            checkBox.setText(task.text);
            checkBox.setChecked(task.checked);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.checked = isChecked;
                saveData();
            });

            // 削除ボタン（×）
            TextView deleteBtn = new TextView(DailyActivity.this);
            deleteBtn.setText("×");
            deleteBtn.setTextSize(18);
            deleteBtn.setPadding(24, 0, 24, 0);

            deleteBtn.setOnClickListener(v -> {
                list.remove(task);      // ★ このタスクだけ削除
                updateDailyText();      // 再描画
                saveData();
            });

            row.addView(checkBox);
            row.addView(deleteBtn);

            taskContainer.addView(row);
        }
    }
    //保存用
    private void saveData() {
        SharedPreferences prefs = getSharedPreferences("task_prefs", MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(dailyDataMap);

        editor.putString("task_data", json);
        editor.apply();
    }

    //読み込み用
    private void loadData() {
        SharedPreferences prefs = getSharedPreferences("task_prefs", MODE_PRIVATE);

        String json = prefs.getString("task_data", null);

        if (json == null) {
            dailyDataMap = new HashMap<>();
            return;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, List<Task>>>() {}.getType();
        dailyDataMap = gson.fromJson(json, type);
    }

}