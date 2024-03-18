package fpt.edu.vn.bookmanagement.activity.customer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.activity.MainActivity;
import fpt.edu.vn.bookmanagement.model.User;
import fpt.edu.vn.bookmanagement.sql.DatabaseHelper;

public class ChangePassActivity extends AppCompatActivity {

    // UI components
    EditText oldPass, newPass, confirmNewPass;

    // Database helper instance
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view to the specified layout
        setContentView(R.layout.activity_update_password);

        // Configure the action bar
        getSupportActionBar().setTitle("Change pass");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Find UI elements by their IDs in the layout
        oldPass = findViewById(R.id.oldpass);
        newPass = findViewById(R.id.newpass1);
        confirmNewPass = findViewById(R.id.newpass2);
    }

    // Method to handle password change
    public void changePassword(View view) {
        if (oldPass.getText().toString().equals("") || newPass.getText().toString().equals("") || confirmNewPass.getText().toString().equals("")) {
            // Display an error message if any field is empty
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please fill in all the information");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }

        if (newPass.getText().toString().equals(confirmNewPass.getText().toString())) {
            if (oldPass.getText().toString().equals(MainActivity.userPass)) {
                // If the old password matches the current password, proceed with the password change
                User newUser = MainActivity.currentUser2;
                newUser.setPassword(newPass.getText().toString());
                databaseHelper.updateUser(newUser);

                // Display a success message using AlertDialog
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Change success");
                alertDialog.setMessage("");
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
                // Display an error message if the old password does not match
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Password old not match");
                alertDialog.setMessage("");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        } else {
            // Display an error message if the new password and confirmation do not match
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Password not match");
            alertDialog.setMessage("Confirmation does not match");
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
