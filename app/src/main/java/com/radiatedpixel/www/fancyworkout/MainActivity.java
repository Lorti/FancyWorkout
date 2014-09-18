package com.radiatedpixel.www.fancyworkout;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Overview fragment
     */
    public static class MainFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onActivityCreated (Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            Exercise[] exercises = {
                new Exercise("Burpees", new int[] { 10, 10, 10 }, null, false),
                new Exercise("Jump Rope", new int[] { 20 }, null, true),
                new Exercise("Inverted Rows", new int[] { 14, 14, 14, 14, 14 }, null, false)
            };

            ArrayAdapter adapter = new ExerciseListAdapter(this.getActivity(), exercises);
            ListView listView = (ListView) getView().findViewById(R.id.listView);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(mainListener);
        }

        protected AdapterView.OnItemClickListener mainListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise item = (Exercise) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), ExerciseActivity.class);
                intent.putExtra("name", item.name);
                intent.putExtra("repetitions", item.repetitions);
                intent.putExtra("total", item.getTotalRepetitions());
                startActivity(intent);
            }
        };
    }
}
