package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel;

public class MainMenu extends AppCompatActivity {

    private static final String TAG_ACTIVITY =
                                   MainMenu.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ((RadioGroup) findViewById(R.id.game_mode_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRadioButtonClicked(checkedId);
            }
        });

        Button button = findViewById(R.id.button_create_game);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean autoCorrect = ((CheckBox) findViewById(R.id.main_menu_autocorrect)).isChecked();
                boolean skipOnFail = ((CheckBox) findViewById(R.id.main_menu_skip_on_fail)).isChecked();

                RadioGroup gameTypeRadioGroup = findViewById(R.id.game_mode_group);

                int gameModeId = gameTypeRadioGroup.getCheckedRadioButtonId();
                OptionsModel.TypeGame typeGame;
                int finishMark = 0;

                switch (gameModeId){
                    case R.id.main_menu_time:
                        typeGame = OptionsModel.TypeGame.TIME;
                        break;
                    case R.id.main_menu_num_words:
                        typeGame = OptionsModel.TypeGame.NUM_WORDS;
                        break;
                    case R.id.main_menu_errors:
                        typeGame = OptionsModel.TypeGame.NUM_ERRORS;
                        break;
                    case R.id.main_menu_correct_words:
                        typeGame = OptionsModel.TypeGame.NUM_CORRECT_WORDS;
                        break;
                    case R.id.main_menu_no_end:
                        typeGame = OptionsModel.TypeGame.NO_END;
                        break;
                    default:
                        Toast.makeText(MainMenu.this, R.string.no_game_type_error_message, Toast.LENGTH_SHORT).show();
                        return;
                }

                switch (gameModeId){
                    case R.id.main_menu_no_end:

                        break;
                    default:
                        finishMark = Integer.valueOf(String.valueOf(((EditText) findViewById(R.id.number_input)).getText()));

                        if(finishMark <= 0){
                            Toast.makeText(MainMenu.this
                                    ,getString(R.string.error_invalid_finish_mark_1)
                                            + ((TextView)findViewById(R.id.label_main_menu_number_input)).getText()
                                            + getString(R.string.error_invalid_finish_mark_2)
                                    , Toast.LENGTH_SHORT).show();
                            return;
                        }
                }

                RadioGroup sourceRadioGroup = findViewById(R.id.text_source_group);

                int sourceId = sourceRadioGroup.getCheckedRadioButtonId();
                OptionsModel.Source source;

                switch (sourceId){
                    case R.id.main_menu_12dicts:
                        source = OptionsModel.Source.TWELVE_DICTS;
                        break;
                    case R.id.main_menu_text:
                        source = OptionsModel.Source.TEXT;
                        break;
                    default:
                        Toast.makeText(MainMenu.this, R.string.no_source_error, Toast.LENGTH_SHORT).show();
                        return;
                }


                OptionsModel optionsModel = new OptionsModel(typeGame,autoCorrect, skipOnFail, source);
                optionsModel.setFinishMark(finishMark);

                startActivity(new Intent(MainMenu.this, SingleWordsModePlayGame.class).putExtra("OPTIONS", optionsModel));
            }
        });
    }

    public void onRadioButtonClicked(int id) {
        View view = findViewById(id);

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
        case R.id.main_menu_time:
             if (checked) {
                 displayNumberSelection(getString(R.string.label_type_time),60);

             }
             break;
        case R.id.main_menu_num_words:
            if (checked) {
                displayNumberSelection(getString(R.string.label_type_num_words),10);
            }
            break;
        case R.id.main_menu_errors:
            if (checked) {
                displayNumberSelection(getString(R.string.label_type_num_errors),3);
            }
            break;
        case R.id.main_menu_correct_words:
            if (checked) {
                displayNumberSelection(getString(R.string.label_type_num_correct_words),10);
            }
            break;
        case R.id.main_menu_no_end:
            if (checked)
                findViewById(R.id.number_input_layout).setVisibility(View.GONE);
            break;

        default:

            break;
       }
    }

    @SuppressLint("SetTextI18n")
    private void displayNumberSelection(String text, int defaultNumber){
        TextView textView = findViewById(R.id.label_main_menu_number_input);
        textView.setText(text);

        EditText input =  findViewById(R.id.number_input);
        input.setText(Integer.toString(defaultNumber));

        findViewById(R.id.number_input_layout).setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.benchmark_list:
                startActivity(new Intent(MainMenu.this, BenchmarksListActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(MainMenu.this, AboutActivity.class));
                break;
            default:
                return false;
        }

        return true;
    }
}
