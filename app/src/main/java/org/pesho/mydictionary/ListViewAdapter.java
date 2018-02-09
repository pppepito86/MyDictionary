package org.pesho.mydictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.SortedSet;

public class ListViewAdapter extends BaseAdapter {
	 
    Context context;
    Word[] words;
    LayoutInflater inflater;
 
    public ListViewAdapter(Context context, SortedSet<Word> words) {
        this.context = context;
        this.words = words.toArray(new Word[0]);
    }
 
    @Override
    public Object getItem(int position) {
        return words[position].getWord();
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
 
        TextView textView = (TextView) itemView.findViewById(R.id.word);
        ImageView imageView= (ImageView) itemView.findViewById(R.id.levelImage);
 
        textView.setText(words[position].getWord());
        int level = words[position].getLevel() / 2;
        
        if (level == 0) {
        	imageView.setImageResource(R.drawable.level0);
        } else if (level == 1) {
        	imageView.setImageResource(R.drawable.level1);
        } else if (level == 2) {
        	imageView.setImageResource(R.drawable.level2);
        } else if (level == 3) {
        	imageView.setImageResource(R.drawable.level3);
        } else {
        	imageView.setImageResource(R.drawable.level4);
        }
        
        return itemView;
    }

	@Override
	public int getCount() {
		return words.length;
	}
}
