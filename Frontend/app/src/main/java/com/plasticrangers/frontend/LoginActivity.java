package com.plasticrangers.frontend;

import static com.google.firebase.auth.GoogleAuthProvider.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.app.Activity;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersReference;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(signInAccountTask);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        usersReference = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize Google Sign-In
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, signInOptions);

        // Set click listener for Google Sign-In button
        ImageView googleSignInButton = findViewById(R.id.google_btn);
        googleSignInButton.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> signInAccountTask) {
        try {
            GoogleSignInAccount signInAccount = signInAccountTask.getResult(ApiException.class);
            if (signInAccount != null) {
                firebaseAuthWithGoogle(signInAccount.getIdToken());
            }
        } catch (ApiException e) {
            Log.e("LoginActivity", "Google sign-in fallido", e);
            Toast.makeText(this, "Google sign-in fallido", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            String photoUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null;

                            // Check if user already exists in database
                            usersReference.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // User already exists, do nothing
                                        Toast.makeText(LoginActivity.this, "Bienvenido de nuevo!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // User does not exist, save user data
                                        User userData = new User(name, email, photoUrl, 0);
                                        usersReference.child(name).setValue(userData);
                                        Toast.makeText(LoginActivity.this, "Inicio de Sesión con éxito!", Toast.LENGTH_SHORT).show();

                                    }
                                    navigateToMainActivity();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("LoginActivity", "Error accediendo BBDD: " + error.getMessage());
                                    Toast.makeText(LoginActivity.this, "Error accediendo BBDD", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Log.e("LoginActivity", "Autentificación de Firebase fallida", task.getException());
                        Toast.makeText(LoginActivity.this, "Autentificación de Firebase fallida", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
