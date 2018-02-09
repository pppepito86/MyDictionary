package org.pesho.mydictionary;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

public class Test extends Activity {

	boolean isNewWordScreen;
	long stoppedTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_learn);

		if (savedInstanceState == null) {
			String label = getIntent().getStringExtra("label");
			TestUtil.prepareTest(label);
			isNewWordScreen = true;
			stoppedTime = -1;
		} else {
			isNewWordScreen = savedInstanceState.getBoolean("isNewWordScreen");
			stoppedTime = savedInstanceState.getLong("stoppedTime");
			if (isNewWordScreen) {
				chronometerSetTime(savedInstanceState.getLong("chronometerSeconds"));
			} else {
				chronometerSetTime(stoppedTime);
			}
		}

		if (TestUtil.isFinished()) {
			finish();
			return;
		}

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isNewWordScreen) {
					isNewWordScreen = false;
					stopChronometer();
					initScreenCheckWord();
				} else {
					checkWord(true);
				}
			}

		});

		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkWord(false);
			}
		});

		initScreen();
		if (savedInstanceState == null) {
			startChronometer(0);
		} else if (isNewWordScreen) {
			startChronometer(savedInstanceState.getLong("chronometerSeconds"));
		}
	}
	
	private void checkWord(boolean isCorrect) {
		storeWord(isCorrect, stoppedTime);
		
		isNewWordScreen = true;
		TestUtil.increaseIndex();
		
		if (TestUtil.isFinished()) {
			TestUtil.prepareTest(TestUtil.getLabel());
			if (TestUtil.isFinished()) {
				finish();
				return;
			}
		}
		initScreenNewWord();
		startChronometer(0);
	}


	private void storeWord(final boolean isCorrect, final long stoppedTime) {
		final Word word = TestUtil.getCurrentWord();

		Thread t = new Thread() {
			public void run() {
				try {
					long time = (int) (stoppedTime / 100);
					TestUtil.updateWord(word, isCorrect, time);
					ExcelUtil.writeWordStats(word);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		};
		t.start();
	}

	private void initScreen() {
		if (isNewWordScreen) {
			initScreenNewWord();
		} else {
			initScreenCheckWord();
		}
	}

	private void initScreenCheckWord() {
		Button correctButton = (Button) findViewById(R.id.button1);
		correctButton.setText("Correct");

		Button wrongButton = (Button) findViewById(R.id.button2);
		wrongButton.setVisibility(View.VISIBLE);
		wrongButton.setText("Wrong");

		if (TestUtil.isFirstTest()) {
			EditText textView2 = (EditText) findViewById(R.id.textView2);
			textView2.setText(TestUtil.checkText() + "\n\n" + TestUtil.additionalText());
		} else {
			EditText textView1 = (EditText) findViewById(R.id.textView1);
			textView1.setText(TestUtil.checkText());
			EditText textView2 = (EditText) findViewById(R.id.textView2);
			textView2.setText(TestUtil.testText() + "\n\n" + TestUtil.additionalText());
		}

	}

	private void initScreenNewWord() {
		Button correctButton = (Button) findViewById(R.id.button1);
		correctButton.setText("Continue");

		Button wrongButton = (Button) findViewById(R.id.button2);
		wrongButton.setVisibility(View.INVISIBLE);

		EditText textView1 = (EditText) findViewById(R.id.textView1);
		EditText textView2 = (EditText) findViewById(R.id.textView2);
		
		if (TestUtil.isFirstTest()) {
			textView1.setText(TestUtil.testText());
			textView2.setText("");
		} else {
			textView1.setText("");
			textView2.setText(TestUtil.testText());
		}

	}

	private void startChronometer(long seconds) {
		Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer1);
		chronometer.stop();
		chronometer.setBase(SystemClock.elapsedRealtime() - seconds);
		chronometer.start();
	}

	private void stopChronometer() {
		Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer1);
		stoppedTime = chronometerGetTime();
		chronometer.stop();
	}

	private long chronometerGetTime() {
		Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer1);
		return SystemClock.elapsedRealtime() - chronometer.getBase();
	}

	private void chronometerSetTime(long seconds) {
		Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer1);
		chronometer.setBase(SystemClock.elapsedRealtime() - seconds);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isNewWordScreen", isNewWordScreen);
		outState.putLong("chronometerSeconds", chronometerGetTime());
		outState.putLong("stoppedTime", stoppedTime);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		isNewWordScreen = savedInstanceState.getBoolean("isNewWordScreen");
		stoppedTime = savedInstanceState.getLong("stoppedTime");
		if (isNewWordScreen) {
			chronometerSetTime(savedInstanceState.getLong("chronometerSeconds"));
		} else {
			chronometerSetTime(stoppedTime);
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

}
