package fpt.edu.vn.bookmanagement.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.activity.admin.AdminHomeActivity;
import fpt.edu.vn.bookmanagement.activity.customer.CustomerHomeActivity;
import fpt.edu.vn.bookmanagement.model.User;
import fpt.edu.vn.bookmanagement.sql.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    /// Khai báo các đối tượng giữ thông tin người dùng hiện tại dưới dạng static để truy xuất trong suốt trương chình vì main activity
    //đóng vai trò là cha của các đối tượng
    public static String userId;
    public static String userName;

    public static String userPass;
    public static User currentUser2;
    /// Khai báo các phần tử giao diện
    private EditText user,pass;
    private RadioButton remember;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ///KHởi tạo và gán giá trị cho các đối tượng đã được lưu tữ mật khẩu
        initViews();
        initObjects();
        SharedPreferences sharedPref = this.getSharedPreferences(
                "application", Context.MODE_PRIVATE);
        user.setText(sharedPref.getString("username",null));
        pass.setText(sharedPref.getString("password",null));
    }
    /// Khởi tạo
    private void initViews() {
        user = findViewById(R.id.txtEmail);
        pass = findViewById(R.id.txtCode);
        remember = findViewById(R.id.td_remember);
    }
    //Khởi tạo db
    private void initObjects() {
        db = new DatabaseHelper(this);
        db.getAllUser();
    }
    /// Kiểm tra thông tin đăng nhập<ctrl63
    public  void verifyFromSQLite(View view) {
        //Kiểm tra các ô nhập liệu
        if (!isInputEditTextFilled(user, pass, getString(R.string.error_message_email))) {
            return;
        }
        //Kiểm tra thông tin đăng nhập<ctrl63
        User currentUser = db.checkUser(user.getText().toString().trim()
                , pass.getText().toString().trim());
        //Xử lí đăng nhập
        if (currentUser!=null) {
            //save password nếu được check và remove nếu ko
            if(remember.isChecked()) {
                //Gọi đến sharepreference và lưu trữ theo key
                SharedPreferences sharedPref = this.getSharedPreferences(
                        "application", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                //
                editor.putString("username", currentUser.getEmail());
                editor.putString("password", currentUser.getPassword());
                editor.apply();
            }
            else
            {
                SharedPreferences sharedPref = this.getSharedPreferences(
                        "application", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username","");
                editor.putString("password","");
                editor.apply();
            }
            /// Kiểm tra quyền truy cập
            //Nếu là admin<ct
            if(currentUser.isAdmin()) {
                //Nếu là admin<ct<ct
               Intent myIntent = new Intent(this, AdminHomeActivity.class);
               emptyInputEditText();
               this.startActivity(myIntent);
                SharedPreferences sharedPref = this.getSharedPreferences(
                        "application", Context.MODE_PRIVATE);

                user.setText(sharedPref.getString("username",null));
                pass.setText(sharedPref.getString("password",null));
           }
           else
           {
               //Nếu là customer<ct
               Intent myIntent = new Intent(this, CustomerHomeActivity.class);
               myIntent.putExtra("id",String.valueOf( currentUser.getId()));
               myIntent.putExtra("name", currentUser.getFullName());
               userId = String.valueOf( currentUser.getId());userName= currentUser.getFullName();
               userPass = String.valueOf( currentUser.getPassword());
               currentUser2= currentUser;
               emptyInputEditText();
               this.startActivity(myIntent);
               SharedPreferences sharedPref = this.getSharedPreferences(
                       "application", Context.MODE_PRIVATE);

               user.setText(sharedPref.getString("username",null));
               pass.setText(sharedPref.getString("password",null));
           }
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Login failed");
            alertDialog.setMessage("Please re-check your username and password");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }
    /// Xóa các ô nhập liệu<ctrl63
    private void emptyInputEditText() {
        user.setText(null);
        pass.setText(null);
    }
    /// Kiểm tra các ô nhập liệu<ctrl63
    public boolean isInputEditTextFilled(EditText textInputEditText, EditText textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {

        }
        return true;
    }
    /// Ẩn bàn phím<ctrl63<ctr
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    /// Chuyển sang các activity quên mật hẩu<ctr
    public void ForgotPass(View view)
    {
        Intent myIntent = new Intent(this, ForgotPassActivity.class);
        this.startActivity(myIntent);
    }
    /// Chuyển sang các activity đăng ký<ctr<ctrl6
    public void Register(View view)
    {
        Intent myIntent = new Intent(this, RegisterActivity.class);
        this.startActivity(myIntent);
    }
}