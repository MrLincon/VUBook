package com.example.vubook.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vubook.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class CRAdapter extends FirestoreRecyclerAdapter<CR, CRAdapter.CRHolder> {

    private OnItemClickListener listener;
    private Context mContext;

    public CRAdapter(@NonNull FirestoreRecyclerOptions<CR> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CRHolder holder, int position, @NonNull CR model) {
        Picasso.get().load(model.getThumbImageUrl()).error(R.drawable.user_default).into(holder.imageView);
        holder.name.setText(model.getName());
        holder.semester.setText(model.getSemester());
        holder.section.setText(model.getSection());
        holder.department.setText(model.getDepartment());
    }

    @NonNull
    @Override
    public CRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cr_card_layout,
                parent, false);
        return new CRHolder(view);
    }

    class CRHolder extends RecyclerView.ViewHolder {
        TextView name,semester,section,department;
        CircularImageView imageView;
//        ProgressBar progressBar;

        public CRHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_image);
            name = itemView.findViewById(R.id.card_name);
            semester = itemView.findViewById(R.id.card_sem);
            section = itemView.findViewById(R.id.card_sec);
            department = itemView.findViewById(R.id.card_department);
//            progressBar = itemView.findViewById(R.id.cr_card_progressbar);

            mContext = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position));
                    }
                }
            });
        }
    }

        public interface OnItemClickListener {
            void onItemClick(DocumentSnapshot documentSnapshot);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
}
