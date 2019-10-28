package com.gwazasoftwares.abhes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gwazasoftwares.abhes.adapters.EmergencyAdapter;
import com.gwazasoftwares.abhes.interfaces.OnPopupItemSelected;
import com.gwazasoftwares.abhes.models.Emergency;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {

    FloatingActionButton fab;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("emergencies");
    ProgressDialog progressDialog;

    //recyclerview objects
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //model object for our list data
    private List<Emergency> emergencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       fab = findViewById(R.id.fab);
       progressDialog = new ProgressDialog(HomePage.this);
       progressDialog.setMessage("loading data");
       progressDialog.setCanceledOnTouchOutside(false);
       progressDialog.show();

        //initializing views
        recyclerView = findViewById(R.id.emergencylist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       emergencyList = new ArrayList<>();


    }

    @Override
    protected void onStart() {
        super.onStart();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Report.class);
                startActivity(intent);
            }
        });
        loadRecyclerViewItem();
    }

    private void loadRecyclerViewItem() {
        //Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            emergencyList.clear();
                for (DataSnapshot emergencySnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Emergency lesson = emergencySnapshot.getValue(Emergency.class);
                    Emergency betterData = new Emergency(R.drawable.ic_person_black_24dp, lesson.getTitle(), lesson.getDescription());

                    emergencyList.add(betterData);
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Toast.makeText(getApplicationContext(),"could not  load data :" +databaseError.getMessage(), Toast.LENGTH_LONG).show();
                // ...  
              }
        });

        OnPopupItemSelected popupItemSelected = new OnPopupItemSelected() {
            @Override
            public void select(int selection) {

                if(selection == R.id.menu1) {
                    Intent intent = new Intent(getApplicationContext(), Details.class);
                    startActivity(intent);
                }else if(selection == R.id.menu2){
                    Intent intent = new Intent(getApplicationContext(), Reactions.class);
                    startActivity(intent);
                }
            }
        };

        adapter = new EmergencyAdapter(emergencyList, this, popupItemSelected);
        recyclerView.setAdapter(adapter);
    }
}
