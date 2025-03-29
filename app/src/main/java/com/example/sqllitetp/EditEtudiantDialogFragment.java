package com.example.sqllitetp;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.sqllitetp.R;
import com.example.sqllitetp.adapter.EtudiantAdapter;
import com.example.sqllitetp.entities.Etudiant;
import com.example.sqllitetp.service.EtudiantService;

import java.io.IOException;
import java.util.Calendar;

public class EditEtudiantDialogFragment extends DialogFragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText nomEdit, prenomEdit, dateNaissanceEdit;
    private ImageView imageView;
    private Button selectImageButton, selectDateButton;
    private Etudiant etudiant;
    private EtudiantService etudiantService;
    private EtudiantAdapter adapter;
    private String imagePath;

    public EditEtudiantDialogFragment(Etudiant etudiant, EtudiantAdapter adapter) {
        this.etudiant = etudiant;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_etudiant, null);

        nomEdit = view.findViewById(R.id.nom_edit);
        prenomEdit = view.findViewById(R.id.prenom_edit);
        dateNaissanceEdit = view.findViewById(R.id.date_naissance);
        imageView = view.findViewById(R.id.image_view);
        selectImageButton = view.findViewById(R.id.select_image_button);
        selectDateButton = view.findViewById(R.id.select_date_button);

        etudiantService = new EtudiantService(getContext());

        nomEdit.setText(etudiant.getNom());
        prenomEdit.setText(etudiant.getPrenom());
        dateNaissanceEdit.setText(etudiant.getDateNaissance());
        imagePath = etudiant.getImage();

        if (imagePath != null && !imagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }

        selectImageButton.setOnClickListener(v -> openImagePicker());
        selectDateButton.setOnClickListener(v -> openDatePicker());

        builder.setView(view)
                .setTitle("Modifier Etudiant")
                .setPositiveButton("Enregistrer", (dialog, id) -> {
                    etudiant.setNom(nomEdit.getText().toString());
                    etudiant.setPrenom(prenomEdit.getText().toString());
                    etudiant.setDateNaissance(dateNaissanceEdit.getText().toString());
                    etudiant.setImage(imagePath);

                    etudiantService.update(etudiant);
                    adapter.updateEtudiant(etudiant);
                    Toast.makeText(getContext(), "Etudiant mis à jour avec succès", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, id) -> EditEtudiantDialogFragment.this.getDialog().cancel());

        return builder.create();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            dateNaissanceEdit.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
        }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                imagePath = getRealPathFromURI(uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }
}