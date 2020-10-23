package com.shesha.projects.paynparkapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shesha.projects.paynparkapp.ParkingSpot;
import com.shesha.projects.paynparkapp.PaymentDetailsActivity;
import com.shesha.projects.paynparkapp.R;

import java.util.ArrayList;

public class ParkingSpotAdapter extends RecyclerView.Adapter<ParkingSpotAdapter.ParkingViewHolder> {
    private ArrayList<ParkingSpot> parkingSpots;
    private Context context;
    private FragmentManager fragmentManager;

    public ParkingSpotAdapter(ArrayList<ParkingSpot> parkingSpots, Context context, FragmentManager fragmentManager)
    {
        this.parkingSpots = parkingSpots;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_item, parent, false);
        ParkingViewHolder viewHolder = new ParkingViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ParkingViewHolder holder, int position) {
        ParkingSpot parkingSpot = parkingSpots.get(position);
        holder.parkingSpotName.setText(parkingSpot.getLocationName());
        holder.parkingAddressLine.setText(parkingSpot.getAddressLine());
        holder.parkingAddressLocality.setText(parkingSpot.getAddressLocality());
        holder.parkingSpotVicinity.setText(parkingSpot.getVicinitySpot());
        holder.bookSpotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                If user decides to book this parking spot, navigate to next screen to record further details.
                 */
                Intent intent = new Intent(context,PaymentDetailsActivity.class);
                intent.putExtra("location",holder.parkingSpotName.getText().toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return parkingSpots.size();
    }



    public class ParkingViewHolder extends RecyclerView.ViewHolder
    {
        private TextView parkingSpotName;
        private TextView parkingAddressLine;
        private TextView parkingAddressLocality;
        private TextView parkingSpotVicinity;
        private Button bookSpotBtn;

        public ParkingViewHolder(@NonNull View itemView) {
            super(itemView);
            parkingSpotName = itemView.findViewById(R.id.parkingSpotName);
            parkingAddressLine = itemView.findViewById(R.id.addressLineTextView);
            parkingAddressLocality = itemView.findViewById(R.id.addressLocalityTextView);
            parkingSpotVicinity = itemView.findViewById(R.id.addressVicinityTextView);
            bookSpotBtn = itemView.findViewById(R.id.bookSpotButton);
        }
    }

}
