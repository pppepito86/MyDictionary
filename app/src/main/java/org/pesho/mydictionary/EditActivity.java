package org.pesho.mydictionary;

import android.os.Bundle;
import android.widget.EditText;

public class EditActivity extends AddActivity {

	private String wordString = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (wordString == null) {
			wordString = getIntent().getStringExtra("word");
		}
		Word word = ExcelUtil.getWord(wordString);

		EditText wordView = (EditText) findViewById(R.id.textView1);
		wordView.setEnabled(false);
		wordView.setText(wordString);
		
		EditText meaningView = (EditText) findViewById(R.id.textView2);
		meaningView.setText(word.getMeaning());

		EditText labelView = (EditText) findViewById(R.id.textView3);
		labelView.setText(word.getLabelsAsString());
	}
	
	protected void saveWord(String word, String meaning, String label) throws Exception {
		if (!word.equals("") && !meaning.equals("")) {
			ExcelUtil.edit(word, meaning, label);
			ExcelUtil.load();
			setResult(RESULT_OK);
		}
	}

}
