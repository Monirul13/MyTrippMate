package com.example.rana.mytrippmate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rana.mytrippmate.pojoclasses.Event;

import java.util.List;

/**
 * Created by Rana on 12/8/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private Context context;
    private List<Event> eventList;
    private MenuActionListener listener;

    public EventAdapter(Context context, List<Event> eventList,MenuActionListener listener) {
        this.context = context;
        this.eventList = eventList;
        this.listener=listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recyclerview_row_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder,final int position) {
        holder.eventNameTV.setText(eventList.get(position).getEventName());
        holder.departureDateTV.setText(eventList.get(position).getDepartureTime());
        holder.budgetTV.setText(eventList.get(position).getBudget());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event=eventList.get(position);
                listener.onClickRecyclerViewItem(event);
                //Toast.makeText(context, "Event Info" +event.getId()+event.getEventName()+event.getDestination()+event.getBudget(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.menuTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context,v);
                MenuInflater inflater=popupMenu.getMenuInflater();
                inflater.inflate(R.menu.event_menu,popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        String id=eventList.get(position).getId();
                        String ename=eventList.get(position).getEventName();
                        String startLocation=eventList.get(position).getStartingLocation();
                        String destination=eventList.get(position).getDestination();
                        String departureDate=eventList.get(position).getDepartureTime();
                        String budget=eventList.get(position).getBudget();

                        Event event=new Event(id,ename,startLocation,destination,departureDate,budget);

                        switch (item.getItemId()){
                            case R.id.menu_Edit:
                                listener.onEventEdit(event);
                                break;
                            case R.id.menu_Delete:
                                listener.onEventDelete(id);
                                break;
                        }

                        return true;
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


    class EventViewHolder extends RecyclerView.ViewHolder{

        private TextView eventNameTV,departureDateTV,budgetTV,menuTV;
        public EventViewHolder(View itemView) {
            super(itemView);

            eventNameTV=itemView.findViewById(R.id.row_eventName_TV_id);
            departureDateTV=itemView.findViewById(R.id.row_eventDepartureDate_TV_id);
            budgetTV=itemView.findViewById(R.id.row_eventBudget_TV_id);
            menuTV=itemView.findViewById(R.id.menuTV_id);
        }
    }

    public void updateAdapter(List<Event> eventList)
    {
         this.eventList=eventList;
         notifyDataSetChanged();
    }


    public interface MenuActionListener{
        void onEventEdit(Event event);
        void onEventDelete(String eventId);
        void onClickRecyclerViewItem(Event event);
    }
}
