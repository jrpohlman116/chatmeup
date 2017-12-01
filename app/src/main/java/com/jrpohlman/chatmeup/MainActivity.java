package com.jrpohlman.chatmeup;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jrpohlman.chatmeup.data.UserContract;
import com.jrpohlman.chatmeup.data.UserContract.UserEntry;
import com.jrpohlman.chatmeup.data.UserDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<User> words = new ArrayList<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String activeUser = getIntent().getStringExtra("user");

        words.add(new User("Where are you going?", "minto wuksus"));
        words.add(new User("What is your name?", "tinnә oyaase'nә"));
        words.add(new User("My name is...", "oyaaset..."));
        final UserAdapter adapter = new UserAdapter(this, words);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        Button btn = findViewById(R.id.send);
        EditText messageText = (EditText) findViewById(R.id.message_textview);
        final String message = messageText.getText().toString();

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                words.add(0, new User(activeUser, message));
                adapter.notifyDataSetChanged();
            }
        });
    }

    /*private void displayDatabaseInfo() {
        UserDbHelper mDbHelper = new UserDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        if (activeChat.equals("Software Design")){
            cursor = db.rawQuery("SELECT * FROM " + UserContract.UserEntry.TABLE1_NAME, null);        }
        else {
            cursor = db.rawQuery("SELECT * FROM " + UserContract.UserEntry.TABLE2_NAME, null);        }

        try {
            while (cursor.moveToNext()) {
                users.add(0, new User(cursor.getString(cursor.getColumnIndex("username")), cursor.getString(cursor.getColumnIndex("message"))));
            }

            adapter.notifyDataSetChanged();

        } finally {
            cursor.close();
        }

    }*/
}
