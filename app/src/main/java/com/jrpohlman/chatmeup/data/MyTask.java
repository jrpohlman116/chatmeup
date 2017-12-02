package com.jrpohlman.chatmeup.data;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.jrpohlman.chatmeup.LoginActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by jorda on 12/2/2017.
 */
public class MyTask extends AsyncTask<Void, Void, Void> {
    private String alertMessage = "";
    private AlertDialog alertDialog;
    private Context context;
    private String username;
    private String password;

    public MyTask(Context context, String user, String pass){
        this.context = context;
        this.username = user;
        this.password = pass;
    }

    @Override
    protected Void doInBackground(Void... voids) {
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
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
        alertDialog.setMessage(alertMessage);
        alertDialog.show();
    }
}
