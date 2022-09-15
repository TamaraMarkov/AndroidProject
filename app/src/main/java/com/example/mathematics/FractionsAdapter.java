package com.example.mathematics;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;

public class FractionsAdapter extends RecyclerView.Adapter<FractionsAdapter.ViewHolder> {
    private ArrayList<Fraction> fractions;

    public FractionsAdapter(ArrayList<Fraction> fractions) {
        this.fractions=fractions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Fraction fraction=fractions.get(position);
        holder.circleView.setTotalSlices(fraction.getSliceCount());
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if(id==R.id.radoGreen){
                    holder.circleView.setSelectedColor(Color.GREEN);
                }
                else if(id==R.id.radoYello){
                    holder.circleView.setSelectedColor(Color.YELLOW);
                }
                else{
                    holder.circleView.setSelectedColor(Color.GRAY);
                }
            }
        });
        holder.numberPicker.setMaxValue(fraction.getMaxSliceCount());
        holder.numberPicker.setValue(fraction.getSliceCount());
        holder.numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                holder.circleView.setTotalSlices(newVal);
                fraction.setSliceCount(newVal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fractions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleView circleView;
        private RadioGroup radioGroup;
        private NumberPicker numberPicker;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleView=itemView.findViewById(R.id.circle);
            radioGroup=itemView.findViewById(R.id.radioGroup);
            numberPicker=itemView.findViewById(R.id.numberPicker);


        }
    }
}
