package com.jrpohlman.chatmeup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Spinner spinner = (Spinner) findViewById(R.id.chatroon_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.chatroon_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button btn = findViewById(R.id.user_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                EditText username = findViewById(R.id.userIn_text);

                bundle.putString("user", username.getText().toString());
                bundle.putString("room", spinner.getSelectedItem().toString());
                i.putExtras(bundle);

                startActivity(i);
            }
        });
    }
}
