package com.example.ravi.besafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

/**
 * Created by ravi on 06-06-2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    String[] VideoID = {"ORAOkP1h3R0",
                        "T7aNSRoDCmg",
                        "CKaa19kpqzM",
                        "XARIQt1Z20M",
                        "uGqfsYaUxYo",
                        "Js-rWbzpd6M",
                        "M4_8PoRQP8w",
                        "jkvm13QYfpI",
                        "Budz0f73Dws",
                        "SAyn12l87-k",
                        "Nkc8R_VtrAo",
                        "2_HgPXgJ16I",
                        "vX-OOfbnD9w","5cOtK3ZKIEw","ZwULI5wMtuY","1SRF-j9zjHM"
                        };
    Context ctx;
    public RecyclerAdapter(Context context) {
        this.ctx = context;
    }
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, final int position) {

//        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener=new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//            @Override
//            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
//            youTubeThumbnailView.setVisibility(View.VISIBLE);
//            holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
//
//                youTubeThumbnailView.release();
//                Toast.makeText(ctx, "Thumbnail Error", Toast.LENGTH_SHORT).show();
//                Log.d("ThumbNail Err:","Youtube_Thumbnail_Error");
//            }
//        };

        holder.youTubeThumbnailView.initialize("AIzaSyDbWv-CuiV9DcBoOhDtORRruGGHp0RN72M", new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(VideoID[position]);


                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailView.setVisibility(View.VISIBLE);
                        holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                       // youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                      //  youTubeThumbnailLoader.release();
                    }
                });


            }//onInitializeSuccess

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(ctx, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                Log.d("Youtube:","Youtube_error");
            }
        });//end of initialize


    }

    @Override
    public int getItemCount() {
        return VideoID.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        protected ImageView playButton;

        public ViewHolder(View itemView) {
            super(itemView);
            playButton=(ImageView)itemView.findViewById(R.id.btnYoutube_player);
            playButton.setOnClickListener(this);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);
        }

        @Override
        public void onClick(View v) {

            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx,"AIzaSyDbWv-CuiV9DcBoOhDtORRruGGHp0RN72M" , VideoID[getLayoutPosition()]);
            ctx.startActivity(intent);
        }
    }
}
