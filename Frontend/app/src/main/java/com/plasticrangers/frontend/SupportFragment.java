package com.plasticrangers.frontend;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.content.pm.PackageManager;

import java.util.ArrayList;


public class SupportFragment extends Fragment {

    private EditText phoneNumberField;
    private EditText messageField;
    private Button sendButton;

    // Define an ActivityResultLauncher to handle permission request
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    sendSMS();
                } else {
                    Toast.makeText(getContext(), "Permission to send SMS was denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_support, container, false);

        phoneNumberField = rootView.findViewById(R.id.editTextPhone);
        messageField = rootView.findViewById(R.id.editTextMessage);
        sendButton = rootView.findViewById(R.id.buttonSend);

        phoneNumberField.setText("");
        messageField.setText("");

        sendButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
            } else {
                sendSMS();
            }
        });

        return rootView;
    }

    private void sendSMS() {
        String phoneNumber = phoneNumberField.getText().toString();
        String baseURL = "https://plasticrangers.000webhostapp.com/Q&A.html";
        String message =
                "¡Hola!\n" +
                "\n" +
                "Queremos informarte que ya estamos trabajando para resolver tu problema. Apreciamos tu paciencia y comprensión mientras nos encargamos de todo.\n" +
                "\n" +
                "Mientras tanto, te invitamos a explorar nuestro apartado de Preguntas y Respuestas, donde podrás encontrar mucha información útil y tal vez respuestas a algunas de tus dudas. Es una excelente manera de conocer más sobre nuestras iniciativas y cómo podrías beneficiarte de ellas.\n" +
                "\n" +
                "Visita nuestro apartado de Q&A aquí: " + baseURL +
                "\n" +
                "¡Gracias por tu colaboración y apoyo!";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(message);
            ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
            PendingIntent sentPI = PendingIntent.getBroadcast(getContext(), 0, new Intent("SMS_SENT"), PendingIntent.FLAG_IMMUTABLE);

            for (int i = 0; i < parts.size(); i++) {
                sentIntents.add(sentPI);
            }

            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, null);
            Toast.makeText(getContext(), "SMS enviado correctamente", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al enviar SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
