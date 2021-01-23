package com.example.mandiri.Activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandiri.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAct extends AppCompatActivity {

    TextView signIn,toReg,forgetPass;
    EditText emailLogin, passLogin;

    ActionBar actionBar;

    FirebaseAuth firebaseAuth;
    ProgressDialog pd;

    String mUID;
    private FirebaseAuth mAuth;

    FirebaseUser user;

    String email,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signIn = findViewById(R.id.signIn);
        toReg = findViewById(R.id.toReg);
        forgetPass = findViewById(R.id.forgetPass);
        emailLogin = findViewById(R.id.emailLogin);
        passLogin = findViewById(R.id.passLogin);

        mAuth = FirebaseAuth.getInstance();

        toReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = new Intent(LoginAct.this , RegisterAct.class);
                startActivity(signIn);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString();
                String password = passLogin.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //invalid email patterns set error
                    emailLogin.setError("Invalid Email");
                    emailLogin.setFocusable(true);
                }
                else{
                    //valid email pattern
                    loginUser(email,password);
                }
            }
        });

        //forget password
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });
    }



    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reecover Password");


        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);

        //views to set in dialog
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email Address");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        /* sets the min width of a edit view to fit a text of n 'M' letters regardless of
        the actual text extension and text size
         */
        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //button recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input data
                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });
        //button cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // show dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {
        final ProgressDialog pd = new ProgressDialog(LoginAct.this);
        pd.setMessage("Sending email ....");
        pd.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(LoginAct.this, "Email send...", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginAct.this, "Failed..", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                // get and show proper error message
                Toast.makeText(LoginAct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        final ProgressDialog pd = new ProgressDialog(LoginAct.this);
        pd.setMessage("Logging in ....");
        pd.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                          pd.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginAct.this, "Logging in...", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginAct.this,DashboardAct.class));
                            finish();
                        } else {
                           pd.dismiss();
                            Toast.makeText(LoginAct.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // error, get and error show message
                pd.dismiss();
                Toast.makeText(LoginAct.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go previous activty
        return super.onSupportNavigateUp();
    }
}
