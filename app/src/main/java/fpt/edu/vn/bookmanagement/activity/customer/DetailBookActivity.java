package fpt.edu.vn.bookmanagement.activity.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.model.Book;
import fpt.edu.vn.bookmanagement.sql.DatabaseHelper;

public class DetailBookActivity extends AppCompatActivity {

    // UI components
    TextView title;
    TextView content;

    // Database helper instance for interacting with the database
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout
        setContentView(R.layout.activity_customer_detail);

        // Set the action bar title and enable the back button
        getSupportActionBar().setTitle("Detail book");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get the intent that started this activity
        Intent intent = getIntent();

        // Find TextViews by their IDs in the layout
        title = findViewById(R.id.textView5);
        content = findViewById(R.id.textView6);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        // Get the book details based on the ID passed in the intent
        Book newBook = databaseHelper.getBooksById(intent.getStringExtra("id"));

        // Set the title and content TextViews with the book information
        title.setText("Book id:" + newBook.getId());
        content.setText("Content:" + newBook.getContent());
    }
}
