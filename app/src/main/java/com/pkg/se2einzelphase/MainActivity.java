package com.pkg.se2einzelphase;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.pkg.se2einzelphase.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
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

    public void onSend(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextNumberSigned);
        String matrNr = editText.getText().toString();

        new Thread(() -> {
            try (Socket socket = new Socket("se2-submission.aau.at", 20080)) {
                socket.getOutputStream().write(matrNr.getBytes());
                byte[] buffer = new byte[1024];
                int read = socket.getInputStream().read(buffer);
                String result = new String(buffer, 0, read);
                runOnUiThread(() -> {
                    Snackbar.make(view, result, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                });
            } catch (Exception e) {
                String msg = e.getMessage();
                if (msg == null) {
                    msg = "Unknown error";
                }
                String finalMsg = msg;
                runOnUiThread(() -> {
                    Snackbar.make(view, finalMsg, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                });
            }
        }).start();


    }
}