package com.tuwien.buildinginteractioninterfaces.prototype.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tuwien.buildinginteractioninterfaces.prototype.R;

/**
 * Created by duarte on 27-01-2018.
 */

public class MainMenu extends AppCompatActivity {

    private static final String TAG_ACTIVITY =
                                   MainMenu.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = (Button) findViewById(R.id.button_create_game);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
        case R.id.main_menu_time:
             if (checked)
                 displayNumberSelection("Time (seconds)");
             break;
        case R.id.main_menu_num_words:
            if (checked)
                displayNumberSelection("Number of words");
            break;
        case R.id.main_menu_errors:
            if (checked)
                displayNumberSelection("Number of errors");
                break;
        case R.id.main_menu_correct_words:
            if (checked)
                displayNumberSelection("Number of correct words");
                break;
        case R.id.main_menu_text:
            if (checked)

                break;
        case R.id.main_menu_no_end:
            if (checked)
                ((TextView) findViewById(R.id.label_main_menu_num)).setVisibility(View.GONE);

                break;

        default:

            break;
       }
    }

    private void displayNumberSelection(String text){
        TextView textView = (TextView) findViewById(R.id.label_main_menu_num);
        textView.setText(text);
        textView.setVisibility(View.VISIBLE);
    }
}
