package com.example.tugas3;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    Button buttonAdd, buttonDeleteAll;
    EditText editTextName;
    List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        buttonAdd = findViewById(R.id.button_add);
        buttonDeleteAll = findViewById(R.id.button_delete_all);
        editTextName = findViewById(R.id.editTextName);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllData();
            }
        });
    }

    private void loadData() {
        dataList = new ArrayList<>();
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }
        while (res.moveToNext()) {
            dataList.add(res.getString(1));  // Index 1 refers to NAME column
        }
        adapter = new RecyclerViewAdapter(dataList);
        recyclerView.setAdapter(adapter);
    }

    private void addData() {
        String name = editTextName.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Masukkan Nama", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isInserted = myDb.insertData(name);
        if (isInserted) {
            Toast.makeText(this, "Data Ditambahkan", Toast.LENGTH_SHORT).show();
            editTextName.setText("");  // Clear input field
            loadData();  // Refresh list after adding data
        } else {
            Toast.makeText(this, "Data Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAllData() {
        boolean isDeleted = myDb.deleteAllData();
        if (isDeleted) {
            Toast.makeText(this, "Semua Data Telah Dihapus", Toast.LENGTH_SHORT).show();
            dataList.clear();  // Hapus data dari list
            adapter.notifyDataSetChanged();  // Perbarui RecyclerView
        } else {
            Toast.makeText(this, "Tidak Ada Data yang Dapat Dihapus", Toast.LENGTH_SHORT).show();
        }
    }
}