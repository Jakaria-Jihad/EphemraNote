package com.example.ephemranote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;


public class NoteActivity extends AppCompatActivity {

    EditText titleText, contentText;
    ImageButton saveNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        titleText = findViewById(R.id.note_title_text);
        contentText = findViewById(R.id.note_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);

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
        documentReference = Utility.getCollecReferenceNotes().document();

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.showToastMessage(NoteActivity.this, "Note uploaded successfully!");
                    finish();
                }else{
                    Utility.showToastMessage(NoteActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });

    }

}