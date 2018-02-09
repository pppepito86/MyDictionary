package org.pesho.mydictionary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class AddActivity extends Activity {

	private static final String TMP_IMAGE_NAME = "tmp_picture";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		initSaveButton();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			//showPicture();
		}
	}

}
