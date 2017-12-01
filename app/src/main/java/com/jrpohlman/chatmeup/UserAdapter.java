package com.jrpohlman.chatmeup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class UserAdapter extends ArrayAdapter<User>{
    public UserAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(

                    R.layout.list_item, parent, false);

        }

        User currentWord = getItem(position);

        TextView userTextView = (TextView) listItemView.findViewById(R.id.user_textview);
        userTextView.setText(currentWord.getUsername());

        TextView messageTextView = (TextView) listItemView.findViewById(R.id.message_textview);
        messageTextView.setText(currentWord.getMessage());

        return listItemView;
    }
}
