package com.example.sqllitetp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqllitetp.EditEtudiantDialogFragment;
import com.example.sqllitetp.R;
import com.example.sqllitetp.entities.Etudiant;

import java.io.File;
import java.util.List;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {

    private Context context;
    private List<Etudiant> etudiantList;

    public EtudiantAdapter(Context context, List<Etudiant> etudiantList) {
        this.context = context;
        this.etudiantList = etudiantList;
    }

    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.etudiant_item, parent, false);
        return new EtudiantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder holder, int position) {
        Etudiant etudiant = etudiantList.get(position);
        holder.nom.setText(etudiant.getNom());
        holder.prenom.setText(etudiant.getPrenom());
        holder.dateNaissance.setText(etudiant.getDateNaissance());
        holder.id.setText(String.valueOf(etudiant.getId()));

        String imagePath = etudiant.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null) {
                holder.image.setImageBitmap(bitmap);
            } else {
                holder.image.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            EditEtudiantDialogFragment dialog = new EditEtudiantDialogFragment(etudiant, this);
            dialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "EditEtudiantDialogFragment");
        });

    }
    public void updateEtudiant(Etudiant updatedEtudiant) {
        for (int i = 0; i < etudiantList.size(); i++) {
            if (etudiantList.get(i).getId() == updatedEtudiant.getId()) {
                etudiantList.set(i, updatedEtudiant);
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return etudiantList.size();
    }

    public static class EtudiantViewHolder extends RecyclerView.ViewHolder {
        TextView nom, prenom, dateNaissance, id;
        ImageView image;

        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.etudiant_nom);
            prenom = itemView.findViewById(R.id.etudiant_prenom);
            dateNaissance = itemView.findViewById(R.id.etudiant_date);
            image = itemView.findViewById(R.id.etudiant_image);
            id = itemView.findViewById(R.id.etudiant_id);
        }
    }
}