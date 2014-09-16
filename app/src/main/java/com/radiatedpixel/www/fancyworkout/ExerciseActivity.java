package com.radiatedpixel.www.fancyworkout;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ExerciseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Intent intent = getIntent();
        getActionBar().setTitle(intent.getStringExtra("name"));

        ExerciseFragment fragment = new ExerciseFragment();
        Bundle arguments = new Bundle();
        arguments.putIntArray("repetitions", intent.getIntArrayExtra("repetitions"));
        arguments.putInt("currentSet", 0);
        fragment.setArguments(arguments);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exercise, menu);
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
     * Exercise fragment
     */
    public static class ExerciseFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_exercise, container, false);
            return rootView;
        }

        @Override
        public void onActivityCreated (Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            /**
             * Set the text and element for the repetitions overview.
             */
            TextView text = (TextView) getView().findViewById(R.id.repetitions);
            int[] repetitions = getArguments().getIntArray("repetitions");
            int currentSet = getArguments().getInt("currentSet");
            String html = "";
            for (int i = 0; i < repetitions.length; i++) {
                if (i == currentSet) {
                    html += "<strong>" + repetitions[i] + "</strong> ";
                } else {
                    html += repetitions[i] + " ";
                }
            }
            text.setText(Html.fromHtml(html));

            /**
             * Set the text for the current set.
             */
            text = (TextView) getView().findViewById(R.id.value);
            text.setText(Integer.toString(repetitions[currentSet]));

            Button btn = (Button) getView().findViewById(R.id.doneButton);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PauseFragment fragment = new PauseFragment();
                    Bundle arguments = new Bundle();
                    arguments.putInt("currentSet", getArguments().getInt("currentSet"));
                    fragment.setArguments(arguments);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                }
            });
        }
    }

    /**
     * Pause fragment
     */
    public static class PauseFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pause, container, false);
            return rootView;
        }

        @Override
        public void onActivityCreated (Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            /**
             * Start the countdown for this pause.
             */
            new CountDownTimer(6000, 1000) {
                TextView time = (TextView) getView().findViewById(R.id.time);

                public void onTick(long timeUntilFinished) {
                    time.setText(Long.toString(timeUntilFinished / 1000));
                }

                public void onFinish() {
                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getActivity(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // TODO
                    ExerciseFragment fragment = new ExerciseFragment();
                    Bundle arguments = new Bundle();
                    arguments.putIntArray("repetitions", getActivity().getIntent().getIntArrayExtra("repetitions"));
                    arguments.putInt("currentSet", getArguments().getInt("currentSet") + 1);
                    fragment.setArguments(arguments);

                    getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                }
            }.start();
        }
    }
}
