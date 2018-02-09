package org.pesho.mydictionary;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.SortedSet;

public class StatisticsActivity extends Activity {
	
	private String label;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        
        label = getIntent().getStringExtra("label");
        
        try {
			SortedSet<Word> words = ExcelUtil.getWordsByLabel(label, "");
			ListView listView = (ListView) findViewById(R.id.listView1);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(StatisticsActivity.this,
	                android.R.layout.simple_list_item_1);
			listView.setAdapter(adapter);
			adapter.add("Words count: " + words.size());
			int totalCorrect = 0;
			int totalWrong = 0;
			for (Word word : words) {
				totalCorrect += word.getCorrect();
				totalCorrect += word.getCorrect2();
				totalWrong += word.getWrong();
				totalWrong += word.getWrong2();
			}
			adapter.add("Total correct/all: " + totalCorrect + "/" + (totalCorrect + totalWrong));
			for (int i = 0; i <= 10; i++) {
				addLevel(i, words);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
	
	private void addLevel(int level, SortedSet<Word> words) {
		int count = 0;
		for (Word word : words) {
			if (word.getLevel() == level) {
				count++;
			} else if (level == 10 && word.getLevel() >= level) {
				count++;
			}
		}
		ListView listView = (ListView) findViewById(R.id.listView1);
		((ArrayAdapter<String>) listView.getAdapter()).add("Level " + level + ": " + count);
	}
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("label", label);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        label = savedInstanceState.getString("label");
        super.onRestoreInstanceState(savedInstanceState);
    }
    
}
