package in.co.icdswb.hrms.Adapter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.co.icdswb.hrms.AdapterList.TaentryList;
import in.co.icdswb.hrms.R;
import in.co.icdswb.hrms.Taentryview;

public class TaentryAdapter  extends RecyclerView.Adapter<TaentryAdapter.ViewHolder> {
    private Context mCtx;
    private List<TaentryList> taentryLists;
    int statusRm;
    int statusAcc;
    int statusReject;
    String idta;
    ObjectAnimator textColorAnim;
    public TaentryAdapter(Context mCtx,List<TaentryList> taentryLists){
        this.mCtx = mCtx;
        this.taentryLists = taentryLists;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adaptertaentry, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,
                                 final int position) {

        TaentryList list = taentryLists.get(position);
        holder.dateId.setText(list.getDate());
        holder.dayID.setText(list.getDay());
        holder.purposrId.setText(list.getPurpose());
        String approve = list.getAproveamount();
        String clame = list.getClamamount();
        holder.amountID.setText(approve+ "/" +clame);
        holder.viewID.setText(list.getTaid());
        //holder.amountID.setText(list.getClamamount());

        //status = list.getStatus();
        statusRm = Integer.parseInt(list.getStatusram());
        statusAcc = Integer.parseInt(list.getStatusacc());
        statusReject =  Integer.parseInt(list.getReject());
        if (statusRm==0){
            holder.rmID.setBackgroundResource(R.drawable.ic_close_black_24dp);
        }
        else {
            holder.rmID.setBackgroundResource(R.drawable.ic_check_black_24dp);
        }
        if (statusAcc==0){
            holder.accID.setBackgroundResource(R.drawable.ic_close_black_24dp);
        }
        else {
            holder.accID.setBackgroundResource(R.drawable.ic_check_black_24dp);
        }
        if (statusReject==0){
         holder.rejID.setVisibility(View.GONE);
        }
        else {
            textColorAnim = ObjectAnimator.ofInt(holder.rejID, "textColor", Color.RED, Color.TRANSPARENT);
            textColorAnim.setDuration(1000);
            textColorAnim.setEvaluator(new ArgbEvaluator());
            textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
            textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
            textColorAnim.start();
        }
        holder.viewID1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idta = holder.viewID.getText().toString().trim();
                Log.e("position", "onBindViewHolder() position: " + position);
                Intent intent = new Intent(mCtx, Taentryview.class);
                Bundle bundle_edit  =   new Bundle();
                bundle_edit.putString("taid",idta);
                intent.putExtras(bundle_edit);
                mCtx.startActivity(intent);


            }
        });


    }
    @Override
    public int getItemCount() {
        return taentryLists.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
    TextView dateId,dayID,purposrId,amountID,rejID;
    ImageView rmID,accID;
    Button viewID,viewID1;
        public ViewHolder(View itemView) {
            super(itemView);
            dateId = itemView.findViewById(R.id.dateId);
            dayID = itemView.findViewById(R.id.dayID);
            purposrId = itemView.findViewById(R.id.purposrId);
            amountID = itemView.findViewById(R.id.amountID);
            rmID = itemView.findViewById(R.id.rmID);
            accID = itemView.findViewById(R.id.accID);
            viewID = itemView.findViewById(R.id.viewID);
            viewID1 = itemView.findViewById(R.id.viewID1);
            rejID = itemView.findViewById(R.id.rejID);
        }
    }
}
