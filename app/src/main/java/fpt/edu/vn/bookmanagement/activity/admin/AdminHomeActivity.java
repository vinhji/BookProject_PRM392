package fpt.edu.vn.bookmanagement.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.activity.MainActivity;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout
        setContentView(R.layout.activity_admin_home);

        // Uncomment the following line if you have a toolbar and want to set it as the support action bar
        // setSupportActionBar(new Toolbar(this));
    }

    // Method to show the Manage Category activity
    public void showManagCategory(View view) {
        // Create an Intent to start the ManageCategoryActivity
        Intent myIntent = new Intent(this, ManageCategoryActivity.class);

        // Start the ManageCategoryActivity
        this.startActivity(myIntent);
    }

    // Method to show the Manage Book activity
    public void showManageBook(View view) {
        // Create an Intent to start the ManageBookActivity
        Intent myIntent = new Intent(this, ManageBookActivity.class);

        // Start the ManageBookActivity
        this.startActivity(myIntent);
    }

    // Method to show the Search Book activity
    public void showSearchCategory(View view) {
        // Create an Intent to start the SearchBookActivity
        Intent myIntent = new Intent(this, SearchBookActivity.class);

        // Start the SearchBookActivity
        this.startActivity(myIntent);
    }

    // Method to perform logout
    public void logout(View view) {
        // Set the userId in the MainActivity to null
        MainActivity.userId = null;

        // Finish the current activity (AdminHomeActivity)
        finish();
    }
}
