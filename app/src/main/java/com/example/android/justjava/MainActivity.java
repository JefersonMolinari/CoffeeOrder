package com.example.android.justjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private int quantity;
    private double price;
    private double total;
    private static final int MAX_AMOUNT = 100;
    boolean topping = false;
    boolean addChocolate = false;
    boolean addWhippedCream = false;
    TextView priceTextView;
    CheckBox toppingWhipCream;
    CheckBox toppingChocolate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setQuantity(0);
        setPrice(5);
        setContentView(R.layout.activity_main);
        priceTextView = findViewById(R.id.textView_value);
        toppingWhipCream = findViewById(R.id.checkBox_cream);
        toppingChocolate = findViewById(R.id.checkBox_chocolate);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal() {
        this.total = quantity * price;
    }

    public void submitOrder(View v){
        closeKeyboard();
        EditText nameTextView = findViewById(R.id.editText_name);
        String name = nameTextView.getText().toString();
        String appName = getString(R.string.app_name);

        createOrderSummary();
        String subject = getString(R.string.order_summary_subject,appName,name);
        String orderMessage = createBodyMessage(name);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, orderMessage);

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else{
            Toast toast = Toast.makeText(this, "Couldn't send email", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public String createBodyMessage(String name){
        String priceMessage = getString(R.string.order_summary_name, name);
        if(topping) priceMessage += String.format("\n\n%s:", getString(R.string.topping));
        if(addWhippedCream) priceMessage += String.format("\n%s", getString(R.string.whipped_cream));
        if(addChocolate) priceMessage+= String.format("\n%s", getString(R.string.chocolate));
        priceMessage += String.format("\n\n%s\n%s\n\n%s",
                getString(R.string.order_summary_quantity,quantity),
                getString(R.string.order_summary_total, NumberFormat.getCurrencyInstance().format(total)),
                getString(R.string.thank_you));
        return priceMessage;
    }

    public void createOrderSummary(){
        closeKeyboard();
        setTotal();

        if(toppingWhipCream.isChecked()){
            topping = true;
            addWhippedCream = true;
            total += quantity;
        }

        if(toppingChocolate.isChecked()){
            topping = true;
            addChocolate = true;
            total += 2*quantity;
        }
        priceTextView.setText(String.format("%s", NumberFormat.getCurrencyInstance().format(total)));
    }

    public void displayQuantity(int quantity){
        TextView quantityTextView = findViewById(R.id.textView_qty_value);
        quantityTextView.setText(String.format("%d", quantity));
    }

    public void increment(View v){
        if (quantity < MAX_AMOUNT) displayQuantity(++quantity);
        createOrderSummary();
    }

    public void decrement(View v){
        if (quantity > 0) displayQuantity(--quantity);
        createOrderSummary();
    }

    public void checkBox (View v){
        createOrderSummary();
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
