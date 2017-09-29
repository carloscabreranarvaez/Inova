package cifer.nova.ciferapp.inova.Adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import cifer.nova.ciferapp.inova.Clases.Blogclass;

import java.util.List;

/**
 * Created by Cifer on 04/04/2017.
 */

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private List<Blogclass> item;
    private Context context;

    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {

        public BlogViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BlogViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }




}
