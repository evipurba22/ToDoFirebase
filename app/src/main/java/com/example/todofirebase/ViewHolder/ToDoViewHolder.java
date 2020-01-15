package com.example.todofirebase.ViewHolder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.todofirebase.R;

public class ToDoViewHolder  extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public TextView text_task, text_priority;

    public ToDoViewHolder(@NonNull View itemView) {
        super(itemView);

        text_task = itemView.findViewById(R.id.text_task);
        text_priority = itemView.findViewById(R.id.text_priority);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select menu");
        menu.add(0, 0, getAdapterPosition(), "Update");
        menu.add(0, 1, getAdapterPosition(), "Delete");
    }
}


