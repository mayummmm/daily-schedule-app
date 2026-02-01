package com.example.tracker;

public class Task {
        public String text;      // タスク内容
        public boolean checked;  // チェック状態

        public Task(String text) {
            this.text = text;
            this.checked = false;
        }
}

