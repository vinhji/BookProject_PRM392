package fpt.edu.vn.bookmanagement.activity.admin;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.model.Category;
import fpt.edu.vn.bookmanagement.sql.DatabaseHelper;

public class SearchBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // DatabaseHelper instance
    DatabaseHelper db;

    // UI components
    ListView alist;
    Spinner listCategory;

    // List to store Category objects
    List<Category> categoryList;

    // Variable to store the currently selected category ID
    private String currentCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout
        setContentView(R.layout.activity_admin_searchbook);

        // Configure the action bar
        getSupportActionBar().setTitle("Search book");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize DatabaseHelper
        db = new DatabaseHelper(this);

        // Attempt to retrieve all categories from the database
        try {
            categoryList = db.getAllCategory();

            // Check if there are no categories and display an error message
            if (categoryList.size() == 0)
                throw new Exception("No category found. Please create categories first!");
        } catch (Exception ex) {
            // Display an error message if an exception occurs
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage(ex.getMessage());
            alertDialog.show();
        }

        // Find UI elements by their IDs in the layout
        listCategory = findViewById(R.id.category);
        alist = findViewById(R.id.newlist);

        // Create an array of category names to populate the Spinner
        String[] arraySpinner = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++)
            arraySpinner[i] = categoryList.get(i).getName();

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter ad = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                arraySpinner);

        // Set the Spinner's item selected listener to this activity
        listCategory.setOnItemSelectedListener(this);

        // Set the drop-down view resource for the ArrayAdapter
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter for the Spinner
        listCategory.setAdapter(ad);
    }

    // Method called when an item in the Spinner is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Store the ID of the selected category
        currentCategory = String.valueOf(categoryList.get(position).getId());
    }

    // Method called when no item in the Spinner is selected
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing in this case
    }

    // Method to retrieve data based on the selected category and populate the ListView
    public void getData(View view) {
        try {
            // Get books from the database based on the selected category
            Cursor allCategories = db.getBooksBy(currentCategory);

            // Create a SimpleCursorAdapter to map data to ListView
            ListAdapter myAdapter = new SimpleCursorAdapter(this, R.layout.task_detail,
                    allCategories,
                    new String[]{DatabaseHelper.COLUMN_BOOK_ID, DatabaseHelper.COLUMN_BOOK_NAME, DatabaseHelper.COLUMN_BOOK_DESCRIPTION, DatabaseHelper.COLUMN_BOOK_CONTENT, DatabaseHelper.COLUMN_BOOK_CATEGORY_ID},
                    new int[]{R.id.idnum, R.id.c1, R.id.c2, R.id.c5, R.id.c4}, 0);

            // Set the adapter for the ListView
            alist.setAdapter(myAdapter);
        } catch (Exception ex) {
            // Handle exceptions, if any
        }
    }
}
