package com.example.sqllitetp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqllitetp.adapter.EtudiantAdapter;
import com.example.sqllitetp.entities.Etudiant;
import com.example.sqllitetp.service.EtudiantService;

import java.util.List;

public class EtudiantListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EtudiantService etudiantService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etudiantService = new EtudiantService(this);
        List<Etudiant> etudiants = etudiantService.findAll();

        EtudiantAdapter adapter = new EtudiantAdapter(this, etudiants);
        recyclerView.setAdapter(adapter);
    }
}
