package com.example.examenprogmult;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Lista central de entrenos
    private ArrayList<Entreno> entrenos = new ArrayList<>();

    // Keys para guardar estado
    private final String KEY_ENTRENOS = "key_entrenos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajustar padding según sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Restaurar entrenos si hay savedInstanceState
        if (savedInstanceState != null) {
            entrenos = savedInstanceState.getParcelableArrayList(KEY_ENTRENOS);
        }

        // Crear entrenos iniciales si no hay
        if (entrenos.isEmpty()) {
            crearEntrenos();
        }

        // Añadir el fragmento de la lista
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.pantallaFragment1, new ListaEjercicios());
        ft.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_ENTRENOS, entrenos);
    }

    // Getter para fragmentos
    public ArrayList<Entreno> getEntrenos() {
        return entrenos;
    }

    // Función para añadir dinámicamente un entreno
    public void anadirEntreno(String nombre, String descripcion, int imagenResId) {
        entrenos.add(new Entreno(nombre, descripcion, imagenResId));
        // El fragmento ListaEjercicios actualizará el adaptador si lo necesita
    }

    // Función que muestra detalles de un entreno
    public void colocarDetallesEntreno(Entreno entreno) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        DetallesEntreno detallesFragment = new DetallesEntreno();

        // Puedes pasar los datos mediante argumentos
        Bundle args = new Bundle();
        args.putString("nombre", entreno.getNombre());
        args.putString("descripcion", entreno.getDescripcion());
        args.putInt("imagen", entreno.getImagen());
        detallesFragment.setArguments(args);

        ft.replace(R.id.pantallaFragment1, detallesFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    // Crear entrenos iniciales (ejemplo)
    private void crearEntrenos() {
        entrenos.add(new Entreno("Entreno 1", "Descripcion 1", R.drawable.ic_launcher_foreground));
        entrenos.add(new Entreno("Entreno 2", "Descripcion 2", R.drawable.ic_launcher_foreground));
        entrenos.add(new Entreno("Entreno 3", "Descripcion 3", R.drawable.ic_launcher_foreground));
        entrenos.add(new Entreno("Entreno 4", "Descripcion 4", R.drawable.ic_launcher_foreground));
    }

    // Clase Entreno
    public static class Entreno implements android.os.Parcelable {
        private final String nombre;
        private final String descripcion;
        private final int imagen;

        public Entreno(String nombre, String descripcion, int imagen) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.imagen = imagen;
        }

        protected Entreno(android.os.Parcel in) {
            nombre = in.readString();
            descripcion = in.readString();
            imagen = in.readInt();
        }

        public static final Creator<Entreno> CREATOR = new Creator<Entreno>() {
            @Override
            public Entreno createFromParcel(android.os.Parcel in) {
                return new Entreno(in);
            }

            @Override
            public Entreno[] newArray(int size) {
                return new Entreno[size];
            }
        };

        public String getNombre() { return nombre; }
        public String getDescripcion() { return descripcion; }
        public int getImagen() { return imagen; }

        @Override
        public int describeContents() { return 0; }

        @Override
        public void writeToParcel(android.os.Parcel parcel, int i) {
            parcel.writeString(nombre);
            parcel.writeString(descripcion);
            parcel.writeInt(imagen);
        }
    }
}
