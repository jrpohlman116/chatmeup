package com.jrpohlman.chatmeup;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jrpohlman.chatmeup.data.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    private EditText userText;
    private EditText passwordText;
    private String alertMessage = "";
    private AlertDialog alertDialog;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Spinner spinner = (Spinner) findViewById(R.id.chatroon_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.chatroon_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button btn = findViewById(R.id.user_btn);
        userText = (EditText) findViewById(R.id.userIn_text);
        passwordText = (EditText) findViewById(R.id.passwordIn_text);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = userText.getText().toString();
                password = passwordText.getText().toString();

                try{
                    Class.forName(AppConfig.DRIVER);
                    Connection conn = DriverManager.getConnection(AppConfig.conn, AppConfig.db_user, AppConfig.db_pass);

                    Statement statement = conn.createStatement();
                    String sql = "SELECT * from " + AppConfig.TABLE_NAME + " WHERE username = '" + username + "' and password = '" + password + "'";

                    final ResultSet result = statement.executeQuery(sql);
                    while(result.next()){
                        alertMessage += result.getString("username") + " Login Success\n";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Login Status");
                alertDialog.setMessage(alertMessage);
                alertDialog.show();

                /*Bundle bundle = new Bundle();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                EditText username = findViewById(R.id.userIn_text);

                bundle.putString("user", username.getText().toString());
                bundle.putString("room", spinner.getSelectedItem().toString());
                i.putExtras(bundle);

                startActivity(i);*/
            }
        });
    }
}
