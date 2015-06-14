package com.roofnfloors.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.roofnfloors.R;
import com.google.android.gms.maps.model.LatLng;
import com.roofnfloors.parser.Project;
import com.roofnfloors.ui.RoofnFloorsActivity.UIHandler;

public class ListViewFragment extends Fragment {
    private static final String TAG = ListViewFragment.class.getCanonicalName();
    private Context mContext = null;
    private ListView lv;
    private MySimpleArrayAdapter adapter;
    public static HashMap<LatLng, Project> myProjectsMap = new HashMap<LatLng, Project>();
    private UIHandler mUiHandler;

    @Override
    public void onAttach(Activity activity) {
        mContext = activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.listview_fragment, container,
                false);
        lv = (ListView) rootView.findViewById(R.id.projectList);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar ab = getActivity().getActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Projects");

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                    int position, long arg3) {
                Project p = (Project) adapter.getItemAtPosition(position);
                Intent in = new Intent(ListViewFragment.this.getActivity(),
                        ProjectDetailsActivity.class);
                in.putExtra("projectId", p.getmProjectid());
                in.putExtra("ProjectName", p.getmProjectName());
                startActivity(in);
            }
        });
        mUiHandler = ((RoofnFloorsActivity) getActivity()).getmUiHandler();
        mUiHandler.setmListViewFragment(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lv = null;
    }

    @Override
    public void onStop() {
        cleanUp();
        super.onStop();
    }

    public class MySimpleArrayAdapter extends ArrayAdapter<Project> {
        private final Context context;
        private final List<Project> values;

        public MySimpleArrayAdapter(Context context, List<Project> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.project_list_item, parent,
                    false);
            TextView pname = (TextView) rowView.findViewById(R.id.projectName);
            pname.setText(values.get(position).getmProjectName());

            return rowView;
        }
    }

    public ListView getLv() {
        return lv;
    }

    public void setLv(ListView lv) {
        this.lv = lv;
    }

    private void cleanUp() {
        mContext = null;
        lv = null;
        adapter = null;
        myProjectsMap = null;
    }

    public void onProjectListDataReceived(final ArrayList<Project> plist) {
        Log.d("Test", TAG + ":onProjectListDataReceived");
        adapter = new MySimpleArrayAdapter(mContext, plist);
        if (lv != null)
            lv.setAdapter(adapter);
    }
}