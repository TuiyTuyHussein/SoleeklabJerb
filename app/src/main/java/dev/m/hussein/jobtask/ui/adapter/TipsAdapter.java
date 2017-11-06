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

/**
 * Created by Dev. M. Hussein on 11/5/2017.
 */

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.Holder> {

    private Context context;
    private List<TipsModel.Tip> array = new LinkedList<>();

    public TipsAdapter(Context context) {
        this.context = context;
    }


    public void addItem(TipsModel.Tip tip){
        array.add(tip);
        notifyItemInserted(array.size() - 1);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_tips , parent , false);
        Holder holder = new Holder(v);
        holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT));
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        TipsModel.Tip tip = array.get(position);
        holder.count.setText(String.valueOf(position + 1));
        holder.title.setText(tip.title);
        Picasso.with(context)
                .load(ApiService.Constants.BASE_IMAGE_URI.concat(tip.image)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return array == null ? 0 : array.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        @BindView(R.id.count) AppCompatTextView count;
        @BindView(R.id.tipText) AppCompatTextView title;
        @BindView(R.id.image) ImageView imageView;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
        }
    }
}
