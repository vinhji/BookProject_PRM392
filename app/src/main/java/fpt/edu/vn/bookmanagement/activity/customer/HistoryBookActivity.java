package fpt.edu.vn.bookmanagement.activity.customer;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.activity.MainActivity;
import fpt.edu.vn.bookmanagement.model.Category;
import fpt.edu.vn.bookmanagement.sql.DatabaseHelper;

public class HistoryBookActivity extends AppCompatActivity {

    // Database helper instance for interacting with the database
    DatabaseHelper db;

    // UI components
    ListView alist;
    Spinner listCategory;

    // List of categories
    List<Category> categoryList;

    // User ID for retrieving history data
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout
        setContentView(R.layout.activity_customer_historybook);

        // Set the action bar title and enable the back button
        getSupportActionBar().setTitle("History book");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize the database helper
        db = new DatabaseHelper(this);

        // Find ListView by its ID in the layout
        alist = findViewById(R.id.newlist);

        // Get the user ID from the intent
        userId = MainActivity.userId;

        // Load history data
        getData();

        // Dummy method call (not doing anything in the provided code)
        db.getAllFavorite();
    }

    // Retrieve and display history data
    @SuppressLint("Range")
    public void getData() {
        try {
            // Get history data from the database based on the user ID
            Cursor allHistories = db.getHistories(userId);

            // Check if there is any history data
            if (allHistories.moveToFirst()) {
                do {
                    // Loop through the history data (not doing anything in the provided code)
                } while (allHistories.moveToNext());
            }

            // Create a SimpleCursorAdapter to bind data to the ListView
            ListAdapter myAdapter = new SimpleCursorAdapter(this, R.layout.task_detail_history,
                    allHistories,
                    new String[]{DatabaseHelper.COLUMN_BOOK_ID, DatabaseHelper.COLUMN_BOOK_NAME, DatabaseHelper.COLUMN_HISTORY_TIME},
                    new int[]{R.id.idnum, R.id.c1, R.id.c2}, 0);

            // Set the adapter for the ListView
            alist.setAdapter(myAdapter);
        } catch (Exception ex) {
            // Handle exceptions if any
        }
    }

    // Placeholder method for potential future functionality
    public void test(View view) {
        // Placeholder method
    }
}
