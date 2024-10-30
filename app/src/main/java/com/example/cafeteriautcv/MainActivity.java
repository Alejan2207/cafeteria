package com.example.cafeteriautcv;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPedido;
    private TextView textViewPedidos;
    private TextView textViewHistorial;
    private TextView textViewTotalPedidos;
    private List<List<String>> historialPedidos;

    private static final int MAX_PEDIDO_LENGTH = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextPedido = findViewById(R.id.editTextPedido);
        Button btnCheck = findViewById(R.id.btnCheck);
        textViewPedidos = findViewById(R.id.textViewPedidos);
        textViewHistorial = findViewById(R.id.textViewHistorial);
        textViewTotalPedidos = findViewById(R.id.textViewTotalPedidos);

        historialPedidos = new ArrayList<>();

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarPedidos();
            }
        });
    }

    private void registrarPedidos() {
        String entradaPedidos = editTextPedido.getText().toString().trim().toLowerCase();

        if (entradaPedidos.isEmpty()) {
            Toast.makeText(this, "Ingresa al menos un pedido", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] pedidos = entradaPedidos.split(",");
        HashSet<String> pedidoActual = new HashSet<>(); // Utilizamos un HashSet para evitar duplicados en el mismo registro

        for (String pedido : pedidos) {
            pedido = pedido.trim();
            if (pedido.isEmpty()) continue;

            if (esPedidoValido(pedido)) {
                pedidoActual.add(pedido); // Añadimos solo pedidos válidos
            } else {
                Toast.makeText(this, "El pedido '" + pedido + "' es inválido o demasiado largo", Toast.LENGTH_SHORT).show();
            }
        }

        if (!pedidoActual.isEmpty()) {
            historialPedidos.add(new ArrayList<>(pedidoActual)); // Agrega el nuevo grupo de pedidos al historial
            Toast.makeText(this, "Pedidos registrados", Toast.LENGTH_SHORT).show();
            actualizarPedidos();
            actualizarHistorial();
            editTextPedido.setText("");
        } else {
            Toast.makeText(this, "No se agregaron pedidos válidos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean esPedidoValido(String pedido) {
        return pedido.length() <= MAX_PEDIDO_LENGTH && Pattern.matches("[a-zA-Z0-9 ]+", pedido);
    }

    private void actualizarPedidos() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pedidos registrados:\n");

        for (List<String> pedidos : historialPedidos) {
            sb.append("- ").append(pedidos.toString()).append("\n");
        }

        textViewPedidos.setText(sb.toString());
    }

    private void actualizarHistorial() {
        StringBuilder sb = new StringBuilder();
        sb.append("Historial de Pedidos:\n");

        int index = 1;
        for (List<String> pedidos : historialPedidos) {
            sb.append("Pedido ").append(index++).append(": ").append(pedidos).append("\n");
        }

        textViewHistorial.setText(sb.toString());
        textViewTotalPedidos.setText("Total de pedidos únicos: " + historialPedidos.size());
    }
}
