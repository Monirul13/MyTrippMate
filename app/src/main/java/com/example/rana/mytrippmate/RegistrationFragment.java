package com.example.rana.mytrippmate;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private EditText nameET,emailET,phoneET,passwordET;
    private Button registerButton;
    private Context context;
    private SignUpIntefaceListener signUpIntefaceListener;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private MainActivity mainActivity;


    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_registration, container, false);
        nameET=view.findViewById(R.id.name_ET_id);
        emailET=view.findViewById(R.id.email_ET_id);
        phoneET=view.findViewById(R.id.phone_ET_id);
        passwordET=view.findViewById(R.id.password_ET_id);
        registerButton=view.findViewById(R.id.signUp_btn_id);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=nameET.getText().toString();
                String email=emailET.getText().toString();
                String phone=phoneET.getText().toString();
                String password=passwordET.getText().toString();

                boolean isValid =mainActivity.validate(nameET,emailET,phoneET,passwordET);

                if(isValid)
                {
                    auth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        user=auth.getCurrentUser();
                                        Toast.makeText(context,"Logged in by : "+user.getEmail(),Toast.LENGTH_LONG).show();
                                        signUpIntefaceListener.onSignUp();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,"Registration Failed: "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                }//end of if()


              mainActivity.clearFields(nameET,emailET,phoneET,passwordET);

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context=context;
        signUpIntefaceListener= (SignUpIntefaceListener) context;
        mainActivity= (MainActivity) context;
    }

    public interface SignUpIntefaceListener
    {
        void onSignUp();
    }
}
