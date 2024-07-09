package com.plasticrangers.frontend;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
public class RecycleFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle, container, false);

        // Obtener referencias a los botones
        Button metalButton = view.findViewById(R.id.metal);
        Button vidrioButton = view.findViewById(R.id.vidrio);
        Button plasticoButton = view.findViewById(R.id.plastico);
        Button papelButton = view.findViewById(R.id.papel);

        // Establecer OnClickListener a cada botón
        metalButton.setOnClickListener(this);
        vidrioButton.setOnClickListener(this);
        plasticoButton.setOnClickListener(this);
        papelButton.setOnClickListener(this);

        // Obtener referencia a la base de datos de Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Obtener referencia al nodo de usuarios en la base de datos
        userRef = database.getReference("Users");

        return view;
    }

    @Override
    public void onClick(View v) {
        int pointsToAdd = 0;

        // Determinar la cantidad de puntos a añadir según el botón presionado
        switch (v.getId()) {
            case R.id.metal:
                pointsToAdd = 5;
                break;
            case R.id.vidrio:
                pointsToAdd = 3;
                break;
            case R.id.plastico:
                pointsToAdd = 2;
                break;
            case R.id.papel:
                pointsToAdd = 4;
                break;
        }

        // Incrementar la puntuación del usuario actual
        incrementUserPoints(pointsToAdd);
    }

    private void incrementUserPoints(int pointsToAdd) {
        // Obtener el usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Verificar si el usuario está autenticado
        if (currentUser != null) {
            // Obtener el nombre de usuario del usuario actual
            String username = currentUser.getDisplayName();

            // Verificar si se obtuvo el nombre de usuario
            if (username != null) {
                // Obtener la referencia al nodo del usuario en la base de datos
                DatabaseReference currentUserRef = userRef.child(username);

                // Obtener la puntuación actual del usuario y actualizarla
                currentUserRef.child("points").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Obtener la puntuación actual del usuario
                        int currentPoints = dataSnapshot.exists() ? dataSnapshot.getValue(Integer.class) : 0;
                        // Calcular la nueva puntuación
                        int newPoints = currentPoints + pointsToAdd;

                        // Actualizar la puntuación del usuario en la base de datos
                        currentUserRef.child("points").setValue(newPoints)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Mostrar un mensaje Toast para indicar que se han sumado los puntos
                                        Toast.makeText(requireContext(), "Se han añadido " + pointsToAdd + " puntos", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Manejar errores al actualizar la puntuación del usuario
                                        Toast.makeText(requireContext(), "Error al actualizar los puntos del usuario", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Manejar errores de lectura de la base de datos
                        Toast.makeText(requireContext(), "Error al obtener los puntos del usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Manejar el caso en que no se pudo obtener el nombre de usuario
                Toast.makeText(requireContext(), "No se pudo obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

