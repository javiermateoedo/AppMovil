package com.plasticrangers.frontend;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.plasticrangers.frontend.databinding.ActivityDetailedBinding;

public class DetailedActivity extends AppCompatActivity {

    ActivityDetailedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = this.getIntent();
        if (intent != null){
            // Asegúrate de que estas claves coinciden con las que usaste para enviar los datos.
            String name = intent.getStringExtra("name");
            String link = intent.getStringExtra("link");
            int image = intent.getIntExtra("image", 0);  // Default a una imagen genérica si no se pasa ninguna.
            String descripcion = intent.getStringExtra("desc");
            binding.detailName.setText(name);
            binding.detailImage.setImageResource(image);
            binding.detailDescription.setText(descripcion);

            // Establecer el enlace al botón para abrir en el navegador
            binding.detailButton.setOnClickListener(v -> {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(link));
                startActivity(webIntent);
            });
        }
    }
}
