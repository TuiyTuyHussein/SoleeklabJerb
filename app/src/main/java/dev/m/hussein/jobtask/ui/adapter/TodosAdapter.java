package dev.m.hussein.jobtask.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.m.hussein.jobtask.R;
import dev.m.hussein.jobtask.api.ApiService;
import dev.m.hussein.jobtask.model.TipsModel;
import dev.m.hussein.jobtask.model.TodoModel;
import dev.m.hussein.jobtask.ui.custom.checkbox.SmoothCheckBox;

/**
 * Created by Dev. M. Hussein on 11/5/2017.
 */

public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.Holder> {

    private Context context;
    private List<TodoModel.Todo> array = new LinkedList<>();

    public TodosAdapter(Context context) {
        this.context = context;
    }


    public void addItem(TodoModel.Todo todo){
        array.add(todo);
        notifyItemInserted(array.size() - 1);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_todo , parent , false);
        Holder holder = new Holder(v);
        holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT));
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        TodoModel.Todo todo = array.get(position);

        holder.title.setText(todo.title);
        holder.checkBox.setChecked(todo.check);
    }

    @Override
    public int getItemCount() {
        return array == null ? 0 : array.size();
    }

    class Holder extends RecyclerView.ViewHolder{


        @BindView(R.id.todoText) AppCompatTextView title;
        @BindView(R.id.checkbox) SmoothCheckBox checkBox;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
        }
    }
}
