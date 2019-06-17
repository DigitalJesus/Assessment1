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
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private String TAG = "ADAPTERDEBUG";

    private LayoutInflater inflater;
    private ArrayList<CustomArrayObject> objects;

    private class ViewHolder {
        TextView cellId_;
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
        final ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.class_edit_listview_item, null);
            holder.cellId_ = convertView.findViewById(R.id.idViewer);
            holder.editTextDay = convertView.findViewById(R.id.editText_day);
            holder.editTextTime = convertView.findViewById(R.id.editText_time);
            holder.editTextDuration = convertView.findViewById(R.id.editText_duration);
            holder.editTextRoom = convertView.findViewById(R.id.editText_roomNumber);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cellId_.setText(Integer.toString(objects.get(position).getId_()));
        holder.editTextDay.setText(objects.get(position).getDay());
        holder.editTextTime.setText(objects.get(position).getTime());
        holder.editTextDuration.setText(objects.get(position).getDuration());
        holder.editTextRoom.setText(objects.get(position).getRoom());

        final View finalConvertView = convertView;

        //TODO: FIX THIS SHIT, won't save anything to anywhere.
       //   Wasted 4 weeks.
//        holder.editTextDay.addTextChangedListener(new TextWatcher(){
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//               CellEditor cellEditor = new CellEditor();
//               if(!s.toString().isEmpty()){
//                  cellEditor.tempData(objects.get(position).getId_(), s ,0);
//               }
//            }
//        });
//
//        holder.editTextTime.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//               CellEditor cellEditor = new CellEditor();
//               if(!s.toString().isEmpty()){
//                  cellEditor.tempData(objects.get(position).getId_(), s ,1);
//               }
//            }
//        });
//
//        holder.editTextDuration.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//               CellEditor cellEditor = new CellEditor();
//               if(!s.toString().isEmpty()){
//                  cellEditor.tempData(objects.get(position).getId_(), s ,2);
//               }
//            }
//        });
//
//        holder.editTextRoom.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                CellEditor cellEditor = new CellEditor();
//                if(!s.toString().isEmpty()){
//                    cellEditor.tempData(objects.get(position).getId_(), s ,3);
//                }
//            }
//        });
        return convertView;
    }
}