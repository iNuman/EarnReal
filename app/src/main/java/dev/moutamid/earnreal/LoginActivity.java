package dev.moutamid.earnreal;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;

//import Utils.MyUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameEditText, passwordEditText;
    private ImageView showPasswordBtn;
    private TextView forgotPasswordBtn, goToSignUpActivityBtn;
    private Button loginBtn;
    private Boolean passwordShowing = false;
    private Dialog mDialog;
    private MyUtils utils = new MyUtils();
    private RelativeLayout progressLayout;

    private ProgressDialog mProgressDialog;

    private static final String PROFILE_IMAGE = "profile_image";
    private static final String USERS = "users";
    private static final String USER_NAME = "userName";
    private static final String PUBLIC = "Public";
    private static final String LOG_STATUS = "logStatus";
    private static final String QUESTIONS_STATUS = "Questions Status";
    private static final String DOMAIN = "@strangers.com";
    private static final String PACKAGE_NAME = "dev.moutamid.strangers";

  //  private DatabaseReference mDatabaseUsers;
    private Boolean isOnline = false;
    private SharedPreferences sharedPreferences;
    private EditText userNameEditText_RSD;

    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

      //  new OnlineStatus().execute();

//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child(USERS);
//        mDatabaseUsers.keepSynced(true);
//
//        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = this.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Signing you in...");

        // Initializing Views
        initViews();

        setListenersToWidgets();

    }

    private void setListenersToWidgets() {
        passwordEditText.addTextChangedListener(passwordEditTextWatcher());

        showPasswordBtn.setOnClickListener(showPasswordBtnListener());

        forgotPasswordBtn.setOnClickListener(forgotPasswordBtnListener());

        loginBtn.setOnClickListener(loginBtnListener());

        goToSignUpActivityBtn.setOnClickListener(signUpBtnListener());
    }

    private TextWatcher passwordEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                showPasswordBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private View.OnClickListener signUpBtnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        };
    }

    private View.OnClickListener showPasswordBtnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordShowing) {

                    // Hiding password
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPasswordBtn.setImageDrawable(getResources().getDrawable(R.drawable.icon_show_password_off));
                    passwordShowing = false;

                } else {

                    // Showing password
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPasswordBtn.setImageDrawable(getResources().getDrawable(R.drawable.icon_show_password_on));
                    passwordShowing = true;

                }
            }
        };
    }

    private View.OnClickListener loginBtnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userNameEditText.getText().toString().trim().toLowerCase();
                String password = passwordEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && isOnline) {

                    signInUserWithNameAndPassword(username, password);

                } else if (TextUtils.isEmpty(username)) {

                    userNameEditText.setError("Please enter username");
                    userNameEditText.requestFocus();

                } else if (TextUtils.isEmpty(password)) {

                    passwordEditText.setError("Please enter password");
                    passwordEditText.requestFocus();

                } else if (!isOnline) {

                    utils.showOfflineDialog(LoginActivity.this);
                }
            }
        };
    }

    private View.OnClickListener forgotPasswordBtnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressLayout.setVisibility(View.VISIBLE);

                TextView closeBtn_RSD, forgotUsernameBtn_RSD;
                Button submitBtn_RSD;

                mDialog.setContentView(R.layout.dialog_reset_password);

                closeBtn_RSD = mDialog.findViewById(R.id.close_btn_reset_password_dialog);
                userNameEditText_RSD = mDialog.findViewById(R.id.userName_editText_reset_password_dialog);
                submitBtn_RSD = mDialog.findViewById(R.id.submit_btn_reset_password_dialog);
                forgotUsernameBtn_RSD = mDialog.findViewById(R.id.forgot_userName_Textview_reset_password_dialog);

                closeBtn_RSD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                });

                submitBtn_RSD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (isOnline) {

                            String userName = userNameEditText_RSD.getText().toString().trim().toLowerCase();

                            if (TextUtils.isEmpty(userName)) {

                                userNameEditText_RSD.setError("Please enter a username.");
                                userNameEditText_RSD.requestFocus();

                            } else {

                                checkUserExistForReset(userName);

                            }
                        } else {
                            utils.showOfflineDialog(LoginActivity.this);
                        }

                    }
                });

                forgotUsernameBtn_RSD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        startActivity(new Intent(LoginActivity.this, ForgotUsernameActivity.class));
                    }
                });

                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                progressLayout.setVisibility(View.GONE);

                mDialog.show();
            }
        };
    }

    private void checkUserExist(final String username, final String userPassword) {

//        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot.hasChild(username)) {
//
                    checkPassword(username);
//
//                } else {
//
//                    mProgressDialog.dismiss();
//                    userNameEditText.setError("Username not found");
//                    userNameEditText.requestFocus();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    private void checkUserExistForReset(final String username) {

        mProgressDialog.show();

//        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot.hasChild(username)) {
//
//                    Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
//                    intent.putExtra(USER_NAME, username);
//
//                    mProgressDialog.dismiss();
//                    mDialog.dismiss();
//
//                    startActivity(intent);
//                    finish();
//
//                } else {
//
//                    mProgressDialog.dismiss();
//                    userNameEditText_RSD.setError("Username not found");
//                    userNameEditText_RSD.requestFocus();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    private void checkPassword(final String username) {

//        mDatabaseUsers.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                String profileImage = dataSnapshot.child(PUBLIC).child(PROFILE_IMAGE).getValue(String.class);
//
//                utils.storeString(LoginActivity.this, USER_NAME, username);
//                utils.storeString(LoginActivity.this, LOG_STATUS, "true");
//                utils.storeString(LoginActivity.this, QUESTIONS_STATUS, "no");
//                utils.storeString(LoginActivity.this, PROFILE_IMAGE, profileImage);
//
//                Intent intent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                mProgressDialog.dismiss();
//
//                Toast.makeText(LoginActivity.this, "You are successfully logged in", Toast.LENGTH_SHORT).show();
//
//                startActivity(intent);
//                finish();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    private void signInUserWithNameAndPassword(final String username, final String password) {

        mProgressDialog.show();

        if (isOnline) {

            String email = username + DOMAIN;

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                //if Email Address is Invalid..

                mProgressDialog.dismiss();
                userNameEditText.setError("Username is not valid. Make sure no spaces and special characters are included");
                userNameEditText.requestFocus();
            } else {

//                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        if (task.isSuccessful()) {
//
//                            checkUserExist(username, password);
//
//                        } else {
//
//                            mProgressDialog.dismiss();
//                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });

            }
        } else {

            mProgressDialog.dismiss();
            Toast.makeText(this, "You are not online", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        userNameEditText = findViewById(R.id.userName_login_editText);
        passwordEditText = findViewById(R.id.password_login_editText);
        showPasswordBtn = findViewById(R.id.showPassword_btn_login);
        forgotPasswordBtn = findViewById(R.id.forgotPassword_login_textView);
        goToSignUpActivityBtn = findViewById(R.id.goTo_signUp_activity_textView);
        loginBtn = findViewById(R.id.login_btn);
        progressLayout = findViewById(R.id.progressBar_layout_login);
        // For Dialog
        mDialog = new Dialog(this);
    }

    private class OnlineStatus extends AsyncTask<Boolean, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... booleans) {

            if (!isCancelled()) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

//                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
//
//                    try {
//
//                        Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
//                        int returnVal = p1.waitFor();
//                        boolean reachable = (returnVal == 0);
//
//                        return reachable;
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return false;
//                    }
//
//                } else return false;
          }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (!isCancelled()) {
                isOnline = aBoolean;

                new OnlineStatus().execute();
            }

        }


    }
}
