package com.kookeries.shop.ui.widgets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.kookeries.shop.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerWidget implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText editText;
    private TextView textView;
    private Calendar myCalendar;
    private Context mContext;

    public DatePickerWidget(TextView view, Context context){
        this.mContext = context;
        this.textView = view;
        textView.setOnClickListener(this);
        myCalendar = Calendar.getInstance();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)     {

        String myFormat = "MMM dd, yyyy"; //In which you need put here
        SimpleDateFormat format = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        textView.setText(format.format(myCalendar.getTime()));

    }

    @Override
    public void onClick(View view) {
        new DatePickerDialog(mContext, R.style.DialogTheme, this, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}