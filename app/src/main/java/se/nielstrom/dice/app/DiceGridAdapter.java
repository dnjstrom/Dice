package se.nielstrom.dice.app;

import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2014-04-17.
 */
public class DiceGridAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;
    List<Die> dice = new ArrayList<>();

    public DiceGridAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);

        dice.add(new Die(4, Color.YELLOW));
        dice.add(new Die(12, Color.CYAN));
        dice.add(new Die(20, Color.MAGENTA));
    }

    @Override
    public int getCount() {
        return dice.size();
    }

    @Override
    public Object getItem(int i) {
        return dice.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View dieView = inflater.inflate(R.layout.die, viewGroup, false);
        final Die die = dice.get(i);
        dieView.setBackgroundColor(die.getColor());
        TextView text = (TextView) dieView.findViewById(R.id.nr_of_sides);
        text.setText(die.getCurrentSide() + "");

        dieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView text = (TextView) dieView.findViewById(R.id.nr_of_sides);
                text.setText(die.roll() + "");
            }
        });

        return dieView;
    }
}
