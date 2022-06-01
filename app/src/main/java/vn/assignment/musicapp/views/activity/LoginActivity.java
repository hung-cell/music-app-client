package vn.assignment.musicapp.views.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.assignment.musicapp.R;


public class LoginActivity extends AppCompatActivity {
    EditText edPassword, edEmailLogin;
    String password, email;
    Button btnLogin;
    TextView tvGoToRegisterPage;
    ProgressDialog loadingBar;
    DatabaseReference databaseReference;
    TextView textViewGoToRegister;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initFirebaseAuth();
        onClickBtnLogin();
        textViewGoToRegister.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(i);
        });
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void initView() {

        edPassword = findViewById(R.id.edPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegisterPage = findViewById(R.id.tvGoToLogin);
        edEmailLogin = findViewById(R.id.edEmailLogin);
        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Đang đăng nhập ...");
        loadingBar.setMessage("Vui lòng đợi trong vài giây");
        loadingBar.setCanceledOnTouchOutside(false);
        textViewGoToRegister = findViewById(R.id.tvGoToRegister);
    }

    private void onClickBtnLogin() {
        btnLogin.setOnClickListener(view -> {
            password = edPassword.getText().toString();
            email = edEmailLogin.getText().toString();
            login(email,password);
        });
    }

    private void login(String email, String password) {
        boolean validateInput = validation(email,password);

        if(validateInput){
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(LoginActivity.this, HomePageActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean validation(String email, String password) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vui lòng điền tài khoản", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Vui lòng điền mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}