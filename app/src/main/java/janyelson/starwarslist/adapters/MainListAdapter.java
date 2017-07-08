package janyelson.starwarslist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import janyelson.starwarslist.R;
import janyelson.starwarslist.utils.Item;

/**
 * Created by MEU PC on 04/07/2017.
 */

public class MainListAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> items;

    public MainListAdapter(Context context, ArrayList<Item> items) {
        super(context, -1, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.main_list, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.name);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(items.get(position).name);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openDescription(position);
            }
        };
        return rowView;
    }

}
