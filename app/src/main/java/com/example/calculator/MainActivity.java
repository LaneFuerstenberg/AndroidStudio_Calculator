package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Calculator calculator;
    Map<Button, String> buttons = new HashMap<>();
    String[] str_num = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    List<String> numbers = Arrays.asList(str_num);
    Map<String, String> operations = new HashMap<String, String>(){{
        put("times","*");
        put("divide","/");
        put("plus","+");
        put("minus","-");
        put("exponent","^2");
        put("exponent2","^");
    }};
    String[] str_buttons = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "times", "divide", "plus", "minus", "exponent", "exponent2", "C", "CE", "back", "dot", "PlusMinus", "equals"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculator = new Calculator();
        for (String butts : str_buttons) {
            buttons.put((Button) findViewById(getResources().getIdentifier("btn_" + butts, "id",
                    this.getPackageName())) , butts);

        }
        for (Map.Entry<Button, String> entry : buttons.entrySet()) {
            Button b = (entry.getKey());
            b.setOnClickListener(this);
        }
    }
    //TODO: Add sliding menu from side for advanced operations
    //TODO: Improve overall gui
    //TODO: Fix decimal issue

    private void renderElements() {
        String display = calculator.outputDisplay();
        TextView txt_display = findViewById(R.id.txt_display);
        txt_display.setText(display);
    }

    private void equalsPressed() {
        TextView display = findViewById(R.id.txt_display);
        display.setText(calculator.calculate());
    }

    @Override
    public void onClick(View v) {
        if(v != null) {
            Button click = (Button) v;
            if (numbers.contains(buttons.get(click))) {
                calculator.receiveNumber(buttons.get(click));
            } else if (operations.containsKey(buttons.get(click))) {
                calculator.receiveOperator(operations.get(buttons.get(click)));
            } else if (buttons.get(click).equals("C")) {
                calculator.clear();
            } else if (buttons.get(click).equals("CE")) {
                calculator.clearEntry();
            } else if (buttons.get(click).equals("equals")) {
                equalsPressed();
            } else if (buttons.get(click).equals("dot")) {
                calculator.receiveDot();
            } else if (buttons.get(click).equals("PlusMinus")) {
                calculator.receiveNegative();
            } else if (buttons.get(click).equals("back")) {
                calculator.deleteLast();
            }
            renderElements();
        }
    }
}

