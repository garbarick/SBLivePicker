package ru.net.serbis.livepicker;
import android.app.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.graphics.drawable.*;
import android.graphics.*;

public class ListAdapter extends ArrayAdapter<Item>
{
    private int selected = -1;
    
    public ListAdapter(Activity context, int layoutId, List<Item> items)
    {
        super(context, layoutId, items);
    }

    public void setSelected(int selected)
    {
        this.selected = selected;
    }

    public int getSelected()
    {
        return selected;
    }

    private Activity getActivity()
    {
        return (Activity) getContext();
    }
    
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = getActivity().getLayoutInflater().inflate(R.layout.item, null);
        }
        
        if (position == selected)
        {
            view.setBackgroundColor(Color.GRAY);
        }
        else
        {
            view.setBackgroundColor(Color.DKGRAY);
        }
        
        Item item = getItem(position);
        
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageDrawable(item.getIcon());
       
        TextView label = (TextView) view.findViewById(R.id.label);
        label.setText(item.getLabel());
        
        return view;
    }
}
