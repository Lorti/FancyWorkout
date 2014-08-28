package com.radiatedpixel.www.fancyworkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ExerciseListAdapter extends ArrayAdapter {
    private Context context;
    private Exercise[] values;

    public ExerciseListAdapter(Context context, Exercise[] values) {
        super(context, R.layout.view_exercise_list, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View exerciseView = inflater.inflate(R.layout.view_exercise_list, parent, false);
        TextView textViewName = (TextView) exerciseView.findViewById(R.id.name);
        TextView textViewSets = (TextView) exerciseView.findViewById(R.id.sets);
        TextView textViewRepetitions = (TextView) exerciseView.findViewById(R.id.repetitions);

        Exercise e = values[position];
        textViewName.setText(e.name);
        textViewSets.setText(e.getSetsString());
        textViewRepetitions.setText(e.getTotalRepetitionsString());

        return exerciseView;
    }
}
