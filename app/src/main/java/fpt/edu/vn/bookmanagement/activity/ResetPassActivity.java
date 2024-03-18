package fpt.edu.vn.bookmanagement.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.model.User;
import fpt.edu.vn.bookmanagement.sql.DatabaseHelper;

public class ResetPassActivity extends AppCompatActivity {

    // Declare EditText fields and DatabaseHelper instance
    EditText oldPass, newPass, confirmNewPass;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout
        setContentView(R.layout.activity_reset_password);

        // Configure the action bar
        getSupportActionBar().setTitle("Change pass");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get intent from the previous activity
        Intent intent = getIntent();

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Find the EditText views by their IDs in the layout
        newPass = findViewById(R.id.newpass1);
        confirmNewPass = findViewById(R.id.newpass2);
    }

    // Method to handle the password change when the user clicks a button
    public void changePassword(View view) {
        // Check if new password or confirmation is empty
        if (newPass.getText().toString().equals("") || confirmNewPass.getText().toString().equals("")) {
            // Display an error message using AlertDialog
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please fill full information");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        // Check if new password matches the confirmation
        if (newPass.getText().toString().equals(confirmNewPass.getText().toString())) {
            // Retrieve user based on the email from the intent
            User user = databaseHelper.getUser(getIntent().getStringExtra("email"));

            // Set the new password for the user
            user.setPassword(newPass.getText().toString());

            // Update the user in the database
            databaseHelper.updateUser(user);

            // Display a success message using AlertDialog
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Change success");
            alertDialog.setMessage(" ");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            // Finish the activity
            finish();
        } else {
            // Display an error message if the passwords do not match
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Password not match");
            alertDialog.setMessage("Confirmation is not match: ");
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
