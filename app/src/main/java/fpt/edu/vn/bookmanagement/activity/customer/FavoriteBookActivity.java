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

public class FavoriteBookActivity extends AppCompatActivity {

    // Database helper instance for interacting with the database
    DatabaseHelper db;

    // UI components
    ListView alist;
    Spinner listCategory;

    // List of categories
    List<Category> categoryList;

    // User ID for retrieving favorite books
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout
        setContentView(R.layout.activity_customer_favoritebook);

        // Set the action bar title and enable the back button
        getSupportActionBar().setTitle("Favorite book");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize the database helper
        db = new DatabaseHelper(this);

        // Find ListView by its ID in the layout
        alist = findViewById(R.id.newlist);

        // Get the user ID from the intent
        userId = MainActivity.userId;

        // Load favorite books data
        getData();
    }

    // Retrieve and display favorite books data
    @SuppressLint("Range")
    public void getData() {
        try {
            // Get favorite books data from the database based on the user ID
            Cursor allHistories = db.getBookbyUserFavor(userId);

            // Create a SimpleCursorAdapter to bind data to the ListView
            ListAdapter myAdapter = new SimpleCursorAdapter(this, R.layout.tasks,
                    allHistories,
                    new String[]{DatabaseHelper.COLUMN_BOOK_ID, DatabaseHelper.COLUMN_BOOK_NAME, DatabaseHelper.COLUMN_BOOK_DESCRIPTION},
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
