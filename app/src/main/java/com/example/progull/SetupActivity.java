package com.example.progull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    CircleImageView profile_image;
    EditText inputUsername,inputPhone,inputClass;
    Button btnSave;
    Uri imageUri;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    StorageReference storageRef;

    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        profile_image = findViewById(R.id.profile_image);
        inputUsername = findViewById(R.id.inputUsername);
        inputPhone = findViewById(R.id.inputPhone);
        inputClass = findViewById(R.id.inputClass);
        btnSave = findViewById(R.id.btnSave);
        mLoadingBar = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        storageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  saveData();
            }
        });


    }

    private void saveData() {

        final String username = inputUsername.getText().toString();
        final String clas = inputClass.getText().toString();
        final String phonenum = inputPhone.getText().toString();

        if(username.isEmpty() || username.length()<2) {
            showError(inputUsername,"Username is not valid");
        }
        else if(clas.isEmpty()){
            showError(inputClass,"Username is empty");
        }
        else if(phonenum.isEmpty()){
            showError(inputPhone,"Phone Number is empty");
        }
        else if(imageUri==null){
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
        else{

            mLoadingBar.setTitle("adding Setup Profile");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            storageRef.child(mUser.getUid()).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){

                        storageRef.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                               HashMap hashMap = new HashMap();
                               hashMap.put("username",username);
                               hashMap.put("clas",clas);
                                hashMap.put("phonenum",phonenum);
                                hashMap.put("profileImg",uri.toString());

                                mRef.child(mUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Intent intent = new Intent(SetupActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        mLoadingBar.dismiss();
                                        Toast.makeText(SetupActivity.this, "Setup Profile Completed", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mLoadingBar.dismiss();
                                        Toast.makeText(SetupActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }

                }
            });
        }


    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!= null) {

            imageUri = data.getData();
            profile_image.setImageURI(imageUri);

        }
    }

}
