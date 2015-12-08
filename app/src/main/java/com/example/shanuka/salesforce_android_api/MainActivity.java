package com.example.shanuka.salesforce_android_api;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shanuka.salesforce_android_api.clients.AuthenticationRequest;
import com.example.shanuka.salesforce_android_api.dto.AuthenticationDto;
import com.example.shanuka.salesforce_android_api.model.Authentication;
import com.google.inject.Inject;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    public static final String EXTRA_AUTHNTICATION = "authntication";
    @InjectView(R.id.editTextUserName)
    private EditText editTextUserName;

    @InjectView(R.id.editTextPassword)
    private EditText editTextPassword;

    @InjectView(R.id.buttonLogin)
    private Button buttonLogin;

    @Inject
    AuthenticationDto mAuthenticationDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthenticationDto.setPassword(editTextPassword.getText().toString());
                mAuthenticationDto.setUsername(editTextUserName.getText().toString());
                performRequest(mAuthenticationDto);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void performRequest(AuthenticationDto mAuthenticationDto) {
        // showProgressFragment(R.id.progress);
        AuthenticationRequest request = new AuthenticationRequest(
                MainActivity.this, "AuthenticationRequest", mAuthenticationDto);

        String lastRequestCacheKey = request.createCacheKey();
        spiceManager.execute(request, lastRequestCacheKey,
                DurationInMillis.ALWAYS_EXPIRED, new UserRequestListener());
    }

    class UserRequestListener implements RequestListener<Authentication> {
        @Override
        public void onRequestFailure(final SpiceException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
                    .show();

            //removeProgressFragment();


        }

        @Override
        public void onRequestSuccess(final Authentication mAuthentication) {

            //removeProgressFragment();
            Ln.d("access_token", "access_token "+mAuthentication.getAccessToken());

//            Toast.makeText(MainActivity.this, mAuthentication.getAuthentication(), Toast.LENGTH_SHORT)
//                    .show();


            startMainActivity(mAuthentication);

//            mIAppreference.putToken(mAuthentication.getAccessToken());
//
//            isAuthenticated= true;
//
//            if(isAuthenticated) {
//
//                startMainActivity();
//            }

        }
    }

    private void startMainActivity(Authentication mAuthentication) {
        Intent intent = new Intent(MainActivity.this, DocumentUploadActivity.class);
        intent.putExtra(EXTRA_AUTHNTICATION,mAuthentication);
        startActivity(intent);


    }
}
