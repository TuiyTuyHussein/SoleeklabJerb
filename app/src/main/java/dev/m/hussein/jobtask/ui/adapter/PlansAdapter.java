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
import dev.m.hussein.jobtask.config.LocaleAlphabets;
import dev.m.hussein.jobtask.model.Plan;
import dev.m.hussein.jobtask.model.TipsModel;

/**
 * Created by Dev. M. Hussein on 11/5/2017.
 */

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.Holder> {

    private Context context;
    private List<Plan> array = new LinkedList<>();
    private char[] alphapits;

    public PlansAdapter(Context context) {
        this.context = context;
        alphapits =  LocaleAlphabets.getAlphabet(LocaleAlphabets.LocaleLanguage.ENGLISH , true);
    }


    public void addItem(Plan plan){
        array.add(plan);
        notifyItemInserted(array.size() - 1);
    }

    public List<Plan> getArray() {
        return array;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_plan , parent , false);
        Holder holder = new Holder(v);
        holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT));
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Plan plan = array.get(position);
        if (plan == null) return;

        holder.planText.setText(context.getString(R.string.plan).concat(" ").concat(String.valueOf(alphapits[position > alphapits.length ? alphapits.length : position])));
        holder.locationTxt.setText(plan.location);
        holder.inviteesTxt.setText(String.valueOf(plan.invitees).concat("+").concat(context.getString(R.string.friends_family)));
        holder.budgetTxt.setText(context.getString(R.string.planned_cost).concat(" ").concat(String.valueOf(plan.budget).concat("L.E")));

    }

    @Override
    public int getItemCount() {
        return array == null ? 0 : array.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        @BindView(R.id.planName) AppCompatTextView planText;
        @BindView(R.id.location) AppCompatTextView locationTxt;
        @BindView(R.id.invitees) AppCompatTextView inviteesTxt;
        @BindView(R.id.budget) AppCompatTextView budgetTxt;


        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
        }
    }


}
