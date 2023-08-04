package com.example.ephemranote;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("title");

        if (docId != null && !docId.isEmpty()){
            is_editMode = true;
        }
        if (is_editMode){
            pageTitleText.setText("Edit your note");
        }

        titleText.setText("title");
        contentText.setText("content");

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

}