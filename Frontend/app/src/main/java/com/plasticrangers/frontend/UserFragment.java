package com.plasticrangers.frontend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.app.AlertDialog;
import android.widget.Button;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.Random;




public class UserFragment extends Fragment {

    private TextView pointsTextView;
    private View dialogView;
    private String[] ofertas = {"aquopolis", "goikogrill", "mcdonalds", "carlossainzkarts", "mercadona", "kinepolis", "safari", "warner"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Inflate the dialog view
        dialogView = inflater.inflate(R.layout.ofertas_content, null);

        // Initialize views
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView emailTextView = view.findViewById(R.id.emailTextView);
        pointsTextView = view.findViewById(R.id.pointsTextView);
        ImageView profileImageView = view.findViewById(R.id.profileImageView);
        ImageButton logoutButton = view.findViewById(R.id.logoutButton); // Agrega esta línea

        ImageButton canjearButton = view.findViewById(R.id.canjearButton);


        // Set OnClickListener for the logoutButton
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                navigateToLogin();
            }
        });

        canjearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un AlertDialog personalizado
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Ofertas disponibles");

                // Inflate the dialog view
                dialogView = getLayoutInflater().inflate(R.layout.ofertas_content, null);
                builder.setView(dialogView);

                // Botón para cerrar el diálogo
                builder.setPositiveButton("Cerrar", null);

                // Mostrar el AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                // Inicializar y configurar los OnClickListener de los botones después de mostrar el dialogo
                Button oferta1 = dialogView.findViewById(R.id.oferta1);
                Button oferta2 = dialogView.findViewById(R.id.oferta2);
                Button oferta3 = dialogView.findViewById(R.id.oferta3);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String username = currentUser.getDisplayName();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(username);

                userRef.child("points").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int points = dataSnapshot.getValue(Integer.class);

                            if (points < 100) {
                                oferta1.setEnabled(false);
                                oferta2.setEnabled(false);
                                oferta3.setEnabled(false);
                            } else if (points < 250) {
                                oferta1.setEnabled(true);
                                oferta2.setEnabled(false);
                                oferta3.setEnabled(false);
                            } else if (points < 500) {
                                oferta1.setEnabled(true);
                                oferta2.setEnabled(true);
                                oferta3.setEnabled(false);
                            } else {
                                oferta1.setEnabled(true);
                                oferta2.setEnabled(true);
                                oferta3.setEnabled(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Manejar errores de lectura de la base de datos
                    }
                });

                // Añadir OnClickListener para oferta1
                oferta1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Restar 100 puntos al usuario
                        decreasePoints(100);
                        // Generar el código QR
                        String tipo = "Disfruta de un descuento del 10%";
                        generateQRCode(ofertas, tipo);
                    }
                });

                // Añadir OnClickListener para oferta2
                oferta2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Restar 250 puntos al usuario
                        decreasePoints(250);
                        // Generar el código QR
                        String tipo = "Disfruta de un descuento del 25%";
                        generateQRCode(ofertas, tipo);
                    }
                });

                // Añadir OnClickListener para oferta3
                oferta3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Restar 500 puntos al usuario
                        decreasePoints(500);
                        // Generar el código QR
                        String tipo = "Disfruta de un descuento del 50%";
                        generateQRCode(ofertas, tipo);
                    }
                });
            }
        });


        // Obtener referencia a la base de datos de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Obtener referencia al nodo de usuarios en la base de datos
        DatabaseReference userRef = database.getReference("Users");

        // Obtener el nombre de usuario y el email del usuario actual de Firebase
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String username = currentUser.getDisplayName();
            String userEmail = currentUser.getEmail();

            // Set user data into views
            nameTextView.setText(username);
            emailTextView.setText(userEmail);

            // Obtener la referencia al nodo del usuario actual en la base de datos
            assert username != null;
            DatabaseReference currentUserRef = userRef.child(username);

            // Escuchar cambios en la URL de la imagen del usuario
            currentUserRef.child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Obtener la URL de la imagen del usuario
                        String imageUrl = dataSnapshot.getValue(String.class);

                        // Cargar la imagen del usuario en el ImageView usando Glide
                        Glide.with(requireContext())
                                .load(imageUrl)
                                .placeholder(R.drawable.baseline_person_outline_24) // Placeholder mientras se carga la imagen
                                .error(R.drawable.baseline_person_outline_24) // Imagen de error si no se puede cargar la URL
                                .into(profileImageView);
                    } else {
                        // Si no hay URL de imagen en Firebase, puedes establecer una imagen predeterminada
                        profileImageView.setImageResource(R.drawable.baseline_person_outline_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar errores de lectura de la base de datos
                }
            });

            // Escuchar cambios en los puntos del usuario
            currentUserRef.child("points").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Obtener los puntos actuales del usuario
                    int points = dataSnapshot.exists() ? dataSnapshot.getValue(Integer.class) : 0;

                    // Actualizar el TextView de puntos con la puntuación del usuario
                    pointsTextView.setText(String.valueOf(points));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar errores de lectura de la base de datos
                }
            });
        }

        return view;
    }

    private void navigateToLogin() {
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish(); // Si deseas finalizar la actividad actual
    }

    private void decreasePoints(int pointsToDecrease) {
        // Obtener la referencia del usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String username = currentUser.getDisplayName();

            // Obtener referencia a la base de datos de Firebase
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(username);

            // Obtener los puntos actuales del usuario
            userRef.child("points").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        int currentPoints = dataSnapshot.getValue(Integer.class);
                        int newPoints = currentPoints - pointsToDecrease;

                        // Actualizar los puntos del usuario en Firebase
                        userRef.child("points").setValue(newPoints);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar errores de lectura de la base de datos
                }
            });
        }
    }

    private void generateQRCode(String[] ofertas, String tipo) {
        // Obtener una oferta aleatoria del array
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        int randomIndex = random.nextInt(ofertas.length);
        tipo += " en ";
        tipo += ofertas[randomIndex];
        String data = ofertas[randomIndex] + ".com";

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            // Mostrar el código QR
            showQRCode(bitmap, tipo);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private void showQRCode(Bitmap bitmap, String tipo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        ImageView imageView = new ImageView(requireContext());
        imageView.setImageBitmap(bitmap);
        builder.setView(imageView);
        builder.setTitle("No cierres esta ventana hasta canjear el descuento.");
        builder.setMessage(tipo);

        // Agregar OnClickListener al botón "Cerrar"
        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Cierra el diálogo actual

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        builder.create().show();
    }

}
