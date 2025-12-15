package com.example.examenprogmult;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Entreno> entrenos = new ArrayList<>();
    private ArrayList<String> nombresEntrenos = new ArrayList<>(), descripcionesEntrenos = new ArrayList<>();
    private ArrayList<Integer> imagenesEntrenos = new ArrayList<>();
    private String nombreEjercicioActual;
    private final String KEY_NOMBRE_EJERCICIO_ACTUAL = "nombre_ejercicio_actual",
            KEY_NOMBRES_ENTRENOS = "nombres_entrenos",
            KEY_DESCRIPCIONES_ENTRENOS = "descripciones_entrenos",
            KEY_IMAGENES_ENTRENOS = "imagenes_entrenos";

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

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (findViewById(R.id.pantallaFragment2) != null){
                Fragment f2 = getSupportFragmentManager().findFragmentById(R.id.pantallaFragment2);

                if (f2 != null && f2.getArguments() != null && f2.getArguments().getString("nombre") != null) {
                    nombreEjercicioActual = f2.getArguments().getString("nombre");
                } else {
                    nombreEjercicioActual = null;
                }
            } else {
                nombreEjercicioActual = null;
            }
        });

        if (savedInstanceState != null) {
            nombresEntrenos = savedInstanceState.getStringArrayList(KEY_NOMBRES_ENTRENOS);
            descripcionesEntrenos = savedInstanceState.getStringArrayList(KEY_DESCRIPCIONES_ENTRENOS);
            imagenesEntrenos = savedInstanceState.getIntegerArrayList(KEY_IMAGENES_ENTRENOS);
            nombreEjercicioActual = savedInstanceState.getString(KEY_NOMBRE_EJERCICIO_ACTUAL);
            Log.d("Macia", "He recuperado: " + nombreEjercicioActual);
        } else {
            inicializarEntrenosBasicos();
        }

        for (int i = 0; i < nombresEntrenos.size(); i++) {
            entrenos.add(new Entreno(nombresEntrenos.get(i), descripcionesEntrenos.get(i), imagenesEntrenos.get(i)));
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (nombreEjercicioActual != null){
            if (findViewById(R.id.pantallaFragment2) != null){
                ft.replace(R.id.pantallaFragment1, new ListaEjercicios(), "LISTA_FRAGMENT");
                ft.commit();
            }
            int posicion = nombresEntrenos.indexOf(nombreEjercicioActual);
            colocarDetallesEntreno(entrenos.get(posicion));
        } else {
            if (findViewById(R.id.pantallaFragment2) != null){
                ft.replace(R.id.pantallaFragment2, new DetallesEntreno());
            }
            ft.replace(R.id.pantallaFragment1, new ListaEjercicios(), "LISTA_FRAGMENT");
            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArrayList(KEY_NOMBRES_ENTRENOS, nombresEntrenos);
        savedInstanceState.putStringArrayList(KEY_DESCRIPCIONES_ENTRENOS, descripcionesEntrenos);
        savedInstanceState.putIntegerArrayList(KEY_IMAGENES_ENTRENOS, imagenesEntrenos);
        savedInstanceState.putString(KEY_NOMBRE_EJERCICIO_ACTUAL, nombreEjercicioActual);
        Log.d("Macia", "He guardado: " + nombreEjercicioActual);
    }

    public ArrayList<Entreno> getEntrenos() {
        return entrenos;
    }

    public void colocarDetallesEntreno(Entreno entreno) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        DetallesEntreno detallesFragment = new DetallesEntreno();

        Bundle args = new Bundle();
        args.putString("nombre", entreno.getNombre());
        args.putString("descripcion", entreno.getDescripcion());
        args.putInt("imagen", entreno.getImagen());
        detallesFragment.setArguments(args);

        if (findViewById(R.id.pantallaFragment2) != null){
            ft.replace(R.id.pantallaFragment2, detallesFragment);
        } else {
            ft.replace(R.id.pantallaFragment1, detallesFragment);
        }

        nombreEjercicioActual = entreno.getNombre();

        Log.d("Macia", "Última modificación: " + nombreEjercicioActual);

        ft.addToBackStack(null);
        ft.commit();
    }

    public static class Entreno {
        private final String nombre;
        private final String descripcion;
        private final int imagen;

        public Entreno(String nombre, String descripcion, int imagen) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.imagen = imagen;
        }

        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }
        public int getImagen() { return imagen; }
    }

    private void inicializarEntrenosBasicos() {
        String[] nombres = getResources().getStringArray(R.array.nombre_entrenamientos_iniciales),
                descipciones = getResources().getStringArray(R.array.descripcion_entrenamienos_iniciales),
                imagenesSinProcesar = getResources().getStringArray(R.array.imagenes);
        int[] imagenes = new int[4];

        for (int i = 0; i < 4; i++) {
            imagenes[i] = getResources().getIdentifier(imagenesSinProcesar[i], "drawable", getPackageName());
        }

        for (int i = 0; i < 4; i++) {
            nombresEntrenos.add(nombres[i]);
            descripcionesEntrenos.add(descipciones[i]);
            imagenesEntrenos.add(imagenes[i]);
        }
    }

    public void anadirEntreno(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.nuevo_entreno_dialog, null);
        builder.setView(dialogView);

        EditText textoNombreDialog = dialogView.findViewById(R.id.texto_nombre_dialog);
        EditText textoDescripcionDialog = dialogView.findViewById(R.id.texto_descripcion_dialog);

        builder.setTitle("Introduce el nuevo entrenamiento");

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = textoNombreDialog.getText().toString();
            String descripcion = textoDescripcionDialog.getText().toString();
            ListaEjercicios listaFragment = (ListaEjercicios) getSupportFragmentManager().findFragmentByTag("LISTA_FRAGMENT");
            if (listaFragment == null) {
                listaFragment = (ListaEjercicios) getSupportFragmentManager().findFragmentById(R.id.pantallaFragment1);
            }

            int imagen = getResources().getIdentifier("levantar_pesas", "drawable", getPackageName());
            nombresEntrenos.add(nombre);
            descripcionesEntrenos.add(descripcion);
            imagenesEntrenos.add(imagen);
            entrenos.add(new Entreno(nombre, descripcion, imagen));

            if (listaFragment != null) {
                listaFragment.refrescarLista();
            }

            dialog.dismiss();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
