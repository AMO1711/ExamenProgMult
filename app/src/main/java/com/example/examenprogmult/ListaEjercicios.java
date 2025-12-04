package com.example.examenprogmult;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ListaEjercicios extends Fragment {

    private ArrayList<MainActivity.Entreno> entrenos;
    private EntrenoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.lista_ejercicios, container, false);

        // Obtener la lista de entrenos de MainActivity
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            entrenos = activity.getEntrenos();

            ListView lista_ejercicios = view.findViewById(R.id.lista_ejercicios);

            adapter = new EntrenoAdapter(entrenos);
            lista_ejercicios.setAdapter(adapter);

            // Listener para clicks en items
            lista_ejercicios.setOnItemClickListener((parent, v, position, id) -> {
                activity.colocarDetallesEntreno(entrenos.get(position));
            });
        }

        return view;
    }

    // Adapter interno para el ListView
    private class EntrenoAdapter extends BaseAdapter {

        private final ArrayList<MainActivity.Entreno> entrenos;

        public EntrenoAdapter(ArrayList<MainActivity.Entreno> entrenos) {
            this.entrenos = entrenos;
        }

        @Override
        public int getCount() {
            return entrenos.size();
        }

        @Override
        public Object getItem(int position) {
            return entrenos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_item, parent, false);
            }

            ImageView imagen = convertView.findViewById(R.id.imagen_item_lista);
            TextView texto = convertView.findViewById(R.id.texto_item_lista);

            MainActivity.Entreno entreno = entrenos.get(position);
            imagen.setImageResource(entreno.getImagen());
            texto.setText(entreno.getNombre());

            return convertView;
        }

        // Método para actualizar la lista si se añade un nuevo entreno
        public void actualizarLista() {
            notifyDataSetChanged();
        }
    }

    // Método público que MainActivity puede llamar al añadir un entreno
    public void refrescarLista() {
        if (adapter != null) {
            adapter.actualizarLista();
        }
    }
}
