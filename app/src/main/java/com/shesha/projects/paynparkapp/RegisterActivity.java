package com.shesha.projects.paynparkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullNameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText reenterPasswordText;
    private Button registerButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullNameText = findViewById(R.id.registerFullNameEditText);
        emailText = findViewById(R.id.registerEmailEditText);
        passwordText = findViewById(R.id.registerPasswordEditText);
        reenterPasswordText = findViewById(R.id.registerReEnterPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = emailText.getText().toString();
                String passwordString = passwordText.getText().toString();
                String fullNameString = fullNameText.getText().toString();
                String reEnterPasswordString = reenterPasswordText.getText().toString();
                if (emailString.equals("") || passwordString.equals("") || fullNameString.equals("") || reEnterPasswordString.equals(""))
                {
                    Toast.makeText(RegisterActivity.this,"Please enter the required details.",Toast.LENGTH_SHORT).show();
                }
                else if (!passwordString.equals(reEnterPasswordString))
                {
                    Toast.makeText(RegisterActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("Name",fullNameString);
                    userMap.put("Email",emailString);
                    firebaseAuth.createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                final String userID = FirebaseAuth.getInstance().getUid();
                                firebaseFirestore.collection("Users")
                                        .document(userID)
                                        .set(userMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this,e.toString(),Toast.LENGTH_LONG).show();

                                }
                            });
                }

            }
        });
    }
}