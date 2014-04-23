package se.nielstrom.dice.app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Daniel on 2014-04-23.
 */
public class DieListItem extends LinearLayout implements View.OnTouchListener {
    private LayoutInflater inflater;
    private Die die;
    private LinearLayout pane;
    private int nrOfDice;
    private GestureDetector detector;
    NumberScrambler scrambler;
    private float downY;
    private float downX;

    public DieListItem(Context context) {
        super(context);
        init();
    }

    public DieListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DieListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.die_list_item, this, true);

        nrOfDice = 0;
        pane = (LinearLayout) findViewById(R.id.dice_pane);
        die = new Die();
        addDie();

        setOnTouchListener(this);

        detector = new GestureDetector(getContext(), new Detector());
    }

    public void setSideCount(int nrOfSides) {
        die.setNrOfSides(nrOfSides);
        rollAll();
    }

    public void addDie() {
        if (nrOfDice >= 1) {
            TextView separator = new TextView(getContext());
            separator.setText("+");
            separator.setTextSize(20);
            separator.setPadding(10, 0, 10, 0);
            pane.addView(separator);
        }

        TextView text = new TextView(getContext());
        text.setTextSize(40);
        text.setText(die.getNrOfSides() + "");
        pane.addView(text);

        nrOfDice++;

        calc_sum();
    }

    public void removeDie() {
        if(nrOfDice <= 1) {
            return;
        }

        pane.removeViewAt(nrOfDice - 1); //dice
        pane.removeViewAt(nrOfDice - 1); //separator
        nrOfDice--;
        calc_sum();
    }

    public void rollAll() {
        for (int i = 0; i < pane.getChildCount(); i+=2) {
            TextView text = (TextView) pane.getChildAt(i);
            text.setText(die.roll() + "");
        }

        calc_sum();
    }

    public void addRemoveDice(int amount) {
            for (int i=0; i<Math.abs(amount); i++) {
                if (amount > 0) {
                    addDie();
                } else {
                    removeDie();
                }
            }
    }

    public int calc_sum() {
        int sum = 0;

        for (int i = 0; i < pane.getChildCount(); i+=2) {
            TextView text = (TextView) pane.getChildAt(i);
            sum += Integer.parseInt( text.getText().toString() );
        }

        TextView sum_text = (TextView) findViewById(R.id.sum_text);
        sum_text.setText(sum + "");

        return sum;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        boolean result = false;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                Log.d("####", "Up!");
                if (scrambler != null) {
                    scrambler.cancel(true);
                }
                rollAll();
            case MotionEvent.ACTION_CANCEL:
                Log.d("####", "Cancel!");
                if (scrambler != null) {
                    scrambler.cancel(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();

                Log.d("####", "Move: " + x + " " + y);

                if (Math.abs(downY - y) < 10) {
                    float delta = x - downX;
                    int nrOfD = (int) (delta / 100);
                    downX += nrOfD * 100;
                    addRemoveDice(nrOfD);
                    result = true;
                }
                break;
        }

        return detector.onTouchEvent(event) || result;
    }

    public class Detector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("####", "Down!");
            downX = e.getX();
            downY = e.getY();
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d("####", "ShowPress!");
            if (scrambler != null) {
                scrambler.cancel(true);
            }

            scrambler = new NumberScrambler();
            scrambler.execute();
        }
    }

    private class NumberScrambler extends AsyncTask<Void, Void, Void> {
        private Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        private List<String> state;

        @Override
        protected void onPreExecute() {
            state = new LinkedList<>();

            for (int i=0; i<pane.getChildCount(); i+=2) {
                TextView text = (TextView) pane.getChildAt(i);
                state.add(text.getText().toString());
            }

            Log.d("####", "Pre: " + state.size());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            long[] pattern = {0, 10, 50};
            vibrator.vibrate(pattern, 0);

            while (true) {
                publishProgress();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    return null;
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... v) {
            rollAll();
        }

        @Override
        protected void onCancelled(Void v) {
            Log.d("####", "Cancelled: " + state.size());

            for (int i=0, j=0; i<pane.getChildCount(); i+=2, j++) {
                TextView text = (TextView) pane.getChildAt(i);
                text.setText(state.get(j));
            }

            vibrator.cancel();
        }

        @Override
        protected void onPostExecute(Void v) {
            vibrator.cancel();
        }
    }
}
