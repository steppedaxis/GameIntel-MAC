package com.example.gameintel.classes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gameintel.R;
import com.example.gameintel.activities.GamePage;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;


public class GameAdapter extends FirestoreRecyclerAdapter<Game, GameAdapter.GameHolder> {

    private OnItemClickListener listener;
    private Context context;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    public GameAdapter(@NonNull FirestoreRecyclerOptions<Game> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GameHolder holder, int position, @NonNull final Game model) {
        holder.mTitleView.setText(model.getName());
        holder.mPublisherView.setText(model.getPublisher());

        FirebaseUser currenrtUser=mAuth.getCurrentUser();
        if (currenrtUser==null){
            holder.favoriteIcon.setVisibility(View.GONE);
        }else{
            holder.favoriteIcon.setVisibility(View.VISIBLE);
        }


       Glide.with(holder.mImageView.getContext())
               .load(model.getImage())
               .into(holder.mImageView);


    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new GameHolder(v);
    }

    class GameHolder extends RecyclerView.ViewHolder{
       TextView mTitleView;
       ImageView mImageView;
       TextView mPublisherView;
       ImageView favoriteIcon;

        public GameHolder(@NonNull View itemView) {
            super(itemView);
            mTitleView=itemView.findViewById(R.id.gameName);
            mImageView=itemView.findViewById(R.id.gameImage);
            mPublisherView=itemView.findViewById(R.id.gamePublisher);
            favoriteIcon=itemView.findViewById(R.id.favorite);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    context = view.getContext();
                    String name=mTitleView.getText().toString();
                    passData(name);
                    listener.onItemClick(getSnapshots().getSnapshot(position),name,position);

                }
            });


        }


    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,String name,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    private void passData(String title) {

        Intent intent = new Intent(context,GamePage.class);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }





}
