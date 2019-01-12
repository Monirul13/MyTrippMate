package com.example.rana.mytrippmate;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;

import com.example.rana.mytrippmate.pojoclasses.Event;
import com.example.rana.mytrippmate.pojoclasses.EventExpenditure;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Context context;
    private TextView eventNameTV, eventDestinationTV, startingLocationTV, departuretimeTV, eventBudgetTV,initProgTV;
    private ProgressBar progressBar;
    private ExpandableListView expandableListView;
    //private RecyclerView expRecyclerView;
    private ExpeditureRecyclerViewAdapter expeditureRecyclerViewAdapter;

    private HashMap<String, List<String>> expandableListDetail;
    private List<String> expandableListTitle;
    private List<EventExpenditure> expenditureList=new ArrayList<>();
    private String eventKey;
    private Event event;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference rootReference;
    private DatabaseReference eventReference;
    private DatabaseReference userReference;
    private DatabaseReference expenditureReference;

    private List<EventExpenditure> costList=new ArrayList<>();
    private CustomExpandableListAdapter customExpandableListAdapter;

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        prepareExpandableListViewData();
        progressBar=view.findViewById(R.id.budget_progressBar_id);



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        rootReference = FirebaseDatabase.getInstance().getReference();
        userReference = rootReference.child("Users").child(user.getUid());
        eventReference = userReference.child("Events");

        eventNameTV = view.findViewById(R.id.eventNameTv);
        initProgTV=view.findViewById(R.id.init_progress_TV_id);
        //eventDestinationTV=view.findViewById(R.id.eventDestinationTv);
        //startingLocationTV=view.findViewById(R.id.eventStartingLocationTV);
        //departuretimeTV=view.findViewById(R.id.departureTimeTV);
        eventBudgetTV = view.findViewById(R.id.eventBudgetTV);
        Bundle bundle = getArguments();
        event = (Event) bundle.getSerializable("event");

        eventNameTV.setText(event.getEventName());

        expenditureReference=userReference.child("Expenditure").child(event.getId());


        expenditureReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double cost=0;
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    EventExpenditure expenditure=data.getValue(EventExpenditure.class);
                    costList.add(expenditure);
                    cost=cost+expenditure.getExpCost();
                }
                eventBudgetTV.setText("Budget Status ( " +cost+ "/" + event.getBudget() + " )");
                int percent= (int) (cost/100);
                progressBar.incrementProgressBy(percent);
                initProgTV.setText(String.valueOf(percent)+"%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        progressBar.setMax(100);
        //eventKey=event.getId();
        //eventDestinationTV.setText(event.getDestination());
        //startingLocationTV.setText(event.getStartingLocation());
        //departuretimeTV.setText(event.getDepartureTime());
        expandableListView = view.findViewById(R.id.expandableListView_id);

        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        customExpandableListAdapter = new CustomExpandableListAdapter(context, expandableListDetail, expandableListTitle);
        expandableListView.setAdapter(customExpandableListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //Toast.makeText(context,"Event id : "+event.getId(),Toast.LENGTH_LONG).show();
                final String selected = (String) customExpandableListAdapter.getChild(groupPosition, childPosition);
                eventKey = event.getId();
                switch (selected) {

                    case "Edit Event":
                        //Toast.makeText(context,"Clicked on: "+selected,Toast.LENGTH_LONG).show();
                        onEventEdit(eventKey);
                        break;
                    case "Delete Event":
                        //Toast.makeText(context,"Clicked on: "+selected,Toast.LENGTH_LONG).show();
                        onEventDelete(eventKey);
                        break;
                    case "Add New Expense":
                        //Toast.makeText(context,"Clicked on: "+selected,Toast.LENGTH_LONG).show();
                        onAddNewExpense();
                        break;
                    case "View All Expense":
                        //Toast.makeText(context, "Clicked on: " + selected, Toast.LENGTH_LONG).show();
                        onViewAllExpenses();
                        break;
                    case "Add More Budget":
                        //Toast.makeText(context, "Clicked on: " + selected, Toast.LENGTH_LONG).show();
                        onAddMoreBudget();
                        break;
                    case "Take a Photo":
                        //Toast.makeText(context, "Clicked on: " + selected, Toast.LENGTH_LONG).show();
                        onTakePhoto();
                        break;
                    case "View Gallery":
                        //Toast.makeText(context, "Clicked on: " + selected, Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(context,ImageGalleryActivity.class);
                        startActivity(intent);
                        break;
                    case "View All Moments":
                        //Toast.makeText(context, "Clicked on: " + selected, Toast.LENGTH_LONG).show();
                        Intent intent1=new Intent(context,ImageGalleryActivity.class);
                        startActivity(intent1);
                        break;


                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void prepareExpandableListViewData() {

        expandableListDetail = new HashMap<String, List<String>>();

        List<String> moreOnEvents = new ArrayList<>();
        moreOnEvents.add("Edit Event");
        moreOnEvents.add("Delete Event");

        List<String> moments = new ArrayList<>();
        moments.add("Take a Photo");
        moments.add("View Gallery");
        moments.add("View All Moments");

        List<String> expenditures = new ArrayList<>();
        expenditures.add("Add New Expense");
        expenditures.add("View All Expense");
        expenditures.add("Add More Budget");


        expandableListDetail.put("More On Event", moreOnEvents);
        expandableListDetail.put("Moments", moments);
        expandableListDetail.put("Expenditure", expenditures);

    }

    public void onEventDelete(String eventId) {
        eventReference.child(eventId).removeValue();
    }

    public void onEventEdit(final String eventKey) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.edit_event_layout,null,false);

        final EditText nameET=view.findViewById(R.id.edit_ename_ET_id);
        final EditText startLocationET=view.findViewById(R.id.edit_eStartLoaction_ET_id);
        final EditText destinationET=view.findViewById(R.id.edit_edestination_ET_id);
        final EditText departureTimeET=view.findViewById(R.id.edit_edepatureTime_ET_id);
        final EditText budgetET=view.findViewById(R.id.edit_ebudget_ET_id);

        nameET.setText(event.getEventName());
        startLocationET.setText(event.getStartingLocation());
        destinationET.setText(event.getDestination());
        departureTimeET.setText(event.getDepartureTime());
        budgetET.setText(event.getBudget());


        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Edit Event");
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean isValidate=validate(nameET,startLocationET,destinationET,departureTimeET,budgetET);
                if(isValidate)
                {
                    String eventName=nameET.getText().toString();
                    String sLocation=startLocationET.getText().toString();
                    String destination=destinationET.getText().toString();
                    String departureTime=departureTimeET.getText().toString();
                    String budget=budgetET.getText().toString();

                    Event event = new Event(eventKey, eventName, sLocation, destination, departureTime, budget);
                    eventReference.child(eventKey).setValue(event);
                }
                clearFields(nameET,startLocationET,destinationET,departureTimeET,budgetET);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });



        builder.show();



    }

    public void onAddNewExpense() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_expense_layout, null, false);

        final EditText expCostET=view.findViewById(R.id.exp_cost_ET_id);
        final EditText expCommentET=view.findViewById(R.id.exp_comment_ET_id);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add New Expense");
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean isValid=validate(expCostET,expCommentET);
                if(isValid)
                {
                    String comment=expCommentET.getText().toString();
                    String cost=expCostET.getText().toString();
                    String key=expenditureReference.push().getKey();
                    EventExpenditure eventExpenditure=new EventExpenditure(key,comment,Double.parseDouble(cost));
                    expenditureReference.child(key).setValue(eventExpenditure);
                    Toast.makeText(context,"Event Expenditure inserted successfully",Toast.LENGTH_LONG).show();
                }
                clearFields(expCostET,expCommentET);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void onViewAllExpenses()
    {
        expenditureReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                expenditureList.clear();
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    EventExpenditure expenditure=data.getValue(EventExpenditure.class);
                    expenditureList.add(expenditure);
                }

                Toast.makeText(context,"No of data found :"+expenditureList.size(),Toast.LENGTH_LONG).show();

                onShowAllExpenditure(expenditureList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void onShowAllExpenditure(List<EventExpenditure> expenditureList)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.allexpenditure_layout,null, false);

        builder.setView(view);
        builder.setTitle("All Expenses");
        builder.setNegativeButton("Ok",null);
        builder.setCancelable(false);
        AlertDialog alertDialog=builder.create();

        RecyclerView expRecyclerView=alertDialog.findViewById(R.id.allexp_recycleView_id);
        expeditureRecyclerViewAdapter=new ExpeditureRecyclerViewAdapter(context,expenditureList);
        //expRecyclerView.setAdapter(expeditureRecyclerViewAdapter);

        alertDialog.show();


    }


    public double getTotalCost()
    {
         double totalcost=0;
        expenditureReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double cost=0;
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    EventExpenditure expenditure=data.getValue(EventExpenditure.class);
                    costList.add(expenditure);
                    cost=cost+expenditure.getExpCost();
                }

                Toast.makeText(context,"No of data found :"+cost,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        for(EventExpenditure expenditure:costList)
        {
            totalcost=totalcost+expenditure.getExpCost();
        }

        Toast.makeText(context,"No of data found :"+totalcost,Toast.LENGTH_LONG).show();
        return totalcost;

    }


   public void onAddMoreBudget()
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.add_more_budget_layout, null, false);

