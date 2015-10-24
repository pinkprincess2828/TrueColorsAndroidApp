package com.example.pinkprincess.meetmetest2;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by mahandru on 18.10.2015.
 */
public class RegisterActivity extends Activity {

    Button registerbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerbtn=(Button)findViewById(R.id.bRegister);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Register Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_call:
                Intent dialer= new Intent(Intent.ACTION_DIAL);
                startActivity(dialer);
                return true;

            case R.id.action_homepic:
                startActivity(new Intent(RegisterActivity.this,MapsActivity.class));
                return true;
            case R.id.action_register:
                startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                return true;
            case R.id.action_homep:
                startActivity(new Intent(RegisterActivity.this, MapsActivity.class));
                return true;

            case R.id.action_login:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                return true;

            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_profile:
                startActivity(new Intent(RegisterActivity.this, PersonalStatistics.class));
                return true;

            case R.id.action_score:
                startActivity(new Intent(RegisterActivity.this, PersonalScore.class));
                return true;

            case R.id.action_ranking:
                startActivity(new Intent(RegisterActivity.this, Ranking.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
