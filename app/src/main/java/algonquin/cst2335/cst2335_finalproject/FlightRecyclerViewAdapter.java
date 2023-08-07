package algonquin.cst2335.cst2335_finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FlightRecyclerViewAdapter extends RecyclerView.Adapter<FlightRecyclerViewAdapter.MyViewHolder>{
    private final FlightRecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<FlightModel> flightModels;

    public FlightRecyclerViewAdapter(Context context, ArrayList<FlightModel> flightModels, FlightRecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.flightModels = flightModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public FlightRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This is where you inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.flight_row, parent, false);

        return new FlightRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightRecyclerViewAdapter.MyViewHolder holder, int position) {
        //Assign values to the views created in the flight_row layout file
        FlightModel obj = flightModels.get(position);
        holder.airportCode.setText(obj.getAirportCode());
        holder.dept.setText(obj.getDeparture());
        holder.status.setText(obj.getStatus());
        holder.gate.setText("Gate: " + obj.getGate());
        holder.terminal.setText("Terminal: " + obj.getTerminal());
        holder.delay.setText(String.format("%d min", obj.getDelay()));
        holder.destAirport.setText(obj.getDestination());
        holder.destAirportCode.setText(obj.getDestAirportCode());
    }

    @Override
    public int getItemCount() {
        return flightModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView airportCode;
        TextView status;
        TextView dept;
        TextView gate;
        TextView terminal;
        TextView delay;
        TextView destAirport;
        TextView destAirportCode;

        public MyViewHolder(@NonNull View itemView, FlightRecyclerViewInterface recyclerViewInterface) {

            super(itemView);

            airportCode = itemView.findViewById(R.id.airportCode);
            status = itemView.findViewById(R.id.status);
            dept = itemView.findViewById(R.id.departureTime);
            gate = itemView.findViewById(R.id.gate);
            terminal = itemView.findViewById(R.id.terminal);
            delay = itemView.findViewById(R.id.delay);
            destAirport = itemView.findViewById(R.id.destAirport);
            destAirportCode = itemView.findViewById(R.id.airportCode2);

            itemView.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(position);
                    }
                }
            });

        }


    }
}
