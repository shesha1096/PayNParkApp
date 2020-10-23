package com.shesha.projects.paynparkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentDetailsActivity extends AppCompatActivity implements PaymentResultListener {
    private EditText durationEditText;
    private TextView locationTextView;
    private Button makePaymentButton;
    private int cost;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        Checkout.preload(getApplicationContext());
        durationEditText = findViewById(R.id.durationEditText);
        locationTextView = findViewById(R.id.parkingLocationTextView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        locationTextView.setText(getIntent().getStringExtra("location"));
        makePaymentButton = findViewById(R.id.paymentButton);
        makePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (durationEditText.getText().toString().equals(""))
                {
                    Toast.makeText(PaymentDetailsActivity.this,"Please enter the duration",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    int duration = Integer.valueOf(durationEditText.getText().toString());
                    if (duration <= 5)
                    {
                        cost = 5;
                    }
                    else if (duration > 5 && duration <= 10)
                    {
                        cost = 10;
                    }
                    else
                    {
                        cost = 15;
                    }
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentDetailsActivity.this);
                    alertDialogBuilder.setTitle("Payment Confirmation");
                    alertDialogBuilder.setMessage("You will be charged $"+cost+" for a duration of "+duration+" hours");
                    alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startPayment();

                        }
                    })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        });
    }

    /*
    This method is used from the RazorPay docs for performing a transaction. Current the key being used is for test purposes only.
     */
    public void startPayment() {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_NZEJlftDS78cIj");

        /**
         * Set your logo here
         */
        //checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            /*
            Set necessary details to pass to the RazorPay Payment API for further processing.
             */
            options.put("name", "Pay N Park App");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(cost*50*100));//pass amount in currency subunits
            options.put("prefill.email", "shesha1096@gmail.com");
            options.put("prefill.contact","0449807560");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("ERROR", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(PaymentDetailsActivity.this,"Payment Successful",Toast.LENGTH_SHORT).show();
        Map<String,String> historyMap = new HashMap<>();
        historyMap.put("Location",getIntent().getStringExtra("location"));
        historyMap.put("Duration",String.valueOf(durationEditText.getText().toString()));
        historyMap.put("Cost",String.valueOf(cost));
        String userId = FirebaseAuth.getInstance().getUid();
        /*
        Once payment is successful, add the booking details to a collection called Parking History, so that the user can view their past bookings at a later stage.
         */
        firebaseFirestore.collection("Users")
                .document(userId)
                .collection("Parking History")
                .add(historyMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(PaymentDetailsActivity.this,"Booking Activated",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(PaymentDetailsActivity.this,"Payment Error",Toast.LENGTH_LONG).show();

    }
}