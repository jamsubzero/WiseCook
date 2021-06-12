package com.mit.jamsubzero.wisecook.Course;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mit.jamsubzero.wisecook.Data.MyDatabase;
import com.mit.jamsubzero.wisecook.Data.Singleton;
import com.mit.jamsubzero.wisecook.R;

import java.util.ArrayList;

/**
 * Created by jamsubzero on 4/21/2016.
 */
public class ThirdFragment extends Fragment{

    ListView listView;

    MyDatabase db;

    CustomCoursesAdapter dataAdapter = null;

    private static  final int def= 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.third_frag, container, false);
        listView = (ListView) v.findViewById(R.id.courseList);

        db = new MyDatabase(this.getActivity().getApplicationContext());
        //   TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
        // tv.setText(getArguments().getString("msg"));
        this.loadCourse();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataAdapter.setSelectedIndex(position);
                dataAdapter.notifyDataSetChanged();
                Course c = (Course)parent.getItemAtPosition(position);
                Singleton.selCourses.clear(); // Clear it first
                Singleton.selCourses.put(c.getCode(), c.getName());
                shakeButton();
            }
        });

        return v;
    }


    //========================================================
    @Override
    public void onResume(){
        super.onResume();
        restoreSelectedIndex();
    }

    @Override
    public void onStart() {
        super.onStart();
        restoreSelectedIndex();
    }

    void restoreSelectedIndex(){
        int curPos = -1;
        if(!Singleton.selCourses.isEmpty()) {
            for(int i = 0; i < dataAdapter.getList().size(); i++){
                Course course = dataAdapter.getList().get(i);
                for (int c : Singleton.selCourses.keySet()) {
                    if(course.getCode() == c){
                        curPos = i;
                        break;
                    }
                }
            }

        }
        dataAdapter.setSelectedIndex(curPos);
        dataAdapter.notifyDataSetChanged();
    }



//============================

    //get data from the database
    private void loadCourse(){
        ArrayList<Course> courseList = new ArrayList<Course>();

        Cursor c = db.getCodes(MyDatabase.CATEG_COURSES);

        Course defCourse = new Course(-1, "(Any Course)", false);
        courseList.add(0,defCourse);
        while (c.moveToNext()){
            Course course = new Course(c.getInt(0), c.getString(1), false);
            courseList.add(course);
        }

        dataAdapter = new CustomCoursesAdapter(this.getActivity().getApplicationContext(),R.layout.course_info, courseList);
        listView.setAdapter(dataAdapter);

    }

    private void shakeButton(){
        ViewPager vp=(ViewPager) getActivity().findViewById(R.id.viewPager);
        ImageButton nxt = (ImageButton) this.getActivity().findViewById(R.id.next);
        Animation myAnim = AnimationUtils.loadAnimation(this.getActivity(), R.anim.milkshake);

        nxt.setAnimation(myAnim);
        nxt.startAnimation(myAnim);
    }







}

/*  if (savedInstanceState != null) {

        ArrayList<Course> countries = dataAdapter.getList();
        for (Course c : countries){
                 String code = c.getCode();
                     if(Singleton.selCourses.containsKey(Integer.parseInt(code))){
                         c.setSelected(true);
                     }else{
                         c.setSelected(false);
                     }
       }

    }*/