package com.example.rana.mytrippmate;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.rana.mytrippmate.pojoclasses.Event;


public class MainActivity extends AppCompatActivity implements LoginFragment.LoginRegisterInterface
        ,RegistrationFragment.SignUpIntefaceListener,Event_List_Fragment.FabButtonActionListener {

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private DrawerLayout drawerLayout;
    private  NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        LoginFragment loginFragment=new LoginFragment();
        transaction.add(R.id.fragmentContainer_id,loginFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();


        drawerLayout =findViewById(R.id.drawerLayout_id);
        navigationView=findViewById(R.id.navigationView_id);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //item.setChecked(true);
                // close drawer when item is tapped
                drawerLayout.closeDrawers();

                switch (item.getItemId()){
                    case R.id.nav_map_id:
                        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                        break;
                    case R.id.nav_weatherReport_id:
                        startActivity(new Intent(getApplicationContext(), WeatherActivity.class));
                        break;
                    case  R.id.nav_nearby_id:
                        startActivity(new Intent(getApplicationContext(), NearByPlacesActivity.class));
                        break;
                }

                return true;
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);



    }



    //Log in and navigate to home page
    @Override
    public void onLoginButtonClicked() {

        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        Event_List_Fragment eventListFragment=new Event_List_Fragment();
        transaction.replace(R.id.fragmentContainer_id,eventListFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onRegisterButtonClicked() {

        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        RegistrationFragment registrationFragment=new RegistrationFragment();
        transaction.replace(R.id.fragmentContainer_id,registrationFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Sign Up and navigate to home page
    @Override
    public void onSignUp() {
        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        Event_List_Fragment eventListFragment=new Event_List_Fragment();
        transaction.replace(R.id.fragmentContainer_id,eventListFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
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

    @Override
    public void onFabButtonClick() {
        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        AddEventFragment addEventFragment=new AddEventFragment();
        transaction.replace(R.id.fragmentContainer_id,addEventFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLogoutButtonClick() {

        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        LoginFragment loginFragment=new LoginFragment();
        transaction.replace(R.id.fragmentContainer_id,loginFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onItemClick(Event event) {
        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        Bundle bundle=new Bundle();
        bundle.putSerializable("event",event);
        EventDetailsFragment eventDetailsFragment=new EventDetailsFragment();
        eventDetailsFragment.setArguments(bundle);
        transaction.replace(R.id.fragmentContainer_id,eventDetailsFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       Activity activity;
        switch (item.getItemId()){
            case R.id.nav_map_id:
                startActivity(new Intent(this, MapActivity.class));
                break;
            case R.id.nav_weatherReport_id:
                startActivity(new Intent(this, WeatherActivity.class));
                break;
            case  R.id.nav_nearby_id:
                startActivity(new Intent(this, NearByActivity.class));
                break;

            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

        }

        //Intent intent=new Intent(this,activity.getClass());

        return super.onOptionsItemSelected(item);
    }


}
