package com.mit.jamsubzero.wisecook.Course;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import com.mit.jamsubzero.wisecook.R;

import java.util.ArrayList;

/**
 * Created by jamsubzero on 4/20/2016.
 */

public class CustomCoursesAdapter extends ArrayAdapter<Course> {

    private ArrayList<Course> CourseList;
    Context context;

    static int selectedIndex = -1;

    public CustomCoursesAdapter(Context context, int textViewResourceId,
                                ArrayList<Course> CourseList) {
        super(context, textViewResourceId, CourseList);
        this.CourseList = new ArrayList<Course>();
        this.CourseList.addAll(CourseList);
        this.context = context;
    }

    private class ViewHolder {
        //  TextView code;
        RadioButton name;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.course_info, parent, false);

            holder = new ViewHolder();
            //  holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (RadioButton) convertView.findViewById(R.id.course);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(selectedIndex == position){
            holder.name.setChecked(true);

        }
        else{
            holder.name.setChecked(false);
        }

        Course course = CourseList.get(position);
        //    holder.code.setText(" (" +  Course.getCode() + ")");
        holder.name.setText(course.getName());
//        holder.name.setChecked(Course.isSelected());
        return convertView;

    }



    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    public ArrayList<Course> getList(){
        return  CourseList;
    }

}