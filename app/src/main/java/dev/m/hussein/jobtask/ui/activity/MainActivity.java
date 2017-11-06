package dev.m.hussein.jobtask.ui.activity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.m.hussein.jobtask.R;
import dev.m.hussein.jobtask.api.ApiService;
import dev.m.hussein.jobtask.config.PreferenceUtility;
import dev.m.hussein.jobtask.model.Plan;
import dev.m.hussein.jobtask.model.TipsModel;
import dev.m.hussein.jobtask.model.TodoModel;
import dev.m.hussein.jobtask.ui.adapter.PlansAdapter;
import dev.m.hussein.jobtask.ui.adapter.TipsAdapter;
import dev.m.hussein.jobtask.ui.adapter.TodosAdapter;
import dev.m.hussein.jobtask.ui.dialog.DatePickerDialogFragment;
import dev.m.hussein.jobtask.ui.dialog.TimePickerDialogFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener, ApiService.OnTipsLoaded, ApiService.OnTodoLoaded {


    private static final int ADD_PLAN_REQUEST = 10;
    @BindView(R.id.dateLayout) LinearLayout dateLayout;
    @BindView(R.id.addCover) AppCompatButton addCover;
    @BindView(R.id.cover) ImageView cover;
    @BindView(R.id.addPlan) FloatingActionButton addPlan;
    @BindView(R.id.tipsListView) RecyclerView tipsListView;
    @BindView(R.id.todoListView) RecyclerView todoListView;
    @BindView(R.id.plansListView) RecyclerView plansListView;
    @BindView(R.id.tipsProgress) ProgressBar tipsProgress;
    @BindView(R.id.todoProgress) ProgressBar todoProgress;
    @BindView(R.id.daysText) AppCompatTextView daysText;
    @BindView(R.id.hoursText) AppCompatTextView hoursText;
    @BindView(R.id.minText) AppCompatTextView minsText;
    @BindView(R.id.secText) AppCompatTextView secText;


    TipsAdapter tipsAdapter;
    TodosAdapter todosAdapter;
    PlansAdapter plansAdapter;

    private Calendar now , selectedCal;
    private CountDownTimer counterDown;
    private Uri coverUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupViews();
    }

    private void setupViews() {
        dateLayout.setOnClickListener(this);
        addCover.setOnClickListener(this);
        addPlan.setOnClickListener(this);



        plansAdapter = new PlansAdapter(this);
        plansListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        plansListView.setAdapter(plansAdapter);



        tipsAdapter = new TipsAdapter(this);
        tipsListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        tipsListView.setAdapter(tipsAdapter);


        todosAdapter = new TodosAdapter(this);
        todoListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        todoListView.setAdapter(todosAdapter);


        loadOldPlans();
        loadTipsData();
        loadTodoData();

        coverUri = PreferenceUtility.open(this).getUri(PreferenceUtility.TAG_COVER_URI);
        changeCover();
        selectedCal = PreferenceUtility.open(this).getCalendar(PreferenceUtility.TAG_SELECTED_CALENDER);
        startCountDown();
    }

    private void loadOldPlans() {
        List<Plan> plans = PreferenceUtility.open(this).getPlans(PreferenceUtility.TAG_PLANS_ITEMS);
        if (plans != null){
            for (Plan plan : plans){
                plansAdapter.addItem(plan);
            }
        }
    }

    private void loadTodoData() {
        todoProgress.setVisibility(View.VISIBLE);
        ApiService.connection.loadTodo(this);
    }

    private void loadTipsData() {
        tipsProgress.setVisibility(View.VISIBLE);
        ApiService.connection.loadTips(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dateLayout:
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                datePickerDialogFragment.setOnDateSet(selectedCal, (dialog, calendar) -> {
                    selectedCal = calendar;
                    PreferenceUtility.open(this).saveCalendar(PreferenceUtility.TAG_SELECTED_CALENDER , selectedCal);
                    dialog.dismiss();

                    TimePickerDialogFragment timePickerDialog = new TimePickerDialogFragment();
                    timePickerDialog.setOnDateSet(selectedCal , (dialog1, calendar1) -> {
                        selectedCal = calendar1;
                        PreferenceUtility.open(this).saveCalendar(PreferenceUtility.TAG_SELECTED_CALENDER , selectedCal);
                        dialog1.dismiss();
                        startCountDown();
                    });
                    timePickerDialog.show(getSupportFragmentManager()  , "timePicker");

                });
                datePickerDialogFragment.show(getSupportFragmentManager() , "datePicker");
                break;

            case R.id.addCover:
                pickImage( uri -> {
                    ButterKnife.bind(this);
                    this.coverUri = uri;
                    PreferenceUtility.open(this).saveUri(PreferenceUtility.TAG_COVER_URI , coverUri);
                    changeCover();
                });
                break;

            case R.id.addPlan:
                startActivityForResult(new Intent(this , AddPlanActivity.class) , ADD_PLAN_REQUEST);
                break;
        }
    }

    private void changeCover() {
        if (coverUri == null) return;
        Picasso.with(this).load(coverUri)
                .placeholder(R.mipmap.cover_photo)
                .error(R.mipmap.cover_photo)
                .into(cover);
    }

    @Override
    public void setonTipsLoaded(TipsModel tipsModel) {
        if (tipsModel.code == 200){
            tipsProgress.setVisibility(View.GONE);
            for (TipsModel.Tip tip : tipsModel.data){
                tipsAdapter.addItem(tip);
            }
        }
    }


    @Override
    public void setonTodoLoaded(TodoModel todoModel) {
        if (todoModel.code == 200){
            todoProgress.setVisibility(View.GONE);
            for (TodoModel.Todo todo : todoModel.data){
                todosAdapter.addItem(todo);
            }
        }
    }


    NumberFormat formatter = new DecimalFormat("00");
    private void startCountDown() {
        now = Calendar.getInstance();
        if (counterDown != null) counterDown.cancel();
        final long totalTime = selectedCal.getTimeInMillis() - now.getTimeInMillis();
        counterDown = new CountDownTimer( totalTime , 1000) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                long h = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(
                        TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
                long m = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                long s = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                daysText.setText(formatter.format(days));
                hoursText.setText(formatter.format(h));
                minsText.setText(formatter.format(m));
                secText.setText(formatter.format(s));

            }


            @Override
            public void onFinish() {

            }
        }.start();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_PLAN_REQUEST) {
                Gson gson = new Gson();
                Plan plan = gson.fromJson(data.getStringExtra(AddPlanActivity.TAG_PLAN) , Plan.class);

                plansListView.postDelayed(() -> plansAdapter.addItem(plan), 1000);

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferenceUtility.open(this).savePlans(PreferenceUtility.TAG_PLANS_ITEMS , plansAdapter.getArray());
    }
}
