package andrewnguy.com.freefoodfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

public class LogInActivity extends AppCompatActivity implements FacebookCallback{

    LoginButton loginBtn;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_log_in);

        loginBtn = (LoginButton) findViewById(R.id.login_button);
        loginBtn.setReadPermissions("user_friends");
        callbackManager = CallbackManager.Factory.create();
        loginBtn.registerCallback(callbackManager, this);
    }

    @Override
    public void onSuccess(Object o) {

        startActivity(new Intent(this, MainActivity.class)); 
    }

    @Override
    public void onCancel() {
        Toast.makeText(LogInActivity.this, "To use this app please log in", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(FacebookException error) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode, data);
    }



}
