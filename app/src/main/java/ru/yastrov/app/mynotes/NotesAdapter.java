package ru.yastrov.app.mynotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private RecyclerViewClickListener mListener;
    private List<NoteItem> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecyclerViewClickListener mListener;
        public TextView title, date;

        public MyViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            title = (TextView) view.findViewById(R.id.notes_row_title);
            date = (TextView) view.findViewById(R.id.notes_row_date);
            mListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    public NotesAdapter(List<NoteItem> notesList, RecyclerViewClickListener listener) {
        this.notesList = notesList;
        mListener = listener;
    }

    public void add(int position, NoteItem item) {
        notesList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        notesList.remove(position);
        notifyItemRemoved(position);
    }

    public NoteItem get(int position) {
        return notesList.get(position);
    }

    public void updateData(List<NoteItem> dataset) {
        notesList.clear();
        notesList.addAll(dataset);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.note_list_row, parent, false);
        return new MyViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NoteItem note = notesList.get(position);
        holder.title.setText(note.getTitle());
        holder.date.setText(note.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}