package dev.m.hussein.jobtask.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.m.hussein.jobtask.R;
import dev.m.hussein.jobtask.model.Plan;
import dev.m.hussein.jobtask.ui.custom.checkbox.SmoothCheckBox;
import dev.m.hussein.jobtask.ui.custom.edittext.InputFilterMinMax;
import dev.m.hussein.jobtask.ui.custom.seekbar.CustomSeekBar;

public class AddPlanActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG_PLAN = "TAG_PLAN";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.locationCheckbox) LinearLayout locationCheckbox;
    @BindView(R.id.budgetCheckbox) LinearLayout budgetCheckbox;
    @BindView(R.id.locationEditText) AppCompatAutoCompleteTextView locationAutoCompleteTextView;
    @BindView(R.id.budgetEditText) AppCompatEditText budgetEditText;
    @BindView(R.id.inviteesSeekbar) CustomSeekBar seekBar;
    @BindView(R.id.locaCheckbox) SmoothCheckBox locCheckbox;
    @BindView(R.id.budgCheckbox) SmoothCheckBox budgCheckbox;
    @BindView(R.id.next) AppCompatButton save;

    double min = 10000.0;
    double max = 100000000.0;

    String [] cities;

    String location = "";

    Plan plan = new Plan();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        ButterKnife.bind(this);


        setupViews();
    }

    private void setupViews() {
        cities = getResources().getStringArray(R.array.cities);
        toolbar.setNavigationOnClickListener(view -> supportFinishAfterTransition());

        locationCheckbox.setOnClickListener(this);
        budgetCheckbox.setOnClickListener(this);
        save.setOnClickListener(this);


        seekBar.setSelectedMaxValue(200);


        budgetEditText.setText(String.valueOf(min));
        budgetEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter limitFilter = new InputFilterMinMax(min, max);
        budgetEditText.setFilters(new InputFilter[] { limitFilter });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,cities);
        locationAutoCompleteTextView.setThreshold(1);
        locationAutoCompleteTextView.setAdapter(adapter);

        locCheckbox.setOnCheckedChangeListener((checkBox, isChecked) -> {
            if (isChecked){

                locationAutoCompleteTextView.setText(R.string.any);
            }else{
                locationAutoCompleteTextView.setText(location);
            }
            locationAutoCompleteTextView.setEnabled(!isChecked);
        });

        budgCheckbox.setOnCheckedChangeListener((checkBox, isChecked) -> {
            budgetEditText.setEnabled(!isChecked);
        });

        locationAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                location = editable.toString();
            }
        });
    }

    private void save() {
        plan.budget = budgCheckbox.isChecked() ? 0.0 : Double.parseDouble(budgetEditText.getText().toString());
        plan.location = (location.isEmpty() || locCheckbox.isChecked()) ? getString(R.string.any) : location;
        plan.invitees = seekBar.getSelectedMaxValue().intValue();



        Gson gson = new Gson();
        Intent output = new Intent();
        output.putExtra(TAG_PLAN, gson.toJson(plan));
        setResult(RESULT_OK, output);

        supportFinishAfterTransition();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.locationCheckbox:
                locCheckbox.toggle();
                break;

            case R.id.budgetCheckbox:
                budgCheckbox.toggle();
                break;

            case R.id.next:
                save();
                break;
        }
    }
}