//        final EditText expCostET=view.findViewById(R.id.exp_cost_ET_id);
//        final EditText expCommentET=view.findViewById(R.id.exp_comment_ET_id);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add More budget");
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText editText=view.findViewById(R.id.add_more_budget_ET_id);
                String budget=editText.getText().toString();

                if(budget.isEmpty())
                {
                    editText.requestFocus();
                    editText.setText("Field is required");
                }
                else
                {
                    double extra= Double.parseDouble(budget);
                    double newBudget= Double.parseDouble(event.getBudget())+extra;

                    Event e = new Event(event.getId(), event.getEventName(),event.getStartingLocation(),event.getDestination(),event.getDepartureTime(),String.valueOf(newBudget));
                    eventReference.child(eventKey).setValue(e);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


   public void onTakePhoto()
   {
       Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
           startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
       }
   }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE)
        {
            if(resultCode == Activity.RESULT_OK)
            {

            }
        }
    }

    //Clear EditText
    public void clearFields(EditText... editTexts) {
        for(EditText editText : editTexts){
            editText.setText("");
        }
    }

    //EditText validation
    public boolean validate(EditText... editTexts){
        boolean b = true;

        for(EditText editText : editTexts){
            if(editText.getText().toString().isEmpty()){
                editText.requestFocus();
                editText.setError("Required field");
                b = false;
            }
        }

        return b;
    }

}
