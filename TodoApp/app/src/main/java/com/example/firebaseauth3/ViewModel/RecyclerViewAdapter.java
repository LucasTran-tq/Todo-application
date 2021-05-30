package com.example.firebaseauth3.ViewModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.firebaseauth3.Model.TodoNote;
import com.example.firebaseauth3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<TodoNote> mData;
    String uID_User;

    public RecyclerViewAdapter(Context mContext, ArrayList<TodoNote> mData) {
        this.mContext = mContext;
        this.mData = mData;

        uID_User = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_listview_todo, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.tvTitle.setText(mData.get(position).getTitle());
        holder.tvDate.setText(mData.get(position).getDate());


        if(mData.get(position).getStatus() == 0){
            holder.cb_status.setChecked(false);
        }
        if (mData.get(position).getStatus() == 1){
            holder.cb_status.setChecked(true);
        }

        String key = "note" + String.valueOf(position+1);

       Log.d("TESTNE", key);



        holder.itemView.setOnClickListener(new View.OnClickListener() {

            Map<String,Object> map=new HashMap<>();


            @Override
            public void onClick(View v) {

                if(mData.get(position).getStatus() == 0){
                    map.put("status", 1);
                }
                else if (mData.get(position).getStatus() == 1){
                    map.put("status", 0);
                }

                FirebaseDatabase.getInstance().getReference(uID_User).child("TodoNote").child(key).updateChildren(map);

                Toast.makeText(mContext, "Updated", Toast.LENGTH_SHORT).show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;
        private TextView tvDate;
        private CheckBox cb_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.txtView_title_item_RecyclerView);
            tvDate = itemView.findViewById(R.id.txtView_date_item_RecyclerView);
            cb_status = itemView.findViewById(R.id.CheckBox_Home);
        }
    }
}
