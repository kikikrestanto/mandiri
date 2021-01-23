package com.example.mandiri.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandiri.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterAct extends AppCompatActivity {

    RelativeLayout regRelative;
    EditText nameReg, emailReg, passwordReg;
    TextView toLogin,register;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameReg = findViewById(R.id.nameReg);
        emailReg = findViewById(R.id.emailReg);
        passwordReg = findViewById(R.id.passwordReg);
        regRelative = findViewById(R.id.regRelative);
        toLogin = findViewById(R.id.toLogin);
        register = findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance();

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(RegisterAct.this, LoginAct.class);
                startActivity(toLogin);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(RegisterAct.this);
                pd.setMessage("Please Wait ...");
                pd.show();

                String str_name = nameReg.getText().toString().trim();
                String str_email = emailReg.getText().toString().trim();
                String str_pass = passwordReg.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()){
                    emailReg.setError("Invalid Email");
                    emailReg.setFocusable(true);
                }
                else if (str_pass.length()<6){
                    passwordReg.setError("Password length at least 6 characters");
                    passwordReg.setFocusable(true);
                }
                else {
                    register(str_name,str_email, str_pass);
                }
            }
        });
    }

    /*private void register(final String username,final String email,final String password) {
        pd.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dismiss dialog and start register act
                            pd.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            //get user email and uid from auth
                            String uid = user.getUid();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            //put infon in hashmap
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name", username);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //path to stroe user data name "users"
                            DatabaseReference reference = database.getReference("Users");
                            //put data within hashmap in database
                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterAct.this, "Registered..\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterAct.this, DashboardAct.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            pd.dismiss();
                            Toast.makeText(RegisterAct.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error dismiss
                pd.dismiss();
                Toast.makeText(RegisterAct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    } */

    private void register(final String username,final String email,final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterAct.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String uid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("uid",uid);
                            hashMap.put("name", username);
                            hashMap.put("mail", email);
                            hashMap.put("phone", "");
                            hashMap.put("image", "");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        pd.dismiss();
                                        Intent intent = new Intent(RegisterAct.this,DashboardAct.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finishAffinity();
                                    }
                                }
                            });

                        }else {
                            pd.dismiss();
                            Toast.makeText(RegisterAct.this, "you can't register with name, email and password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go previous activty
        return super.onSupportNavigateUp();
    }
}
