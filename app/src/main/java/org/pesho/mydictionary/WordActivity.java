package org.pesho.mydictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class WordActivity extends AppCompatActivity {

	private String wordString = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_word);
		setSupportActionBar(toolbar);
		loadData();
	}

	private void loadData() {
		if (wordString == null) {
			wordString = getIntent().getStringExtra("word");
		}
		Word word = ExcelUtil.getWord(wordString);
		EditText textView1 = (EditText) findViewById(R.id.textView1);
		String text = word.getWord();
		if (word.getGender() != null) {
			text = word.getGender() + " " + text;
			if (word.getPlural() != null && word.getPlural().length() > 0) {
				text += ", " + word.getPlural();
			}
		}
		textView1.setText(text);

		EditText textView2 = (EditText) findViewById(R.id.textView2);
		String meaning = word.getMeaning();
		if (word.getAdditionalInfo().length() > 0) {
			meaning += "\n\n" + word.getAdditionalInfo();
		}
		if (word.getLabelsAsString().length() > 0) {
			meaning += "\n\n" + word.getLabelsAsString();
		}
		meaning += "\n\nLevel " + word.getLevel() + ", " + (word.getCorrect()+word.getCorrect2()) + "/" +
				(word.getCorrect()+word.getCorrect2() + word.getWrong() + word.getWrong2());
		textView2.setText(meaning);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("wordString", wordString);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		wordString = savedInstanceState.getString("wordString");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_word, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit:
			startEditActivity();
			return true;
		case R.id.delete:
			onDeletePressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void startEditActivity() {
		Intent intent = new Intent(WordActivity.this, EditActivity.class);
		intent.putExtra("word", wordString);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			loadData();
		}
	}

	public void onDeletePressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete");

		builder.setMessage("Are you sure?");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					ExcelUtil.removeWord(wordString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ExcelUtil.load();
				WordActivity.this.finish();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
	}

}
