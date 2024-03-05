package com.pkg.se2einzelphase;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.pkg.se2einzelphase.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

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
        if (checkIfMatrNrInputIsValid(matrNr)) {
            return;
        }

        new Thread(() -> {
            try (Socket socket = new Socket("se2-submission.aau.at", 20080)) {
                socket.getOutputStream().write(matrNr.getBytes());
                byte[] buffer = new byte[1024];
                int read = socket.getInputStream().read(buffer);
                String result = new String(buffer, 0, read);
                runOnUiThread(() -> ((TextView) findViewById(R.id.txtResult)).setText(result));
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Snackbar.make(view, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                });
            }
        }).start();
    }

    public void onSort(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextNumberSigned);
        String matrNr = editText.getText().toString();
        if (checkIfMatrNrInputIsValid(matrNr)) {
            return;
        }
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < matrNr.length(); i++) {
            int number = Integer.parseInt(String.valueOf(matrNr.charAt(i)));
            if (!isPrimeNumber(number)) {
                numbers.add(number);
            }
        }
        numbers.sort((a, b) -> b - a);
        StringBuilder result = new StringBuilder();
        for (int number : numbers) {
            result.append(number);
        }
        TextView textView = (TextView) findViewById(R.id.txtResult);
        textView.setText(result.toString());
    }

    private boolean isPrimeNumber(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfMatrNrInputIsValid(String matrNr) {
        EditText editText = (EditText) findViewById(R.id.editTextNumberSigned);
        if (matrNr.isEmpty()) {
            editText.setError("Please enter a valid matriculation number");
            return true;
        }
        if (matrNr.length() != 8) {
            editText.setError("Matriculation number must be 8 digits long");
            return true;
        }
        return false;
    }

}