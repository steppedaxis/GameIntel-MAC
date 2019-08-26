package com.example.gameintel.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gameintel.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class GameAdapter extends FirestoreRecyclerAdapter<Game, GameAdapter.GameHolder> {

    private OnGameListener mOnGameListener;

    public GameAdapter(@NonNull FirestoreRecyclerOptions<Game> options,OnGameListener onGameListener) {
        super(options);
        this.mOnGameListener=onGameListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull GameHolder holder, int position, @NonNull final Game model) {
        holder.mTitleView.setText(model.getName());
        holder.mGenreView.setText(model.getGenre());
       Glide.with(holder.mImageView.getContext())
               .load(model.getImage())
               .into(holder.mImageView);

    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new GameHolder(v,mOnGameListener);
    }

    class GameHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       TextView mTitleView;
       TextView mGenreView;
       ImageView mImageView;
       OnGameListener onGameListener;

        public GameHolder(@NonNull View itemView,OnGameListener onGameListener) {
            super(itemView);
            mTitleView=itemView.findViewById(R.id.gameName);
            mGenreView=itemView.findViewById(R.id.gameGenre);
            mImageView=itemView.findViewById(R.id.gameImage);
            this.onGameListener= onGameListener;

            mImageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onGameListener.onGameClick(getAdapterPosition());
        }
    }




    public interface OnGameListener{
        void onGameClick(int position);
    }

}
