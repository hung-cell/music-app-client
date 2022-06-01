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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.assignment.musicapp.R;
import vn.assignment.musicapp.model.User;
import vn.assignment.musicapp.retrofit.ApiClient;
import vn.assignment.musicapp.retrofit.MusicAppService;


public class RegisterActivity extends AppCompatActivity {
    EditText edEmail, edPassword, edUserName;
    String email,password;
    Button btnRegister;
    TextView tvGoToLoginPage;
    ProgressDialog loadingBar;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initFirebaseAuth();
        onClickBtnRegister();
        onCLickGoToLogin();

    }

    private void onCLickGoToLogin() {
        tvGoToLoginPage.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(i);
        });
    }

    //This method for handle when user click btn register
    private void onClickBtnRegister() {
        btnRegister.setOnClickListener(view -> {
            email = edEmail.getText().toString();
            password = edPassword.getText().toString();

            createNewAccount(email, password);
        });
    }

    // This method for init firebase database
    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    // This method for connect to component in layout
    private void initView() {
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLoginPage = findViewById(R.id.tvGoToLogin);

        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Đang tạo tài khoản ...");
        loadingBar.setMessage("Vui lòng đợi trong vài giây");
        loadingBar.setCanceledOnTouchOutside(false);
    }

    // This method for encrypt password to save to database

    private void createNewAccount(String email, String password) {
        // validate input
        boolean validateInput = validation(email,password);


        //send request create user to firebase database


        if(validateInput){
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loadingBar.dismiss();



                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                MusicAppService apiService = ApiClient.getRetrofitInstance().create(MusicAppService.class);
                                User userPost = new User(edEmail.getText().toString(),user.getUid());
                                Call<User> call = apiService.createUser(userPost);

                                call.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        Intent i = new Intent(RegisterActivity.this, HomePageActivity.class);
                                        startActivity(i);
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    }
                                });


                            } else {

                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }

    // this method for validate input when user click register
    private boolean validation(String email, String password) {


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vui lòng điền email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Vui lòng điền mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}