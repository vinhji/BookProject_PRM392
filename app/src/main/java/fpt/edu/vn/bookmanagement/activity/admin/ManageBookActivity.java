package fpt.edu.vn.bookmanagement.activity.admin;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.model.Category;
import fpt.edu.vn.bookmanagement.sql.DatabaseHelper;

public class ManageBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // DatabaseHelper instance
    DatabaseHelper db;

    // UI components
    EditText txtName, txtDesc, txtContent;
    ListView alist;
    Spinner listCategory;

    // List to store categories
    List<Category> categoryList;

    // Current selected category ID
    private String currentCategory = "";

    // Current selected book ID
    private String currentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout
        setContentView(R.layout.activity_admin_book);

        // Configure the action bar
        getSupportActionBar().setTitle("Manage book");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize DatabaseHelper
        db = new DatabaseHelper(this);

        try {
            // Get all categories from the database
            categoryList = db.getAllCategory();

            // Check if there are no categories, show an error message
            if (categoryList.size() == 0)
                throw new Exception("No category found. Please create a category first!");
        } catch (Exception ex) {
            // Display an error dialog if an exception occurs
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage(ex.getMessage());
            alertDialog.show();
        }

        // Find UI elements by their IDs in the layout
        listCategory = findViewById(R.id.category);
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDesc);
        txtContent = findViewById(R.id.txtContent);
        alist = findViewById(R.id.newlist);

        // Set up the category spinner
        String[] arraySpinner;
        arraySpinner = new String[categoryList.size()];
        for (int i = 0; i < categoryList.size(); i++)
            arraySpinner[i] = categoryList.get(i).getName();

        ArrayAdapter ad = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                arraySpinner);
        listCategory.setOnItemSelectedListener(this);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listCategory.setAdapter(ad);

        // Get data and populate the ListView
        getData();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Update the current category when an item is selected
        currentCategory = String.valueOf(categoryList.get(position).getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Handle the case when nothing is selected in the spinner
    }

    // Method to add a new book
    public void addBook(View view) {
        // Get input values from the EditText fields
        String s1 = txtName.getText().toString();
        String s2 = txtDesc.getText().toString();
        String s3 = txtContent.getText().toString();

        // Check if any input field is empty
        if (s1.trim().equals("") || s2.trim().equals("") || s3.trim().equals("")) {
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

        // Check if the book with the given name already exists
        if (!db.checkBookExist(s1))
            db.addBook(s1, currentCategory, s2, s3);
        else {
            // Display an error message if the book name already exists
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
        txtContent.setText("");

        // Display a success message using AlertDialog
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Create success");
        alertDialog.setMessage("Success in creating book name: " + s1);
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
        // Get all books from the database
        Cursor allCategories = db.getBooks();

        // Create a SimpleCursorAdapter to map data to ListView
        ListAdapter myAdapter = new SimpleCursorAdapter(this, R.layout.tasks,
                allCategories,
                new String[]{DatabaseHelper.COLUMN_BOOK_ID, DatabaseHelper.COLUMN_BOOK_NAME, DatabaseHelper.COLUMN_BOOK_DESCRIPTION, DatabaseHelper.COLUMN_BOOK_CONTENT, DatabaseHelper.COLUMN_BOOK_CATEGORY_ID},
                new int[]{R.id.idnum, R.id.c1, R.id.c2, R.id.c3, R.id.c4}, 0);
        alist.setAdapter(myAdapter);
    }

    // Method to handle item selection in the ListView
    public void test(View view) {
        // Get the selected book's ID and set the corresponding values in the EditText fields
        currentId = (String) ((TextView) view.findViewById(R.id.idnum)).getText();
        txtName.setText(((TextView) view.findViewById(R.id.c1)).getText());
        txtDesc.setText(((TextView) view.findViewById(R.id.c2)).getText());
        txtContent.setText(((TextView) view.findViewById(R.id.c3)).getText());

        try {
            // Get the category name and set the selection in the spinner
            String value = categoryList.stream().filter(x -> x.getId() == Integer.parseInt((String) ((TextView) view.findViewById(R.id.c4)).getText())).toList().get(0).getName();
            int index = getIndex(listCategory, value);
            listCategory.setSelection(index);
        } catch (Exception ex) {
            // Handle exceptions, if any
            ex.printStackTrace();
        }
    }

    // Method to delete the selected book
    public void deleteBook(View view) {
        try {
            if (!currentId.equals("")) {
                // Delete the book from the database
                db.deleteBook(Integer.parseInt(currentId));

                // Display a success message using AlertDialog
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Delete success");
                alertDialog.setMessage("Success in deleting book id: " + currentId);
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
                txtContent.setText("");
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

    // Method to update the selected book
    public void updateBook(View view) {
        // Get input values from the EditText fields
        String s1 = txtName.getText().toString();
        String s2 = txtDesc.getText().toString();
        String s3 = txtContent.getText().toString();

        // Check if any input field is empty
        if (s1.trim().equals("") || s2.trim().equals("") || s3.trim().equals("")) {
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

        // Update the book in the database
        db.updateBook(currentId, s1, currentCategory, s2, s3);

        // Clear the input fields
        txtName.setText("");
        txtDesc.setText("");
        txtContent.setText("");

        // Display a success message using AlertDialog
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Update success");
        alertDialog.setMessage("Update in book name: " + s1);
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

    // Method to get the index of an item in the Spinner
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }
}
