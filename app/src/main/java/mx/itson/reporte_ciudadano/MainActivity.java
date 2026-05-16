package mx.itson.reporte_ciudadano;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ScrollView layoutReporte;
    private ScrollView layoutContacto;

    private BottomNavigationView bottomNavigation;

    private EditText etNombre;
    private EditText etDireccion;
    private EditText etCelular;
    private EditText etCorreo;
    private EditText etDescripcion;

    private Spinner spinnerColonia;
    private Spinner spinnerTipoReporte;

    private Button btnSeleccionarImagen;
    private Button btnEnviarReporte;

    private Button btnMapa;
    private Button btnCorreo;
    private Button btnLlamar;

    private ImageView ivEvidencia;

    private Uri imagenSeleccionadaUri;

    private final ActivityResultLauncher<String> abrirGaleria =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri uri) {

                            if (uri != null) {
                                imagenSeleccionadaUri = uri;
                                ivEvidencia.setImageURI(uri);
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutReporte = findViewById(R.id.layoutReporte);
        layoutContacto = findViewById(R.id.layoutContacto);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        etNombre = findViewById(R.id.etNombre);
        etDireccion = findViewById(R.id.etDireccion);
        etCelular = findViewById(R.id.etCelular);
        etCorreo = findViewById(R.id.etCorreo);
        etDescripcion = findViewById(R.id.etDescripcion);

        spinnerColonia = findViewById(R.id.spinnerColonia);
        spinnerTipoReporte = findViewById(R.id.spinnerTipoReporte);

        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        btnEnviarReporte = findViewById(R.id.btnEnviarReporte);

        btnMapa = findViewById(R.id.btnMapa);
        btnCorreo = findViewById(R.id.btnCorreo);
        btnLlamar = findViewById(R.id.btnLlamar);

        ivEvidencia = findViewById(R.id.ivEvidencia);

        ArrayAdapter<CharSequence> adapterColonias =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.colonias_array,
                        android.R.layout.simple_spinner_item
                );

        adapterColonias.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerColonia.setAdapter(adapterColonias);

        ArrayAdapter<CharSequence> adapterTipos =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.tipos_reporte,
                        android.R.layout.simple_spinner_item
                );

        adapterTipos.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerTipoReporte.setAdapter(adapterTipos);

        btnSeleccionarImagen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrirGaleria.launch("image/*");
                    }
                });

        btnEnviarReporte.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String nombre = etNombre.getText().toString();
                        String direccion = etDireccion.getText().toString();
                        String celular = etCelular.getText().toString();
                        String correo = etCorreo.getText().toString();
                        String descripcion = etDescripcion.getText().toString();

                        String colonia =
                                spinnerColonia.getSelectedItem().toString();

                        String tipoReporte =
                                spinnerTipoReporte.getSelectedItem().toString();

                        if (nombre.isEmpty() || descripcion.isEmpty()) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "Completa los campos",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        JSONObject parametrosPost = new JSONObject();

                        try {

                            parametrosPost.put(
                                    "nombre_interesado",
                                    nombre
                            );

                            parametrosPost.put(
                                    "direccion",
                                    direccion
                            );

                            parametrosPost.put(
                                    "colonia",
                                    colonia
                            );

                            parametrosPost.put(
                                    "celular",
                                    celular
                            );

                            parametrosPost.put(
                                    "correo",
                                    correo
                            );

                            parametrosPost.put(
                                    "tipo",
                                    tipoReporte
                            );

                            parametrosPost.put(
                                    "descripcion",
                                    descripcion
                            );

                            parametrosPost.put(
                                    "imagen",
                                    JSONObject.NULL
                            );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String url =
                                "https://mcaconsultores.com.mx/apireporte/reporte.php";

                        JsonObjectRequest request =
                                new JsonObjectRequest(
                                        Request.Method.POST,
                                        url,
                                        parametrosPost,

                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                Toast.makeText(
                                                        MainActivity.this,
                                                        "Reporte enviado",
                                                        Toast.LENGTH_LONG
                                                ).show();

                                                limpiarFormulario();
                                            }
                                        },

                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                Toast.makeText(
                                                        MainActivity.this,
                                                        error.toString(),
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            }
                                        }) {

                                    @Override
                                    public java.util.Map<String, String> getHeaders() {

                                        java.util.Map<String, String> headers =
                                                new java.util.HashMap<>();

                                        headers.put(
                                                "Authorization",
                                                "Bearer a0f4dcad-5903-482f-8982-88ec8bc6156e"
                                        );

                                        return headers;
                                    }
                                };

                        RequestQueue queue =
                                Volley.newRequestQueue(MainActivity.this);

                        queue.add(request);
                    }
                });

        btnMapa.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Uri uri = Uri.parse(
                                "geo:27.9297,-110.9083?q=Miramar+Guaymas+Sonora"
                        );

                        Intent intent =
                                new Intent(Intent.ACTION_VIEW, uri);

                        startActivity(intent);
                    }
                });
        btnCorreo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent =
                                new Intent(Intent.ACTION_SENDTO);

                        intent.setData(
                                Uri.parse(
                                        "mailto:palaciomunicipalguaymas@gob.mx"
                                )
                        );

                        startActivity(intent);
                    }
                });

        btnLlamar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent =
                                new Intent(Intent.ACTION_DIAL);

                        intent.setData(
                                Uri.parse("tel:6221692905")
                        );

                        startActivity(intent);
                    }
                });

        bottomNavigation.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(
                            @NonNull MenuItem item
                    ) {

                        int id = item.getItemId();

                        if (id == R.id.nav_reporte) {

                            layoutReporte.setVisibility(View.VISIBLE);
                            layoutContacto.setVisibility(View.GONE);

                            return true;
                        }

                        if (id == R.id.nav_contacto) {

                            layoutReporte.setVisibility(View.GONE);
                            layoutContacto.setVisibility(View.VISIBLE);

                            return true;
                        }

                        return false;
                    }
                });
    }

    private void limpiarFormulario() {

        etNombre.setText("");
        etDireccion.setText("");
        etCelular.setText("");
        etCorreo.setText("");
        etDescripcion.setText("");

        spinnerColonia.setSelection(0);
        spinnerTipoReporte.setSelection(0);

        ivEvidencia.setImageResource(
                android.R.drawable.ic_menu_camera
        );

        imagenSeleccionadaUri = null;
    }
}