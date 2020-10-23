package com.shesha.projects.paynparkapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shesha.projects.paynparkapp.BookingDetail;
import com.shesha.projects.paynparkapp.R;

import java.util.ArrayList;

public class ParkingHistoryRecyclerAdapter extends RecyclerView.Adapter<ParkingHistoryRecyclerAdapter.ParkingHistoryViewHolder> {

    private Context context;
    private ArrayList<BookingDetail> bookingDetails;

    public ParkingHistoryRecyclerAdapter(ArrayList<BookingDetail> bookingDetails, Context context)
    {
        this.bookingDetails = bookingDetails;
        this.context = context;
    }


    @NonNull
    @Override
    public ParkingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parking_history_item, parent, false);
        ParkingHistoryViewHolder viewHolder = new ParkingHistoryRecyclerAdapter.ParkingHistoryViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingHistoryViewHolder holder, int position) {
        BookingDetail bookingDetail = bookingDetails.get(position);
        holder.parkingSpotName.setText(bookingDetail.getLocation());
        holder.parkingAddressLocality.setText(bookingDetail.getDuration()+" hours");
        holder.parkingSpotVicinity.setText("$"+bookingDetail.getCost());

    }

    @Override
    public int getItemCount() {
        return bookingDetails.size();
    }

    public class ParkingHistoryViewHolder extends RecyclerView.ViewHolder
    {
        private TextView parkingSpotName;
        private TextView parkingAddressLine;
        private TextView parkingAddressLocality;
        private TextView parkingSpotVicinity;

        public ParkingHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            parkingAddressLine = itemView.findViewById(R.id.parkingHistoryAddressLineTextView);
            parkingSpotName = itemView.findViewById(R.id.parkingHistorySpotName);
            parkingAddressLocality = itemView.findViewById(R.id.parkingHistoryAddressLocalityTextView);
            parkingSpotVicinity = itemView.findViewById(R.id.addressVicinityTextView);
        }
    }
}
