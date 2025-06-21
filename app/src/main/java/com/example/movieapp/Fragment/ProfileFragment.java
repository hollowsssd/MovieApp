package com.example.movieapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movieapp.Activities.LoginActivity;
import com.example.movieapp.Activities.RegisActivity;
import com.example.movieapp.Activities.favoriteactivity;
import com.example.movieapp.Activities.manageaccountactivity;
import com.example.movieapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    TextView logout, nameTextView, emailTextView,tvFavorite;

    LinearLayout manage_account;

    FirebaseFirestore db;


    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameTextView= view.findViewById(R.id.user_name);
        emailTextView= view.findViewById(R.id.user_email);
        logout= view.findViewById(R.id.logout);
        manage_account = view.findViewById(R.id.manage_account);
        tvFavorite = view.findViewById(R.id.tvFavorite);
        db = FirebaseFirestore.getInstance();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("displayName");
                            String email = currentUser.getEmail();

                            // Cập nhật UI
                            nameTextView.setText(name);
                            emailTextView.setText(email);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Lỗi khi lấy dữ liệu
                        Toast.makeText(getContext(), "Lỗi khi tải thông tin người dùng", Toast.LENGTH_SHORT).show();
                    });
        }


        Bundle args = getArguments();
        if (args != null) {
            String name = args.getString("name");
            String email = args.getString("email");

            nameTextView.setText(name);
            emailTextView.setText(email);
        }
    

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Bạn đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),LoginActivity.class));
            }
        });

        manage_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), manageaccountactivity.class));
            }
        });

        tvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), favoriteactivity.class));
            }
        });

    }
}
