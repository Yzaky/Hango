package com.example.youss.hango.views.EventListViews;

import android.support.v7.widget.RecyclerView;
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

        if(event.getdateCreated()!=null)
        {
            DateCreated.setText(new SimpleDateFormat().format(new Date((long)event.getdateCreated())));

        }
    }

}
