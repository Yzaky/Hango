package com.example.youss.hango.views.EventListViews;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.example.youss.hango.R;
import com.example.youss.hango.entities.Event;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.events_list_layout)
    public View LayoutView;

    @BindView(R.id.events_list_OwnerName)
    TextView creator;

    @BindView(R.id.events_list_name)
    TextView EventName;

    @BindView(R.id.events_list_dateCreated)
    TextView DateCreated;

    public EventsListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }

    public void populate(Event event)
    {
        creator.setText(event.getcreator());
        EventName.setText(event.geteventName());

            //DateCreated.setText(new SimpleDateFormat().format(new Date(event.getdateCreated())));
            long now = System.currentTimeMillis();
            long due = event.getdateCreated();
            String relative = DateUtils.getRelativeTimeSpanString(due, now, DateUtils.MINUTE_IN_MILLIS).toString();
            DateCreated.setText(relative);
    }

}
