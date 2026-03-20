package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.app.AlertDialog;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private LinearLayout notesContainer;
    private FloatingActionButton fabAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        db = new DatabaseHelper(this);
        notesContainer = findViewById(R.id.notes_container);
        fabAddNote = findViewById(R.id.fab_add_note);

        fabAddNote.setOnClickListener(v -> showAddNoteDialog());

        Button backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        loadNotes();
    }

    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_note_dialog, null);
        builder.setView(dialogView);

        EditText etDescription = dialogView.findViewById(R.id.et_note_description);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSave = dialogView.findViewById(R.id.btn_save);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String description = etDescription.getText().toString().trim();
            if (!description.isEmpty()) {
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                Note note = new Note(currentDate, description);
                db.addNote(note);
                loadNotes();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Введите описание заметки", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNotes() {
        notesContainer.removeAllViews();
        List<Note> notes = db.getAllNotes();

        for (Note note : notes) {
            View noteView = getLayoutInflater().inflate(R.layout.note_item, null);

            TextView dateView = noteView.findViewById(R.id.note_date);
            TextView descView = noteView.findViewById(R.id.note_description);
            Button deleteButton = noteView.findViewById(R.id.delete_button);
            Button editButton = noteView.findViewById(R.id.edit_button);

            dateView.setText(note.getDate());
            descView.setText(note.getDescription());

            deleteButton.setOnClickListener(v -> {
                db.deleteNote(note);
                loadNotes();
            });

            editButton.setOnClickListener(v -> showEditNoteDialog(note));

            notesContainer.addView(noteView);
        }
    }

    private void showEditNoteDialog(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_note_dialog, null);
        builder.setView(dialogView);

        EditText etDescription = dialogView.findViewById(R.id.et_note_description);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnSave = dialogView.findViewById(R.id.btn_save);

        etDescription.setText(note.getDescription());

        AlertDialog dialog = builder.create();
        dialog.setTitle("Редактировать заметку");
        dialog.show();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String newDescription = etDescription.getText().toString().trim();
            if (!newDescription.isEmpty()) {
                note.setDescription(newDescription);
                db.updateNote(note);
                loadNotes();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Описание не может быть пустым", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}