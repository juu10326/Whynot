package com.example.whynot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UniversityActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UniversityAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context mContext;
    private Button SButton,RButton;
    private EditText editSUv,editRUv;
    private String userId;

    FirebaseDatabase Database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = Database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler);
        SButton=(Button)findViewById(R.id.Sbutton);
        RButton=(Button)findViewById(R.id.Rbutton);
        editSUv=(EditText)findViewById(R.id.editSUv);
        editRUv=(EditText)findViewById(R.id.editRUv);
        mContext=this;
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new GridLayoutManager(mContext,2);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        final List<Universityitem> uv = new ArrayList<>();
        Intent intent = getIntent();
        userId = "www";//User user=(User)intent.getSerializableExtra("userInfo");
        mDatabaseReference.child("Universities").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Universityitem Uvitem=postSnapshot.getValue(Universityitem.class);
                    uv.add(Uvitem);
                }
                uv.sort(new Comparator<Universityitem>(){
                    @Override
                    public int compare(Universityitem o1, Universityitem o2) {
                        int followers1 = o1.getFollowers();
                        int followers2 = o2.getFollowers();

                        if(followers1 == followers2) return 0;
                        else if(followers1<followers2) return 1;
                        else return -1;
                    }
                });
                mAdapter = new UniversityAdapter(uv,mContext,userId);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SearchUv=editSUv.getText().toString();
                CharSequence cs=SearchUv;
                mAdapter.getFilter().filter(cs);

            }
        });

        RButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String RequestedUv=editRUv.getText().toString();
                mDatabaseReference.child("Universities").child(RequestedUv).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final University uv = dataSnapshot.getValue(University.class);
                        if(uv==null){
                            mDatabaseReference.child("Requests").push().setValue(RequestedUv);
                            Toast.makeText(UniversityActivity.this, "요청되었습니다", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(UniversityActivity.this, "이미 들어있습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
