package com.example.firebaseauth3.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.firebaseauth3.Model.TodoNote;
import com.example.firebaseauth3.R;
import com.example.firebaseauth3.ViewModel.RecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private RecyclerView recyclerView_Completed;
    private RecyclerView recyclerView_Todo;

    private DatabaseReference databaseReference;

    private String uID_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        changeActivityBottomNavigation();

        uID_User = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference(uID_User).child("TodoNote");

        getDataFireBase_In_RecyclerView_Completed();

        getDataFireBase_In_RecyclerView_Todo();

        gotoProfileActivity();
    }

    public void gotoProfileActivity(){

        Button button_gotoProfile = findViewById(R.id.button_gotoProfile);

        button_gotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Statistics.this, Profile.class));
                finish();
            }
        });
    }



    public void getDataFireBase_In_RecyclerView_Todo(){

        recyclerView_Todo = findViewById(R.id.recyclerView_todo_Statistic);
        recyclerView_Todo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ArrayList<TodoNote> mData_Todo = new ArrayList<>();
        RecyclerViewAdapter adapter_Todo = new RecyclerViewAdapter(this, mData_Todo);
        recyclerView_Todo.setAdapter(adapter_Todo);



        //databaseReference = FirebaseDatabase.getInstance().getReference("TodoNote");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mData_Todo.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    if(dataSnapshot.getValue(TodoNote.class).getStatus() == 0){
                        TodoNote todoNote = dataSnapshot.getValue(TodoNote.class);
                        mData_Todo.add(todoNote);
                    }

                }
                adapter_Todo.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getDataFireBase_In_RecyclerView_Completed(){

        recyclerView_Completed = findViewById(R.id.recyclerView_completed_Statistic);
        recyclerView_Completed.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ArrayList<TodoNote> mData_Completed = new ArrayList<>();
        RecyclerViewAdapter adapter_Completed = new RecyclerViewAdapter(this, mData_Completed);
        recyclerView_Completed.setAdapter(adapter_Completed);



        //databaseReference = FirebaseDatabase.getInstance().getReference("TodoNote");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mData_Completed.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    if(dataSnapshot.getValue(TodoNote.class).getStatus() == 1){
                        TodoNote todoNote = dataSnapshot.getValue(TodoNote.class);
                        mData_Completed.add(todoNote);
                    }

                }
                adapter_Completed.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void getDataFireBase(DatabaseReference databaseReference,RecyclerViewAdapter adapter, ArrayList<TodoNote> mData){

        //databaseReference = FirebaseDatabase.getInstance().getReference("TodoNote");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    TodoNote todoNote = dataSnapshot.getValue(TodoNote.class);
                    mData.add(todoNote);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void changeActivityBottomNavigation(){
        // BOTTOM NAVIGATION
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Statictis);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.Statictis:
                        return true;

                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });
    }
}