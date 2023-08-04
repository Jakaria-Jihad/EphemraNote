package com.example.ephemranote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViews> {
    Context context;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViews holder, int position, @NonNull Note note) {
        holder.titleText.setText(note.title);
        holder.contentText.setText(note.content);
        holder.timestampText.setText(Utility.timestamptoString(note.timestamp));
    }

    @NonNull
    @Override
    public NoteViews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyled_note_items, parent, false);
        return new NoteViews(view);
    }

    class NoteViews extends RecyclerView.ViewHolder {
        TextView titleText, contentText, timestampText;

        public NoteViews(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.recycled_note_title);
            contentText = itemView.findViewById(R.id.recycled_note_content);
            timestampText = itemView.findViewById(R.id.recycled_note_timestamp);
        }
    }
}
