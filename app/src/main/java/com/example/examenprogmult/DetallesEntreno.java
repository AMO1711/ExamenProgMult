package com.example.examenprogmult;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetallesEntreno extends Fragment {

    private String nombre;
    private String descripcion;
    private int imagenResId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detalles_entreno, container, false);

        if (getArguments() != null) {
            nombre = getArguments().getString("nombre");
            descripcion = getArguments().getString("descripcion");
            imagenResId = getArguments().getInt("imagen");
        }

        TextView nombreText = view.findViewById(R.id.nombre_entreno);
        TextView descripcionText = view.findViewById(R.id.descripcion_entreno);
        ImageView imagenView = view.findViewById(R.id.imagen_entreno);

        nombreText.setText(nombre);
        descripcionText.setText(descripcion);
        imagenView.setImageResource(imagenResId);

        return view;
    }
}
