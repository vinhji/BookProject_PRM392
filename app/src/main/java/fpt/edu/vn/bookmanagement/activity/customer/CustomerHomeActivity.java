package fpt.edu.vn.bookmanagement.activity.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.activity.MainActivity;

public class CustomerHomeActivity extends AppCompatActivity {

    // UI components
    TextView title;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout
        setContentView(R.layout.activity_customer_home);

        // Get the intent
        intent = getIntent();

        // Find the TextView by its ID in the layout
        title = findViewById(R.id.textView);

        // Set the welcome message with the username from MainActivity
        title.setText("Welcome " + MainActivity.userName);
    }

    // Method to show the ReadBookActivity
    public void showReadBook(View view) {
        Intent myIntent = new Intent(this, ReadBookActivity.class);

        // Pass the user ID to the next activity
        myIntent.putExtra("id", intent.getStringExtra("id"));
        this.startActivity(myIntent);
    }

    // Method to show the HistoryBookActivity
    public void showHistory(View view) {
        Intent myIntent = new Intent(this, HistoryBookActivity.class);

        // Pass the user ID to the next activity
        myIntent.putExtra("id", intent.getStringExtra("id"));
        this.startActivity(myIntent);
    }

    // Method to show the FavoriteBookActivity
    public void showFavorite(View view) {
        Intent myIntent = new Intent(this, FavoriteBookActivity.class);

        // Pass the user ID to the next activity
        myIntent.putExtra("id", intent.getStringExtra("id"));
        this.startActivity(myIntent);
    }

    // Method to show the ChangePassActivity
    public void changepass(View view) {
        Intent myIntent = new Intent(this, ChangePassActivity.class);

        // Pass the user ID to the next activity
        myIntent.putExtra("id", intent.getStringExtra("id"));
        this.startActivity(myIntent);
    }

    // Method to handle user logout
    public void logout(View view) {
        // Clear the user ID in MainActivity
        MainActivity.userId = null;

        // Finish the current activity
        finish();
    }
}
