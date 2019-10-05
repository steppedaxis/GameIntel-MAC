package com.example.gameintel.classes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gameintel.R;
import com.example.gameintel.activities.GamePage;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class GameAdapter extends FirestoreRecyclerAdapter<Game, GameAdapter.GameHolder> {

    private OnItemClickListener listener;
    private Context context;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore database=FirebaseFirestore.getInstance();

    public GameAdapter(@NonNull FirestoreRecyclerOptions<Game> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final GameHolder holder, int position, @NonNull final Game model) {
        holder.mTitleView.setText(model.getName());
        holder.mPublisherView.setText(model.getPublisher());

        FirebaseUser currenrtUser=mAuth.getCurrentUser();
        if (currenrtUser==null){
            holder.favoriteIcon.setVisibility(View.GONE);
            holder.unfavoriteIcon.setVisibility(View.GONE);
        }
        else{
            database.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot document:task.getResult()){
                            FirebaseUser currenrtUser=mAuth.getCurrentUser();
                            List<String> favoritelist=(List<String>) document.get("favoriteGames");

                            if(document.getId().equals(currenrtUser.getUid())){
                                String title=model.getName();
                                for (String favorite:favoritelist){
                                    if (favorite.equals(title)){
                                        holder.unfavoritebutton.setVisibility(View.VISIBLE);
                                        holder.favoritebutton.setVisibility(View.GONE);

                                    }
                                }
                            }

                        }

                    }
                }
            });

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
       ImageButton favoriteIcon;
       ImageButton unfavoriteIcon;
       Button unfavoritebutton;
       Button favoritebutton;



        public GameHolder(@NonNull View itemView) {
            super(itemView);
            mTitleView=itemView.findViewById(R.id.gameName);
            mImageView=itemView.findViewById(R.id.gameImage);
            mPublisherView=itemView.findViewById(R.id.gamePublisher);
            favoriteIcon=itemView.findViewById(R.id.favorite);
            unfavoriteIcon=itemView.findViewById(R.id.unfavorite);
            unfavoritebutton=itemView.findViewById(R.id.unfavoritebutton);
            favoritebutton=itemView.findViewById(R.id.favoritebutton);

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

            favoritebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title=mTitleView.getText().toString();
                    addToFavorites(title);
                    favoritebutton.setVisibility(View.GONE);
                    unfavoritebutton.setVisibility(View.VISIBLE);
                }
            });

            unfavoritebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title=mTitleView.getText().toString();
                    removeFromFavorites(title);
                    unfavoritebutton.setVisibility(View.GONE);
                    favoritebutton.setVisibility(View.VISIBLE);
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

    public void addToFavorites(String gameName){
        FirebaseUser currentUser=mAuth.getCurrentUser();
        String userID=currentUser.getUid();
        DocumentReference currentUserRef=database.collection("Users").document(userID);
        currentUserRef.update("favoriteGames", FieldValue.arrayUnion(gameName));
    }

    public void removeFromFavorites(String gameName){
        FirebaseUser currentUser=mAuth.getCurrentUser();
        String userID=currentUser.getUid();
        DocumentReference currentUserRef=database.collection("Users").document(userID);
        currentUserRef.update("favoriteGames", FieldValue.arrayRemove(gameName));
    }



}
