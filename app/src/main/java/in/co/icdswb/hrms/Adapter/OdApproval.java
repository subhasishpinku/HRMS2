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

import in.co.icdswb.hrms.AdapterList.OdApprovalList;
import in.co.icdswb.hrms.OutdoorViewActivity;
import in.co.icdswb.hrms.R;

public class OdApproval  extends RecyclerView.Adapter<OdApproval.ViewHolder> {
    private Context mCtx;
    private List<OdApprovalList> odapprovalLists;
    String idta,dateOD;
    private static int currentPosition = 0;
    int statusRm;
    int statusHr;
    int statusAcc;
    int statusFinal;
    ObjectAnimator textColorAnim;
    public OdApproval(Context mCtx,List<OdApprovalList> odapprovalLists){
        this.mCtx = mCtx;
        this.odapprovalLists = odapprovalLists;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapterodapproval, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,
                                 final int position) {
        OdApprovalList odapproval = odapprovalLists.get(position);
        holder.viewIDD.setText(odapproval.getId());
        holder.dateId.setText(odapproval.getDay());
        holder.dayID.setText(odapproval.getDate());
        holder.inTimeID.setText(odapproval.getInTime());
        holder.outTimeID.setText(odapproval.getOutTime());
        holder.durationID.setText(odapproval.getDuration());
        statusRm = Integer.parseInt(odapproval.getRmApprove());
        Log.e("Rm", String.valueOf(statusRm));
        statusHr = Integer.parseInt(odapproval.getHrApprove());
        statusAcc = Integer.parseInt(odapproval.getCeoApprove());
        statusFinal = Integer.parseInt(odapproval.getFinalStatus());
        if (statusRm==0){
            holder.rmID.setBackgroundResource(R.drawable.ic_close_black_24dp);
        }
        else if (statusRm==2){
            holder.rmID.setBackgroundResource(R.drawable.ic_close_black_24dp);
        }
        else {
            holder.rmID.setBackgroundResource(R.drawable.ic_check_black_24dp);

        }

        if (statusHr==0){

            holder.hmID.setBackgroundResource(R.drawable.ic_close_black_24dp);
        }
        else if (statusHr==2){
            holder.hmID.setBackgroundResource(R.drawable.ic_close_black_24dp);
        }
        else {
            holder.hmID.setBackgroundResource(R.drawable.ic_check_black_24dp);
        }

        if (statusAcc==0){

            holder.ceoId.setBackgroundResource(R.drawable.ic_close_black_24dp);
        }
        else if (statusAcc==2){
            holder.ceoId.setBackgroundResource(R.drawable.ic_close_black_24dp);
        }
        else {
            holder.ceoId.setBackgroundResource(R.drawable.ic_check_black_24dp);
        }
        if (statusFinal==0){
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

        holder.viewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (currentPosition == position) {
//                    Intent intent = new Intent(mCtx, OutdoorViewActivity.class);
//                    mCtx.startActivity(intent);
//
//                }
                idta = holder.viewIDD.getText().toString().trim();
                dateOD = holder.dateId.getText().toString();
                Log.e("position", "onBindViewHolder() position: " + position);
                Intent intent = new Intent(mCtx, OutdoorViewActivity.class);
                Bundle bundle_edit  =   new Bundle();
                bundle_edit.putString("taid",idta);
                bundle_edit.putString("date",dateOD);
                intent.putExtras(bundle_edit);
                mCtx.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return odapprovalLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView dateId,resonId,locationID;
       TextView odtypeId,dayID,durationID,inTimeID,outTimeID,rejID;
       Button viewID,viewIDD;
       ImageView rmID,hmID,ceoId;
        public ViewHolder(View itemView) {
            super(itemView);
            dateId = itemView.findViewById(R.id.dateId);
            dayID = itemView.findViewById(R.id.dayID);
            inTimeID = itemView.findViewById(R.id.inTimeID);
            outTimeID = itemView.findViewById(R.id.outTimeID);
            durationID = itemView.findViewById(R.id.durationID);
            viewID = itemView.findViewById(R.id.viewID);
            viewIDD = itemView.findViewById(R.id.viewIDD);
            rmID = itemView.findViewById(R.id.rmID);
            hmID = itemView.findViewById(R.id.hmID);
            ceoId = itemView.findViewById(R.id.ceoId);
            rejID = itemView.findViewById(R.id.rejID);
        }

    }



}