package com.example.android.caffeinefix;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void submitOrder(View view) {
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        EditText editText = findViewById(R.id.enter_name);
        String nameText = editText.getText().toString();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, nameText);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, "");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email, nameText));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @NonNull
    private String createOrderSummary(int price, boolean whippedCream, boolean chocolate, String name) {

        String message = getString(R.string.order_summary_name, name) +
                "\n" + getString(R.string.has_whipped_cream, whippedCream) +
                "\n" + getString(R.string.has_chocolate, chocolate) +
                "\n" + getString(R.string.quantity_of_coffee, quantity) +
                "\n" + getString(R.string.total, NumberFormat.getCurrencyInstance().format(price)) +
                "\n" + getString(R.string.thank_you);
        return message;
    }

    private int calculatePrice(boolean whippedCream, boolean chocolate) {

        int pricePerCup = 5;
        int priceOfWhippedCream = 1;
        int priceOfChocolate = 2;
        if (whippedCream) {
            pricePerCup += priceOfWhippedCream;
        }

        if (chocolate) {
            pricePerCup += priceOfChocolate;
        }

        return quantity * pricePerCup;

    }

    public void increment(View view) {
        if (quantity < 100) {
            quantity++;
            displayQuantity(quantity);
        } else {
            Toast toast = Toast.makeText(this, getString(R.string.toast_more), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void decrement(View view) {
        if (quantity > 1) {
            quantity--;
            displayQuantity(quantity);
        } else {
            Toast toast = Toast.makeText(this, getString(R.string.toast_less), Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}