
package com.example.todofirebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.todofirebase.Model.ToDo;
import com.example.todofirebase.ViewHolder.ToDoViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    FirebaseDatabase database;
    DatabaseReference todoDb;

    FirebaseRecyclerOptions<ToDo> options;
    FirebaseRecyclerAdapter<ToDo, ToDoViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                startActivity(intent);
            }
        });
        database = FirebaseDatabase.getInstance();
        todoDb = database.getReference("ToDo");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showTask();
    }

    private void showTask() {
        options = new FirebaseRecyclerOptions.Builder<ToDo>()
                .setQuery(todoDb, ToDo.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ToDo, ToDoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ToDoViewHolder holder, int position, @NonNull ToDo model) {
                holder.text_task.setText(model.getTask());
                holder.text_priority.setText(model.getPriority());
            }

            @NonNull
            @Override
            public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.todo_row, viewGroup, false);
                return new ToDoViewHolder(itemView);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals("Update")) {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        } else if (item.getTitle().equals("Delete")) {
            deleteTask(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteTask(String key) {
        todoDb.child(key).removeValue();

    }

    private void showUpdateDialog(final String key, ToDo item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update");
        builder.setMessage("Please update the fields");

        View update_layout = LayoutInflater.from(this).inflate(R.layout.custom_layout, null);

        final EditText edt_update_task = update_layout.findViewById(R.id.edit_update_task);
        final EditText edt_update_priority = update_layout.findViewById(R.id.edit_update_priority);

        edt_update_task.setText(item.getTask());
        edt_update_priority.setText(item.getPriority());

        builder.setView(update_layout);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                String task = edt_update_task.getText().toString();
                String priority = edt_update_priority.getText().toString();

                ToDo toDo = new ToDo(task, priority);
                todoDb.child(key).setValue(toDo);

                Toast.makeText(MainActivity.this, "Task is updated", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);

}
@Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.delete_all){
          todoDb.removeValue();
        }
        return super.onOptionsItemSelected(item);
}
}
