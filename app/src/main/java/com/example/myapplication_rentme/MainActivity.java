package com.example.myapplication_rentme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import android.content.Intent;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etText;
    Button btAdd;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.bt_back);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         Intent intent= new Intent(MainActivity.this,home_page.class);
                         startActivity(intent);
                    }
                });

        etText = findViewById(R.id.et_text);
        btAdd = findViewById(R.id.bt_add);
        listView = findViewById(R.id.list_view);

        adapter = new ArrayAdapter<>(this
        , android.R.layout.simple_list_item_1, arrayList);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Data");


        getValue();
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sValue = etText.getText().toString().trim();

                String sKey = databaseReference.push().getKey();

                if (sKey != null){
                    databaseReference.child(sKey).child("value").setValue(sValue);
                    etText.setText("");
                }

            }
        });


    }

    private void getValue() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String sValue = dataSnapshot.child("value").getValue(String.class);

                    arrayList.add(sValue);
                }
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext()
                , error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}