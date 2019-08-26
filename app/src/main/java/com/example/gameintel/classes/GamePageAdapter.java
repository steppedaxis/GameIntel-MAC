package com.example.gameintel.classes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gameintel.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class GamePageAdapter extends FirestoreRecyclerAdapter<Game, GamePageAdapter.GamePageHolder> {


    public GamePageAdapter(@NonNull FirestoreRecyclerOptions<Game> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GamePageHolder holder, int position, @NonNull Game model) {
        Glide.with(holder.mPageImageView.getContext())
                .load(model.getImage())
                .into(holder.mPageImageView);
        holder.mPageTitleView.setText(model.getName());
        holder.mPageGenreView.setText(model.getGenre());
        holder.mPageSubGenreView.setText(model.getSubGenres());



    }

    @NonNull
    @Override
    public GamePageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new GamePageHolder(v);
    }

    class GamePageHolder extends RecyclerView.ViewHolder{
        ImageView mPageImageView;
        TextView mPageTitleView;
        TextView mPageGenreView;
        TextView mPageSubGenreView;
        TextView mPageDeveloperView;
        TextView mPagePublisherView;
        TextView mPageDescriptionView;
        TextView mPageReleaseDateView;
        TextView mPageSeriesView;
        TextView mPagePlatformsView;

        public GamePageHolder(@NonNull View itemView) {
            super(itemView);
            mPageImageView=itemView.findViewById(R.id.page_image);
            mPageTitleView=itemView.findViewById(R.id.page_title);
            mPageGenreView=itemView.findViewById(R.id.page_genre);
            mPageSubGenreView=itemView.findViewById(R.id.page_sub_geners);
            mPageDeveloperView=itemView.findViewById(R.id.page_developer);
            mPagePublisherView=itemView.findViewById(R.id.page_publisher);
            mPageDescriptionView=itemView.findViewById(R.id.page_description);
            mPageReleaseDateView=itemView.findViewById(R.id.page_release_date);
            mPageSeriesView=itemView.findViewById(R.id.page_series);
            mPagePlatformsView=itemView.findViewById(R.id.page_platforms);

        }

    }

}
