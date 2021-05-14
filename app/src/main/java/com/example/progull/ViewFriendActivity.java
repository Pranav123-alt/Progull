package com.example.progull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFriendActivity extends AppCompatActivity {


    DatabaseReference mUserRef,requestRef,friendRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String prfileImageUrl,username;
    CircleImageView profileImage;
    TextView Username;
    Button btnPerform,btnDecline;
    String CurrentState = "nothing_happen";
     String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend);

          userID = getIntent().getStringExtra("userKey");
        Toast.makeText(this, ""+userID, Toast.LENGTH_SHORT).show();
        btnPerform = findViewById(R.id.btnPerform);
        btnDecline = findViewById(R.id.btnDecline);


        mUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        requestRef= FirebaseDatabase.getInstance().getReference().child("Requests");
        friendRef= FirebaseDatabase.getInstance().getReference().child("Friends");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        profileImage = findViewById(R.id.profileImage);
        Username = findViewById(R.id.username);

        LoadUser();

        btnPerform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformActivity(userID);
            }
        });
        checkUserExistence(userID);
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Unfriend(userID);
            }
        });

    }

    private void Unfriend(final String userID) {
        if(CurrentState.equals("friend")){
            friendRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        friendRef.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ViewFriendActivity.this, "You are unfriend", Toast.LENGTH_SHORT).show();
                                    CurrentState = "nothing_happen";
                                    btnPerform.setText("Send Friend Request");
                                    btnDecline.setVisibility(View.GONE);
                                }

                            }
                        });
                    }

                }
            });
        }
        if(CurrentState.equals("he_sent_pending")){
            HashMap hashMap= new HashMap();
            hashMap.put("status","decline");
            requestRef.child(userID).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {

                        CurrentState="he_sent_decline";
                        Toast.makeText(ViewFriendActivity.this, "You have decline Friend", Toast.LENGTH_SHORT).show();
                        btnPerform.setVisibility(View.GONE);
                        btnDecline.setVisibility(View.GONE);
                    }

                }
            });
        }
    }

    private void checkUserExistence(String userID) {
        friendRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CurrentState="friend";
                    btnPerform.setText("Send sms");
                    btnDecline.setText("unfriend");
                    btnDecline.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        friendRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    CurrentState="friend";
                    btnPerform.setText("Send sms");
                    btnDecline.setText("unfriend");
                    btnDecline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(mUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        btnDecline.setVisibility(View.GONE);
                        CurrentState="I_sent_pending";
                        btnPerform.setText("Cancel Friend Request");
                    }
                    if(snapshot.child("status").getValue().toString().equals("decline")){
                        btnDecline.setVisibility(View.GONE);
                        CurrentState="I_sent_pending";
                        btnPerform.setText("Cancel Friend Request");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(userID).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState= "he_sent_pending";
                        btnPerform.setText("Accept Friend Request");
                        btnDecline.setText("Decline Friend Request");
                        btnDecline.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(CurrentState.equals("nothing_happen")){
            CurrentState= "nothing_happen";
            btnPerform.setText("Send Friend Request");
            btnDecline.setVisibility(View.GONE);
        }


    }

    private void PerformActivity(final String userID) {
           if(CurrentState.equals("nothing_happen")){

               HashMap hashMap = new HashMap();
                hashMap.put("status","pending");
               requestRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                   @Override
                   public void onComplete(@NonNull Task task) {
                       if(task.isSuccessful()){
                           Toast.makeText(ViewFriendActivity.this, "You have sent friend request", Toast.LENGTH_SHORT).show();
                           btnDecline.setVisibility(View.GONE);
                           CurrentState="I_sent_pending";
                           btnPerform.setText("Cancel Friend Request");
                       }
                       else{
                           Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                       }

                   }
               });
           }
           if(CurrentState.equals("I_sent_pending") || CurrentState.equals("I_sent_decline")){

               requestRef.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(ViewFriendActivity.this, "You have cancelled Friend Request", Toast.LENGTH_SHORT).show();
                           CurrentState="nothing_happen";
                           btnPerform.setText("Send Friend Request");
                           btnDecline.setVisibility(View.GONE);
                       }
                       else{
                           Toast.makeText(ViewFriendActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                       }

                   }
               });
           }
           if(CurrentState.equals("he_sent_pending")){
               requestRef.child(userID).child(mUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                     if(task.isSuccessful()){

                         final HashMap hashMap = new HashMap();
                         hashMap.put("status","friend");
                         hashMap.put("username",username);
                         hashMap.put("profileImageUrl",prfileImageUrl);
                         friendRef.child(mUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                             @Override
                             public void onComplete(@NonNull Task task) {
                                 if(task.isSuccessful()){
                                     friendRef.child(userID).child(mUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                         @Override
                                         public void onComplete(@NonNull Task task) {

                                             Toast.makeText(ViewFriendActivity.this, "You added friend", Toast.LENGTH_SHORT).show();
                                             CurrentState="friend";
                                             btnPerform.setText("Send sms");
                                             btnDecline.setText("unfriend");
                                             btnDecline.setVisibility(View.VISIBLE);
                                         }
                                     });
                                 }

                             }
                         });
                     }
                   }
               });
           }
           if(CurrentState.equals("friend")){

               Intent intent = new Intent(ViewFriendActivity.this,ChatActivity.class);
               intent.putExtra("OtherUserID",userID);
               startActivity(intent);


               //send sms
           }

    }

    private void LoadUser() {

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   prfileImageUrl=snapshot.child("profileImg").getValue().toString();
                    username=snapshot.child("username").getValue().toString();

                    Picasso.get().load(prfileImageUrl).into(profileImage);
                    Username.setText(username);

                }
                else{
                    Toast.makeText(ViewFriendActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ViewFriendActivity.this, ""+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
