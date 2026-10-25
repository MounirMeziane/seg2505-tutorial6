package com.uottawa.seg2505.mysqlitelab;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private FeedReaderDbHelper dbHelper;
    private EditText editTitle, editSubtitle;
    private TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new FeedReaderDbHelper(this);

        editTitle = findViewById(R.id.editTitle);
        editSubtitle = findViewById(R.id.editSubtitle);
        textResult = findViewById(R.id.textResult);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnFind = findViewById(R.id.btnFind);
        Button btnDelete = findViewById(R.id.btnDelete);

        // ADD
        btnAdd.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String subtitle = editSubtitle.getText().toString().trim();
            if (title.isEmpty()) {
                Toast.makeText(this, "Enter a title first", Toast.LENGTH_SHORT).show();
                return;
            }
            long id = dbHelper.addEntry(title, subtitle);
            textResult.setText("Added: " + title + " (id " + id + ")");
            editTitle.setText("");
            editSubtitle.setText("");
        });

        // FIND
        btnFind.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String subtitle = dbHelper.findSubtitle(title);
            if (subtitle == null) {
                textResult.setText("Not found: " + title);
            } else {
                editSubtitle.setText(subtitle);
                textResult.setText("Found: " + title + " - " + subtitle);
            }
        });

        // DELETE
        btnDelete.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            int rows = dbHelper.deleteEntry(title);
            if (rows > 0) {
                textResult.setText("Deleted " + rows + " row(s) with title " + title);
            } else {
                textResult.setText("Nothing to delete for: " + title);
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}