package com.gwazasoftwares.abhes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.gwazasoftwares.abhes.Details;
import com.gwazasoftwares.abhes.R;
import com.gwazasoftwares.abhes.interfaces.OnPopupItemSelected;
import com.gwazasoftwares.abhes.models.Emergency;

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.EmergencyViewHolder> {

    List<Emergency> emergencies;
    private Context mCtx;
    private OnPopupItemSelected popUpItemSelected;

    public EmergencyAdapter(List<Emergency> emergencies, Context mCtx, OnPopupItemSelected popUpItemSelected) {
        this.emergencies = emergencies;
        this.mCtx = mCtx;
        this.popUpItemSelected = popUpItemSelected;
    }

    @NonNull
    @Override
    public EmergencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emergency_row, parent,false);
        EmergencyViewHolder emergencyViewHolder = new EmergencyViewHolder(view);
        return emergencyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EmergencyViewHolder holder, int position) {
        holder.description.setText(emergencies.get(position).getDescription());
        holder.title.setText(emergencies.get(position).getTitle());
        holder.image.setImageResource(emergencies.get(position).getImage());

        holder.actions.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.actions);
                //inflating menu from xml resource
                popup.inflate(R.menu.emergency_actions_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        popUpItemSelected.select(id);

                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return emergencies.size();
    }

    class EmergencyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView  title, description;
        Button actions;

        public EmergencyViewHolder( View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.emergencyImage);
            title = itemView.findViewById(R.id.txttile);
            description = itemView.findViewById(R.id.txtdesc);
            actions = itemView.findViewById(R.id.btnactions);

        }


    }
}
