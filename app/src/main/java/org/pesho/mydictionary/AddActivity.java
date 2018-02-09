package org.pesho.mydictionary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		initWordField();
		initSaveButton();
	}

	private void initWordField() {
		EditText word = (EditText) findViewById(R.id.textView1);
		word.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(final Editable s) {
				AsyncTask.execute(new Runnable() {
					@Override
					public void run() {
						String meaning = GoogleTranslator.getInstance().translate(s.toString());
						setMeaning(meaning);
					}
				});
			}

		});
	}

	private void setMeaning(final String meaning) {
		if (meaning == null) return;

		this.runOnUiThread(new Runnable() {
			public void run() {
				EditText word = (EditText) findViewById(R.id.textView2);
				word.setText(meaning);
			}
		});
	}

	private void initSaveButton() {
		Button saveButton = (Button) findViewById(R.id.buttonSave);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSaveClicked();
			}

		});
	}

	private void onSaveClicked() {
		try {
			EditText editText1 = (EditText) findViewById(R.id.textView1);
			EditText editText2 = (EditText) findViewById(R.id.textView2);
			EditText editText3 = (EditText) findViewById(R.id.textView3);
			String word = editText1.getText().toString().trim();
			String meaning = editText2.getText().toString().trim();
			String label = editText3.getText().toString().trim();
			saveWord(word, meaning, label);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finish();
	}

	protected void saveWord(String word, String meaning, String label) throws Exception {
		if (!word.equals("") && !meaning.equals("")) {
			ExcelUtil.add(word, meaning, label);
			ExcelUtil.load();
		}
	}

}
