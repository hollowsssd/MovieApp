package com.example.movieapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class manageaccountactivity extends AppCompatActivity {

    TextView changeInformation, changePass;
    ImageView btnback;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageaccount);

        changeInformation = findViewById(R.id.changeInformation);
        changePass = findViewById(R.id.changePass);
        btnback = findViewById(R.id.btnback);
        auth=FirebaseAuth.getInstance();

        btnback.setOnClickListener(V->finish());

        changeInformation.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.pop_up_change_information, null);
            EditText editName = view.findViewById(R.id.editName);

            new AlertDialog.Builder(this)
                    .setTitle("Thay đổi thông tin")
                    .setView(view)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String newName = editName.getText().toString().trim();
                        if (!newName.isEmpty()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(user.getUid())
                                        .update("displayName", newName)
                                        .addOnSuccessListener(aVoid ->
                                                Toast.makeText(this, "Đổi tên thành công", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e ->
                                                Toast.makeText(this, "Lỗi Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                        }
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });


        changePass.setOnClickListener(V->{
        View view = getLayoutInflater().inflate(R.layout.pop_up_change_pass, null);
        EditText editPassNow = view.findViewById(R.id.editPassNow);
        EditText editPassNew = view.findViewById(R.id.editChangePass);
        EditText editPassNewAgain = view.findViewById(R.id.editChangePassAgain);
        Button btnSubmit = view.findViewById(R.id.btnConfirmChangePass);
        TextView btnCancelChangePass = view.findViewById(R.id.btnCancelChangePass);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

            btnCancelChangePass.setOnClickListener(N->dialog.dismiss());

            btnSubmit.setOnClickListener(v -> {
            String passNow = editPassNow.getText().toString().trim();
            String passNew = editPassNew.getText().toString().trim();
            String passNewAgain = editPassNewAgain.getText().toString().trim();

            if (passNow.isEmpty() || passNew.isEmpty() || passNewAgain.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (passNew.length() < 6) {
                Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }


            if (!passNew.equals(passNewAgain)) {
            Toast.makeText(this, "Mật khẩu mới không trùng khớp", Toast.LENGTH_SHORT).show();
            return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), passNow);
                user.reauthenticate(credential)
                        .addOnSuccessListener(aVoid -> {
                            user.updatePassword(passNew)
                                    .addOnSuccessListener(aVoid2 -> {
                                        Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Lỗi cập nhật mật khẩu: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show());
            }
        });

        dialog.show();
        });




    }

}
