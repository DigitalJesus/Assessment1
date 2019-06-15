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

public class CustomAdapter extends BaseAdapter {
    private String TAG = "ADAPTERDEBUG";

    private LayoutInflater inflater;
    private ArrayList<CustomArrayObject> objects;
    private CellEditor cellEditor = new CellEditor();

    private class ViewHolder {
        EditText editTextDay;
        EditText editTextTime;
        EditText editTextDuration;
        EditText editTextRoom;

    }

    CustomAdapter(Context context, ArrayList<CustomArrayObject> objects) {
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
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.class_edit_listview_item, null);
            holder.editTextDay = convertView.findViewById(R.id.editText_day);
            holder.editTextTime = convertView.findViewById(R.id.editText_time);
            holder.editTextDuration = convertView.findViewById(R.id.editText_duration);
            holder.editTextRoom = convertView.findViewById(R.id.editText_roomNumber);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.editTextDay.setText(objects.get(position).getProp1());
        holder.editTextTime.setText(objects.get(position).getProp2());
        holder.editTextDuration.setText(objects.get(position).getProp3());
        holder.editTextRoom.setText(objects.get(position).getProp4());
        final View finalConvertView = convertView;
       holder.editTextDay.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().isEmpty()){
                    Log.d(TAG, "onTextChanged: pos: " + position); //TODO:: POSITION IS ALWAYS 0
                    cellEditor.modifyDayData(finalConvertView.getContext(), s.toString(), position);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.editTextTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    cellEditor.modifyStartTimeData(finalConvertView.getContext(), s.toString(), position);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.editTextDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    cellEditor.modifyDurationData(finalConvertView.getContext(), s.toString(), position);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.editTextRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    cellEditor.modifyClassRoomData(finalConvertView.getContext(), s.toString(), position);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        return convertView;
    }
}