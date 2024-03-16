package fpt.edu.vn.bookmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.bookmanagement.R;
import fpt.edu.vn.bookmanagement.model.SendMail;
import fpt.edu.vn.bookmanagement.sql.DatabaseHelper;

public class ForgotPassActivity extends AppCompatActivity {

    /// Khai báo đối tượng Edittext email và code
    EditText txtEmail,txtCode;
    // Khái báo đối tượng db helper dùng để thao tác với sqllite
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set giao diện cho activity
        setContentView(R.layout.activity_forgotpass);
        /// Cài đặt actionBar title và nút back
        getSupportActionBar().setTitle("Change pass");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /// Init các phần tử Edit text
        txtEmail = findViewById(R.id.txtEmail);
        txtCode = findViewById(R.id.txtCode);
    }
    /// Code cho verification
    private  String code= "default";
    /// Hàm sự kiện onclick của gửi mã xác nhận
    public void sendMail(View view)
    {
        /// Nếu email nhập vào rỗng thì trả về
        if(txtEmail.getText().toString().equals(""))
            return;
        /// Xử lí nếu email không tồn tại thì trả về
        databaseHelper = new DatabaseHelper(this);
        if(databaseHelper.getUser(txtEmail.getText().toString())==null)
        {
            Toast.makeText(this, "Email not exist", Toast.LENGTH_SHORT).show();
            return;
        }
        /// Gửi email cho người dùng<ctr
        String email = txtEmail.getText().toString();
        String subject ="[RESET PASSWORD] Refresh password code from Book Management";
        /// Generate code tự động có 6 chữ và để trong bộ nhớ tạm
        code =getAlphaNumericString(6);
        String message = "Your verification code is: "+code;
        SendMail sm = new SendMail(this, email, subject, message);
        sm.execute();
        /// kết thúc ham
    }
    /// Hàm kiểm tra code
    public void verifyCode(View view)
    {
        // Nếu editext rỗng thì tra về
        if(txtCode.getText().toString().equals(""))
            return;
        /// Nếu chuẩn code thì chuyển sang reset pass<c
        if(txtCode.getText().toString().equals(code))
        {
            // Gọi activity reset pass
           Intent intent = new Intent(this, ResetPassActivity.class);
            intent.putExtra("email",txtEmail.getText().toString());
            startActivity(intent);
            // kết thúc sau khi reset kết thúc
            finish();
        }
        else
        {
            Toast.makeText(this, "Invalid code", Toast.LENGTH_SHORT).show();
        }
    }
    /// Hàm reandom code
    static String getAlphaNumericString(int n)
    {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}