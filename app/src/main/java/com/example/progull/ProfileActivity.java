package com.example.progull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView profileImageView;
    EditText inputUsername;
    Button btnUpdate;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImageView=findViewById(R.id.circleImageView);
        inputUsername=findViewById(R.id.inputUsername);
        btnUpdate=findViewById(R.id.btnUpdate);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mUserRef= FirebaseDatabase.getInstance().getReference().child("Users");

        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String profileImageUrl = snapshot.child("profileImg").getValue().toString();
                    String username = snapshot.child("username").getValue().toString();

                    Picasso.get().load(profileImageUrl).into(profileImageView);
                    inputUsername.setText(username);
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Data not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ProfileActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
