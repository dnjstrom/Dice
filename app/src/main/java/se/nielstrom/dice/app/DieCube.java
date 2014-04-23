package se.nielstrom.dice.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Daniel on 2014-04-17.
 */
public class DieCube extends LinearLayout implements View.OnTouchListener {

    private Die die;
    private LayoutInflater inflater;
    private TextView tCurrentSide;
    private NumberScrambler scrambler;

    public DieCube(Context context) {
        super(context);
        init();
    }

    public DieCube(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DieCube(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.setOnTouchListener(this);
    }

    private void setText(String text) {
        TextView vText = (TextView) findViewById(R.id.current_side);
        vText.setText(text);
    }

    public DieCube attachDie(Die die) {
        this.die = die;

        setBackgroundColor(die.getColor());
        setText(die.getCurrentSide() + "");

        return this;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (die == null) {
            return false;
        }

        switch (motionEvent.getActionMasked()) {
            case ACTION_DOWN:
                scrambler = new NumberScrambler();
                scrambler.execute();
                changeBackground(-0.2);
                break;
            case ACTION_UP:
                if (scrambler != null) scrambler.cancel(true);
                setText(die.roll() + "");
                changeBackground(0.2);
                break;
            case ACTION_CANCEL:
                if (scrambler != null) scrambler.cancel(true);
                setText(die.getCurrentSide() + "");
                changeBackground(0.2);
                break;
        }

        return true;
    }

    private void changeBackground(double amount) {
        ColorDrawable bg = (ColorDrawable) getBackground();
        int color = bg.getColor();
        float[] hsv = {0,0,0};
        Color.colorToHSV(color, hsv);
        hsv[2] += amount;
        setBackgroundColor(Color.HSVToColor(hsv));
    }

    private class NumberScrambler extends AsyncTask<Void, Integer, Void> {
        private Vibrator vibrator;

        @Override
        protected Void doInBackground(Void... voids) {
            vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 10, 50};
            vibrator.vibrate(pattern, 0);

            while (true) {

                publishProgress(die.roll());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    return null;
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... v) {
            setText(v[0] + "");
        }

        @Override
        protected void onCancelled(Void v) {
            vibrator.cancel();
        }

        @Override
        protected void onPostExecute(Void v) {
            vibrator.cancel();
        }
    }
}
