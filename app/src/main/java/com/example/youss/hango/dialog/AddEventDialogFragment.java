package com.example.youss.hango.dialog;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.example.youss.hango.R;
import com.example.youss.hango.services.EventService;
import com.squareup.otto.Subscribe;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEventDialogFragment extends BaseDialog implements View.OnClickListener {

    @BindView(R.id.add_event_editText)
    EditText newHangoName;

    @BindView(R.id.add_event_desc)
    EditText newHangoDescription;

    @BindView(R.id.add_event_date)
    EditText newHangoDate;

    @BindView(R.id.calendaricon)
    ImageView calendarIcon;

    @BindView(R.id.add_event_time)
    EditText newHangoTime;

    @BindView(R.id.timeicon)
    ImageView timeIcon;

    final Calendar calendar = Calendar.getInstance();

    public static AddEventDialogFragment newInstance() {
        return new AddEventDialogFragment();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View rootView = layoutInflater.inflate(R.layout.add_event, null);
        ButterKnife.bind(this, rootView);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(rootView).
                setPositiveButton("Create", null).setNegativeButton("Cancel", null).show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setOnClickListener(this);
        return alertDialog;


    }

    @Override
    public void onClick(View view) {
        mybus.post(new EventService.AddEventRequest(newHangoName.getText().toString(), userName, userEmail, newHangoDescription.getText().toString(),
                newHangoDate.getText().toString(),newHangoTime.getText().toString()));
    }

    @Subscribe
    public void addEvent(EventService.AddEventResponse response) {
        if (!response.didSucceed()) {
            newHangoName.setError(response.getError("HangoName"));
            newHangoDescription.setError(response.getError("HangoDescription"));
            newHangoDate.setError(response.getError("HangoDate"));
            newHangoTime.setError(response.getError("HangoTime"));
        } else {
            dismiss();
        }
    }

    @OnClick(R.id.calendaricon)
    public void showDatePickerDialog(View view) {
        DatePickerDialog StartTime = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(newHangoDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        StartTime.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        StartTime.show();
    }

    @OnClick(R.id.timeicon)
    public void showTimePickerDialog(View view) {
        TimePickerDialog StartTime = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateLabel2(newHangoTime);
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
        StartTime.show();
    }
    public void updateLabel(EditText Date) {
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
        Date.setText(sdf.format(calendar.getTime()));
    }
    public void updateLabel2(EditText Time){
        String format="HH:mm";
        SimpleDateFormat sdf=new SimpleDateFormat(format,Locale.CANADA);
        Time.setText(sdf.format(calendar.getTime()));
    }
}
