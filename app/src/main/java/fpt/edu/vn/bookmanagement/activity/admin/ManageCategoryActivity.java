package fpt.edu.vn.bookmanagement.activity.admin;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.sql.DatabaseHelper;

public class ManageCategoryActivity extends AppCompatActivity {

    // DatabaseHelper instance
    DatabaseHelper db;

    // UI components
    EditText txtName, txtDesc;
    ListView alist;

    // Variable to store the current selected category ID
    String currentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout
        setContentView(R.layout.activity_admin_category);

        // Configure the action bar
        getSupportActionBar().setTitle("Manage category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize DatabaseHelper
        db = new DatabaseHelper(this);

        // Find UI elements by their IDs in the layout
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDesc);
        alist = findViewById(R.id.newlist);

        // Get data and populate the ListView
        getData();
    }

    // Method to add a new category
    public void addNew(View view) {
        // Get input values from the EditText fields
        String s1 = txtName.getText().toString();
        String s2 = txtDesc.getText().toString();

        // Check if any input field is empty
        if (s1.trim().equals("") || s2.trim().equals("")) {
            // Display an error message using AlertDialog
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Create failed");
            alertDialog.setMessage("Please enter full information");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        // Check if the category with the given name already exists
        if (!db.checkCategoryExist(s1))
            db.addCategory(s1, s2);
        else {
            // Display an error message if the category name already exists
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Create failed");
            alertDialog.setMessage("Name exists");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        // Clear the input fields
        txtName.setText("");
        txtDesc.setText("");

        // Display a success message using AlertDialog
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Create success");
        alertDialog.setMessage("Success in creating category name: " + s1);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        // Update the ListView
        getData();
    }

    // Method to delete the selected category
    public void deleteCategory(View view) {
        try {
            // Check if a category is selected for deletion
            if (!currentId.equals("")) {
                // Delete the category from the database
                db.deleteCategory(Integer.parseInt(currentId));

                // Display a success message using AlertDialog
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Delete success");
                alertDialog.setMessage("Success in deleting category id: " + currentId);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                // Clear the input fields
                txtName.setText("");
                txtDesc.setText("");
                currentId = "";

                // Update the ListView
                getData();
            } else
                throw new RuntimeException("");
        } catch (Exception ex) {
            // Display an error message if an exception occurs
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Delete failed");
            alertDialog.setMessage("ID does not exist");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    // Method to update the selected category
    public void updateCourse(View view) {
        // Get input values from the EditText fields
        String s1 = txtName.getText().toString();
        String s2 = txtDesc.getText().toString();

        // Check if any input field is empty
        if (s1.trim().equals("") || s2.trim().equals("")) {
            // Display an error message using AlertDialog
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Update failed");
            alertDialog.setMessage("Please enter full information");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        // Update the category in the database
        db.updateCategory(Integer.parseInt(currentId), s1, s2);

        // Clear the input fields
        txtName.setText("");
        txtDesc.setText("");

        // Display a success message using AlertDialog
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Update success");
        alertDialog.setMessage("Update in category name: " + s1);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        // Update the ListView
        getData();
    }

    // Method to retrieve data and populate the ListView
    public void getData() {
        // Get all categories from the database
        Cursor allCategories = db.getCategories();

        // Create a SimpleCursorAdapter to map data to ListView
        ListAdapter myAdapter = new SimpleCursorAdapter(this, R.layout.tasks,
                allCategories,
                new String[]{DatabaseHelper.COLUMN_CATEGORY_ID, DatabaseHelper.COLUMN_CATEGORY_NAME, DatabaseHelper.COLUMN_CATEGORY_DESCRIPTION},
                new int[]{R.id.idnum, R.id.c1, R.id.c2}, 0);

        // Set the adapter for the ListView
        alist.setAdapter(myAdapter);
    }

    // Method to handle item selection in the ListView for testing purposes
    public void test(View view) {
        // Get the selected category's ID and set the corresponding values in the input fields
        currentId = (String) ((TextView) view.findViewById(R.id.idnum)).getText();
        txtName.setText(((TextView) view.findViewById(R.id.c1)).getText());
        txtDesc.setText(((TextView) view.findViewById(R.id.c2)).getText());
    }
}
