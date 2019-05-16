package com.example.assessment1;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CustomAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<CustomArrayObject> objects;

    private class ViewHolder {
        EditText editTextDay;
        EditText editTextTime;
        EditText editTextDuration;
        EditText editTextRoom;

    }

    public CustomAdapter(Context context, ArrayList<CustomArrayObject> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public int getCount() {
        return objects.size();
    }

    public CustomArrayObject getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.class_edit_listview_item, null);
            holder.editTextDay = (EditText) convertView.findViewById(R.id.editText_day);
            holder.editTextTime = (EditText) convertView.findViewById(R.id.editText_time);
            holder.editTextDuration = (EditText) convertView.findViewById(R.id.editText_duration);
            holder.editTextRoom = (EditText) convertView.findViewById(R.id.editText_roomNumber);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.editTextDay.setText(objects.get(position).getProp1());
        holder.editTextTime.setText(objects.get(position).getProp2());
        holder.editTextDuration.setText(objects.get(position).getProp3());
        holder.editTextRoom.setText(objects.get(position).getProp4());

        holder.editTextDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ViewHolder holder = new ViewHolder();
                //TODO: Learn how to pull data from the listview items.
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return convertView;
    }

    private void cacheUpdatedValue(String updatedValue){

    }
}