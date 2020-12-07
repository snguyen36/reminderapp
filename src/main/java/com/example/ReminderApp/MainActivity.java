package com.example.ReminderApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    TextView textView;
    EditText editText;
    TimePicker timePicker;
    DatePicker datePicker;
    Switch minute;
    Switch hour;
    Switch day;
    Switch week;
    Switch month;
    Switch year;
    Calendar c;
    String[] reminder = {"Reminder1", "Reminder2", "Reminder3", "Reminder4", "Reminder5"};
    String[] setDate = {"No time set", "No time set", "No time set", "No time set" ,"No time set"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomAdapter adapter = new
                CustomAdapter(MainActivity.this, reminder, setDate);
        listView = (ListView)findViewById(R.id.ListView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cancelAlarm(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView = (TextView)findViewById(R.id.item1);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_layout, viewGroup, false);
                editText = (EditText) dialogView.findViewById(R.id.editText);
                timePicker = (TimePicker)dialogView.findViewById(R.id.timePicker);
                datePicker = dialogView.findViewById(R.id.datePicker);
                builder.setView(dialogView).setTitle("Edit")
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (TextUtils.isEmpty(editText.getText().toString())) {
                                }
                                else {
                                    String name = editText.getText().toString();
                                    reminder[position] = name;
                                }
                                c = Calendar.getInstance();
                                c.set(Calendar.YEAR, datePicker.getYear());
                                c.set(Calendar.MONTH, datePicker.getMonth());
                                c.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                                c.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                                c.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                                c.set(Calendar.SECOND, 0);
                                setDate[position] = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime()) +
                                        " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
                                minute = dialogView.findViewById(R.id.Minute);
                                hour = dialogView.findViewById(R.id.Hour);
                                day = dialogView.findViewById(R.id.Day);
                                week = dialogView.findViewById(R.id.Week);
                                month = dialogView.findViewById(R.id.Month);
                                year = dialogView.findViewById(R.id.Year);
                                if (minute.isChecked()) {
                                    repeatMinute(c, position);
                                }
                                else if (hour.isChecked()) {
                                    repeatHour(c, position);
                                }
                                else if (day.isChecked()) {
                                    repeatDay(c, position);
                                }
                                else if (week.isChecked()) {
                                    repeatWeek(c, position);
                                }
                                else if (month.isChecked()) {
                                    repeatMonth(c, position);
                                }
                                else if (year.isChecked()) {
                                    repeatYear(c, position);
                                }
                                else {
                                startAlarm(c, position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    private void startAlarm(Calendar c, int position) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, position, intent, 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
        else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }

    }
    private void repeatMinute(Calendar c, int position) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, position, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 60000, pendingIntent);
        setDate[position] += " Repeating every Minute";
    }
    private void repeatHour(Calendar c, int position) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, position, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent);
        setDate[position] += " Repeating every Hour";
    }
    private void repeatDay(Calendar c, int position) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, position, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        setDate[position] += " Repeating every Day";
    }
    private void repeatWeek(Calendar c, int position) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, position, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        setDate[position] += " Repeating every Week";
    }
    private void repeatMonth(Calendar c, int position) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, position, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY * c.getActualMaximum(Calendar.DAY_OF_MONTH), pendingIntent);
        setDate[position] += " Repeating every Month";
    }
    private void repeatYear(Calendar c, int position) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, position, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 365, pendingIntent);
        setDate[position] += " Repeating every Year";
    }
    private void cancelAlarm(int position) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, position, intent, 0);
        alarmManager.cancel(pendingIntent);
        reminder[position] = "alarm cancelled";
        setDate[position] = "no time set";
    }
}