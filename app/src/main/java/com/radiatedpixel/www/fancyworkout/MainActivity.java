package com.radiatedpixel.www.fancyworkout;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


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

            getActivity().getActionBar().setTitle(R.string.app_name);

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

                // Create new fragment and transaction
                Fragment newFragment = new ExerciseFragment();
                Bundle data = new Bundle();
                data.putString("name", item.name);
                data.putIntArray("repetitions", item.repetitions);
                newFragment.setArguments(data);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        };
    }

    /**
     * Exercise fragment
     */
    public static class ExerciseFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);
            return rootView;
        }

        @Override
        public void onActivityCreated (Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            getActivity().getActionBar().setTitle(this.getArguments().getString("name"));

            /**
             * Set the text and element for the repetitions overview.
             */
            final LinearLayout layout = (LinearLayout) getView().findViewById(R.id.repetitionsDisplay);
            int[] repetitions = this.getArguments().getIntArray("repetitions");
            String html = "";
            for (int i = 0; i < repetitions.length; i++) {
                html += "<strong>" + repetitions[i] + "</strong> ";
            }
            final TextView text = new TextView(getActivity());
            text.setText(Html.fromHtml(html));
            layout.addView(text);

            Button btn = (Button) getView().findViewById(R.id.doneButton);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    (getView().findViewById(R.id.workLayout)).setVisibility(View.INVISIBLE);
                    (getView().findViewById(R.id.pauseLayout)).setVisibility(View.VISIBLE);

                    new CountDownTimer(5000, 1000) {
                        TextView time = (TextView) getView().findViewById(R.id.time);

                        public void onTick(long timeUntilFinished) {
                            time.setText(Long.toString(timeUntilFinished / 1000));
                        }

                        public void onFinish() {
                            time.setText("Done!");
                            try {
                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                Ringtone r = RingtoneManager.getRingtone(getActivity(), notification);
                                r.play();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            });
        }
    }
}
