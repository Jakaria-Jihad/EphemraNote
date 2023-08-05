package com.example.ephemranote;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Objects;


public class NoteActivity extends AppCompatActivity {

    EditText titleText, contentText;
    ImageButton saveNoteBtn;
    TextView pageTitleText;

    TextView deleteNoteTextViewBtn;

    String title, content, docId;
    boolean is_editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        titleText = findViewById(R.id.note_title_text);
        contentText = findViewById(R.id.note_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleText = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_textview_btn);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()){
            is_editMode = true;
        }
        titleText.setText(title);
        contentText.setText(content);

        if (is_editMode){
            pageTitleText.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        deleteNoteTextViewBtn.setOnClickListener((v) -> deleteNoteFromFirebase());

        saveNoteBtn.setOnClickListener(v -> saveNote());
    }

    void saveNote(){
        String noteTitle = titleText.getText().toString();
        String noteContent = contentText.getText().toString();

        if (noteTitle.isEmpty()){
            titleText.setError("Title is missing");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);

    }
    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if (is_editMode){
            documentReference = Utility.getCollecReferenceNotes().document(docId);
        }else {
            documentReference = Utility.getCollecReferenceNotes().document();
        }
        documentReference.set(note).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Utility.showToastMessage(NoteActivity.this, "Note uploaded successfully!");
                finish();
            }else{
                Utility.showToastMessage(NoteActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage());
            }
        });

    }
    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollecReferenceNotes().document(docId);
        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Utility.showToastMessage(NoteActivity.this, "Note deleted successfully!");
                finish();
            }else{
                Utility.showToastMessage(NoteActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage());
            }
        });


    }

}