package se.nielstrom.dice.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_HOVER_ENTER;
import static android.view.MotionEvent.ACTION_HOVER_EXIT;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Daniel on 2014-04-17.
 */
public class DieView extends LinearLayout implements View.OnTouchListener {

    private Die die;
    private LayoutInflater inflater;

    public DieView(Context context) {
        super(context);
        init();
    }

    public DieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.setOnTouchListener(this);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case ACTION_DOWN:
                changeBackground(-0.2);
                break;
            case ACTION_UP:
            case ACTION_CANCEL:
                changeBackground(0.2);
                break;
        }
        return false;
    }

    private void changeBackground(double amount) {
        ColorDrawable bg = (ColorDrawable) getBackground();
        int color = bg.getColor();
        float[] hsv = {0,0,0};
        Color.colorToHSV(color, hsv);
        hsv[2] += amount;
        setBackgroundColor(Color.HSVToColor(hsv));
    }
}
