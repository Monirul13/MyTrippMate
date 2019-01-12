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
public class LoginFragment extends Fragment {

    private Button loginButton,registerButton;
    private EditText emailET,passwordET;
    private LoginRegisterInterface loginRegisterInterface;
    private Context context;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private MainActivity mainActivity;

    public LoginFragment() {
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
        View view=inflater.inflate(R.layout.fragment_login, container, false);

        emailET=view.findViewById(R.id.frag_email_ET_id);
        passwordET=view.findViewById(R.id.frag_password_ET_id);
        loginButton=view.findViewById(R.id.frag_login_btn_id);
        registerButton=view.findViewById(R.id.frag_register_btn_id);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=emailET.getText().toString();
                String password=passwordET.getText().toString();

                boolean isValid=mainActivity.validate(emailET,passwordET);

                if(isValid)
                {
                    auth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        user=auth.getCurrentUser();
                                        Toast.makeText(context,"Logged in by : "+user.getEmail(),Toast.LENGTH_LONG).show();
                                        loginRegisterInterface.onLoginButtonClicked();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,"Logged in Failed: "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }


                mainActivity.clearFields(emailET,passwordET);

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loginRegisterInterface.onRegisterButtonClicked();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        loginRegisterInterface= (LoginRegisterInterface) context;
        mainActivity= (MainActivity) context;
    }

    public interface LoginRegisterInterface
    {
        void onLoginButtonClicked();
        void onRegisterButtonClicked();
    }

}
