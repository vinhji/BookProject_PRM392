package fpt.edu.vn.bookmanagement.activity.customer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.activity.MainActivity;
import fpt.edu.vn.bookmanagement.model.Category;
import fpt.edu.vn.bookmanagement.sql.DatabaseHelper;

public class ReadBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Database helper instance for interacting with the database
    DatabaseHelper db;

    // UI components
    ListView alist;
    Spinner listCategory;

    // List of categories
    List<Category> categoryList;

    // User ID for retrieving favorite books
    private String userId;

    // Current selected category ID
    private String currentCategory = "";

    // Current selected book ID
    private String currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout
        setContentView(R.layout.activity_customer_readbook);

        // Set the action bar title and enable the back button
        getSupportActionBar().setTitle("Search book");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize the database helper
        db = new DatabaseHelper(this);

        try {
            // Get the list of all categories
            categoryList = db.getAllCategory();

            // If no categories found, show an error message
            if (categoryList.size() == 0)
                throw new Exception("No category found. Please create category first!");
        } catch (Exception ex) {
            // Display an error dialog if an exception occurs
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage(ex.getMessage());
            alertDialog.show();
        }

        // Find Spinner by its ID in the layout
        listCategory = findViewById(R.id.category);

        // Initialize an array of category names for the Spinner
        String[] arraySpinner;
        arraySpinner = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++)
            arraySpinner[i] = categoryList.get(i).getName();

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter ad = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                arraySpinner);

        // Set the item selection listener for the Spinner
        listCategory.setOnItemSelectedListener(this);

        // Set the layout resource for the drop-down views
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the Spinner
        listCategory.setAdapter(ad);

        // Find ListView by its ID in the layout
        alist = findViewById(R.id.newlist);

        // Get the user ID from the intent
        userId = MainActivity.userId;

        // Load favorite books data
        db.getAllFavorite();
    }

    // Handle item selection in the Spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentCategory = String.valueOf(categoryList.get(position).getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing when nothing is selected
    }

    // Retrieve and display books data based on the selected category
    @SuppressLint("Range")
    public void getData(View view) {
        try {
            // Get books data from the database based on the selected category
            Cursor allCategories = db.getBooksFavor(currentCategory);

            // Create a SimpleCursorAdapter to bind data to the ListView
            ListAdapter myAdapter = new SimpleCursorAdapter(this, R.layout.task_detai_readl,
                    allCategories,
                    new String[]{DatabaseHelper.COLUMN_BOOK_ID, DatabaseHelper.COLUMN_BOOK_NAME, DatabaseHelper.COLUMN_BOOK_DESCRIPTION, DatabaseHelper.COLUMN_BOOK_CONTENT, DatabaseHelper.COLUMN_BOOK_CATEGORY_ID, "is_favorite"},
                    new int[]{R.id.idnum, R.id.c1, R.id.c2, R.id.c3, R.id.c4, R.id.button8}, 0);

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

    // Handle the "Read" button click event
    public void read(View view) {
        View parent = ((LinearLayout) view.getParent().getParent());
        currentId = (String) ((TextView) parent.findViewById(R.id.idnum)).getText();

        // Add book to history if the book ID and user ID are not null
        if (currentId != null && MainActivity.userId != null) {
            db.addHistory(currentId, MainActivity.userId);
        }

        // Start the DetailBookActivity and pass the book ID as an extra
        Intent intent = new Intent(this, DetailBookActivity.class);
        intent.putExtra("id", currentId);
        startActivity(intent);
    }

    // Handle the "Set Favorite" or "Remove Favorite" button click event
    public void setFavorite(View view) {
        if (((Button) view).getText().toString().equals("Set Favorite")) {
            View parent = ((LinearLayout) view.getParent().getParent());
            currentId = (String) ((TextView) parent.findViewById(R.id.idnum)).getText();

            // Add book to favorites if the book ID and user ID are not null
            if (currentId != null && MainActivity.userId != null)
                db.addFavourite(currentId, MainActivity.userId);
            else {
                // Display an error dialog if the book ID or user ID is null
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Set failed");
                alertDialog.setMessage("Please re-check ");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

            // Update the button text and display a success dialog
            ((Button) view).setText("Remove Favorite");
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Set success");
            alertDialog.setMessage("OK");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else {
            View parent = ((LinearLayout) view.getParent().getParent());
            currentId = (String) ((TextView) parent.findViewById(R.id.idnum)).getText();

            // Remove book from favorites if the book ID and user ID are not null
            if (currentId != null && MainActivity.userId != null) {
                db.deleteFavorite(currentId, MainActivity.userId);

                // Update the button text and display a success dialog
                ((Button) view).setText("Set Favorite");
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Remove success");
                alertDialog.setMessage("OK");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                // Display an error dialog if the book ID or user ID is null
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Remove failed");
                alertDialog.setMessage("Please re-check ");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }
}
