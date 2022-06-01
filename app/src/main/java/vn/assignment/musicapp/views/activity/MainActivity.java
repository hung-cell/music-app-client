package vn.assignment.musicapp.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.assignment.musicapp.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // go to home page
        goToHomePage();


    }
    // This method is to go to home page
    private void goToHomePage() {
         FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();

                    finish();
                }
                finally {
                    Intent i;
                    if(user != null){
                        i = new Intent(MainActivity.this, HomePageActivity.class);
                    }else{
                        i = new Intent(MainActivity.this, RegisterActivity.class);
                    }
                    startActivity(i);

                    finish();
                }
            }
        };
        thread.start();

    }
}