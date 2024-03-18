package fpt.edu.vn.bookmanagement.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.model.User;
import fpt.edu.vn.bookmanagement.sql.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    /// Khởi tạo các ối tượng edittext
    EditText txtEmail, txtPassword, txtName;
    // Khởi tạo các đối tượng thao tác db
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set giao diện
        setContentView(R.layout.activity_register);
        // Init các đối tượng
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPass);
        txtName = findViewById(R.id.txtName);
        databaseHelper = new DatabaseHelper(this);

    }
    /// Hàm xử lĩ sụ kiện đăng kí
    public void register(View view){
        // Nếu các ô rỗng thì trả về
        if(txtEmail.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()|| txtName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        /// Nếu email đã tồn tại trhif trả về
        if(databaseHelper.getUser(txtEmail.getText().toString())!=null){
            Toast.makeText(this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }
        /// Khai báo đối tượng User mới từ thông tin
        User user = new User();
        user.setName(txtName.getText().toString());
        user.setEmail(txtEmail.getText().toString());
        user.setFullName(txtName.getText().toString());
        user.setPassword(txtPassword.getText().toString());
        user.setAdmin(false);
        //// Add đối tượng vào db
        databaseHelper.addUser(user);
        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
        ////Trở về màn hình login
        finish();
    }

}