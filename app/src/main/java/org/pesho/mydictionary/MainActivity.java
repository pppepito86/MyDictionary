package org.pesho.mydictionary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.SortedSet;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    SortedSet<Word> set;
    private String label;
    private ListView listView;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        search = (EditText) findViewById(R.id.textView1);
        search.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateList();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        listView = (ListView) findViewById(R.id.listView1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, WordActivity.class);
                intent.putExtra("word", listView.getItemAtPosition(position).toString());
                startActivity(intent);
            }

        });

        verifyStoragePermissions(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.label:
                setLabel();
                return true;
            case R.id.add:
                startActivity(new Intent(MainActivity.this, AddActivity.class));
                return true;
            case R.id.test:
                startLearn(0);
                return true;
            case R.id.statistics:
                startLearn(3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setLabel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Label");

        final EditText input = new EditText(this);
        if (label != null) {
            input.setText(label);
        }
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setSelection(0, input.getText().length());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                label = input.getText().toString().trim();
                updateList();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void startLearn(int activity) {
        Intent intent = null;
        if (activity == 0) {
            intent = new Intent(MainActivity.this, Test.class);
        } else if (activity == 1) {
            //intent = new Intent(MainActivity.this, LearnActivity.class);
        } else if (activity == 2) {
            //intent = new Intent(MainActivity.this, BgDe.class);
        } else {
            intent = new Intent(MainActivity.this, StatisticsActivity.class);
        }
        intent.putExtra("label", label);
        startActivity(intent);
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

    @Override
    protected void onResume() {
        updateList();
        super.onResume();
    }

    void updateList() {
        try {
            ListViewAdapter adapter = new ListViewAdapter(this, ExcelUtil.getWordsByLabel(label, search.getText()
                    .toString()));
            listView.setAdapter(adapter);
        } catch (Exception e) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1);
            adapter.add(e.getMessage());
        }
    }


    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
