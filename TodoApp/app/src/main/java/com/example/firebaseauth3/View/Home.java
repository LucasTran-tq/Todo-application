package com.example.firebaseauth3.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;

public class Home extends AppCompatActivity {

    private Button button_addTodo;
    private BottomNavigationView bottomNavigationView;


    RecyclerView recyclerView;
    RecyclerViewAdapter myAdapter;
    ArrayList<TodoNote> mData;

    DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    String uID_User;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        button_addTodo = findViewById(R.id.button_addTodo);


        uID_User = firebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference(uID_User).child("TodoNote");



        changeActivityBottomNavigation();

        getDataFireBase_In_RecyclerView();

        button_addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayInputDialog();
            }
        });



    }

    private void displayInputDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setTitle("Save To Firebase");
        dialog.setContentView(R.layout.input_dialog);



        TextView tvShowDate = dialog.findViewById(R.id.tv_showdate);
        TextView tvTitle = (EditText) dialog.findViewById(R.id.textView_title_HomeActivity);

        Button saveBtn = (Button) dialog.findViewById(R.id.btn_Save_HomeActivity);


        DatePickerDialog.OnDateSetListener setListener;
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                String date = day + "/" + month + "/" + year;
                tvShowDate.setText(date);
            }
        };

        tvShowDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(dialog.getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth, setListener, year, month, day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });





        //SAVE
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //GET DATA
                String title = tvTitle.getText().toString();
                String date = tvShowDate.getText().toString();

                //SET DATA
                TodoNote todoNote = new TodoNote();
                todoNote.setTitle(title);
                todoNote.setDate(date);
                todoNote.setStatus(0);


                String key = "note" + String.valueOf(mData.size()+1);

                //SIMPLE VALIDATION
                if (title != null && title.length() > 0) {

                    //database.push().setValue(todoNote);

                    databaseReference.child(key).setValue(todoNote);

                    getDataFireBase(databaseReference);


                } else {
                    Toast.makeText(Home.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }

                dialog.cancel();
            }

        });

        dialog.show();

    }

    public void getDataFireBase_In_RecyclerView(){

        recyclerView = findViewById(R.id.recyclerView_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mData = new ArrayList<>();



//        mData.add(new TodoNote("One", "1/1"));
//        mData.add(new TodoNote("One", "1/1"));
//        mData.add(new TodoNote("One", "1/1"));
//        mData.add(new TodoNote("One", "1/1"));
//        mData.add(new TodoNote("One", "1/1"));


        //databaseReference = FirebaseDatabase.getInstance().getReference("TodoNote");

        //databaseReference = FirebaseDatabase.getInstance().getReference("TodoNote");



        myAdapter = new RecyclerViewAdapter(this, mData);
        recyclerView.setAdapter(myAdapter);


        getDataFireBase(databaseReference);

    }


    public void getDataFireBase(DatabaseReference databaseReference){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mData.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    TodoNote todoNote = dataSnapshot.getValue(TodoNote.class);
                    mData.add(todoNote);

                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void changeActivityBottomNavigation(){
        // BOTTOM NAVIGATION
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.Home:
                        return true;

                    case R.id.Statictis:
                        startActivity(new Intent(getApplicationContext(), Statistics.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });
    }
}