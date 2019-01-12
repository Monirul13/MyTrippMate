package com.example.rana.mytrippmate;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rana.mytrippmate.pojoclasses.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment {

    private EditText eventNameET,startingLocationET,destinationET,departureTimeET,budgetET;
    private Button saveEventButton;
    private Context context;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference rootReference;
    private DatabaseReference userReference;
    private DatabaseReference eventReference;

    public AddEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        rootReference= FirebaseDatabase.getInstance().getReference();
        userReference=rootReference.child("Users").child(user.getUid());
        eventReference=userReference.child("Events");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_event, container, false);
        eventNameET=view.findViewById(R.id.eventName_ET_Id);
        startingLocationET=view.findViewById(R.id.startingLocation_ET_id);
        destinationET=view.findViewById(R.id.destination_ET_id);
        departureTimeET=view.findViewById(R.id.departure_ET_id);
        budgetET=view.findViewById(R.id.budget_ET_id);

        saveEventButton=view.findViewById(R.id.eventSaveButton);

        saveEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName=eventNameET.getText().toString();
                String sLocation=startingLocationET.getText().toString();
                String destination=destinationET.getText().toString();
                String departureTime=departureTimeET.getText().toString();
                String budget=budgetET.getText().toString();

                boolean isValid=validate(eventNameET,startingLocationET,destinationET,departureTimeET,budgetET);


                if(isValid)
                {
                    String key=eventReference.push().getKey();
                    Event event=new Event(key,eventName,sLocation,destination,departureTime,budget);
                    eventReference.child(key).setValue(event);

                    //fragmentManager i don't get getSupportfragmentmanager();
                }
                Toast.makeText(context,"Event inserted successfully",Toast.LENGTH_LONG).show();;

                clearFields(eventNameET,startingLocationET,destinationET,departureTimeET,budgetET);

            }
        });

        return view;
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
                editText.setError("Required field");
                b = false;
            }
        }

        return b;
    }

}
