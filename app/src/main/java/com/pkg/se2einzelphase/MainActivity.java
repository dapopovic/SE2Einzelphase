package com.pkg.se2einzelphase;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.pkg.se2einzelphase.databinding.ActivityMainBinding;
import com.pkg.se2einzelphase.misc.socketThread;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
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
        EditText editText = findViewById(R.id.editTextMatrNr);
        String matrNr = editText.getText().toString();
        if (checkIfMatrNrInputIsValid(matrNr)) {
            return;
        }
        try {
            new socketThread(matrNr, "se2-submission.aau.at", 20080, findViewById(R.id.lblResult)).start();
        } catch (IllegalArgumentException e) {
            Snackbar.make(view, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_LONG).show();
        }
    }

    public void onSort(View view) {
        EditText editText = findViewById(R.id.editTextMatrNr);
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
        TextView textView = findViewById(R.id.lblResult);
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
        EditText editText = findViewById(R.id.editTextMatrNr);
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