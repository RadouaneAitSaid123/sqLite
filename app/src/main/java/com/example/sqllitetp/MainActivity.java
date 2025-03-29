package com.example.sqllitetp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sqllitetp.entities.Etudiant;
import com.example.sqllitetp.service.EtudiantService;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText nom;
    private EditText prenom;
    private EditText id;
    private TextView res;
    private EtudiantService etudiantService = null;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private EditText dateNaissanceEdit;

    void clear() {
        nom.setText("");
        prenom.setText("");
        id.setText("");
        dateNaissanceEdit.setText("");
        imageView.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        nom = findViewById(R.id.nom_edit);
        prenom = findViewById(R.id.prenom_edit);
        id = findViewById(R.id.id_Edit);
        res = findViewById(R.id.resultat);
        etudiantService = new EtudiantService(this);
        imageView = findViewById(R.id.image_view);
        dateNaissanceEdit = findViewById(R.id.date_naissance);


    }


    public void ajouter(View view) {
        String imagePath = imageView.getTag() != null ? imageView.getTag().toString() : "";

        Etudiant etudiant = new Etudiant(
                nom.getText().toString(),
                prenom.getText().toString(),
                imagePath,
                dateNaissanceEdit.getText().toString()
        );

        etudiantService.create(etudiant);
        clear();
        Toast.makeText(this, "Etudiant ajouté avec succès", Toast.LENGTH_SHORT).show();
    }


    public void chercher(View view) {
        Etudiant et = etudiantService.findById(Integer.parseInt(id.getText().toString()));
        if (et != null) {
            res.setText(et.getPrenom() + " " + et.getNom());
            clear();
        } else {
            Toast.makeText(this, "Etudiant non trouvé", Toast.LENGTH_SHORT).show();
            clear();
        }
    }

    public void supprimer(View view) {
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Etes vous sure de supprimer cet etudiant?")
                .setPositiveButton("Oui", (dialog, id) -> deleteStudent())
                .setNegativeButton("Nom", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    private void deleteStudent() {
        int studentId = Integer.parseInt(id.getText().toString());
        Etudiant et = etudiantService.findById(studentId);
        if (et != null) {
            etudiantService.delete(et);
            Toast.makeText(this, "Etudiant supprimé avec succé", Toast.LENGTH_SHORT).show();
            clear();
        } else {
            Toast.makeText(this, "Etudiant non trouvé", Toast.LENGTH_SHORT).show();
        }
    }
    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                imageView.setTag(getRealPathFromURI(uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }
    public void afficher(View view) {
        Intent intent = new Intent(this, EtudiantListActivity.class);
        startActivity(intent);
    }

    public void showDatePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateNaissanceEdit.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
}