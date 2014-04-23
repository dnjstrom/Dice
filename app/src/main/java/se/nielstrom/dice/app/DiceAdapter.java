package se.nielstrom.dice.app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2014-04-17.
 */
public class DiceAdapter extends BaseAdapter {

    private final int SIDES = 0;
    private final int AMOUNT = 1;

    private final Context context;
    private final LayoutInflater inflater;
    int[][] dice = { // sides, amount
            {2,1},
            {4,3},
            {6,5},
            {10,2},
            {12,4},
            {20,1},
            {26,2},
            {32,1},
            {100,3}
    };

    public DiceAdapter(Context context) {
        this.context = context;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dice.length;
    }

    @Override
    public Object getItem(int i) {
        return dice[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DieListItem item = new DieListItem(context);
        item.setSideCount(dice[i][SIDES]);

        for(int j=0; j < dice[i][AMOUNT]; j++) {
            item.addDie();
        }

        return item;
    }
}
