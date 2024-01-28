package in.co.icdswb.hrms.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.co.icdswb.hrms.AdapterList.OutdoorviewList;
import in.co.icdswb.hrms.R;

public class Adapteroutdoorview extends RecyclerView.Adapter<Adapteroutdoorview.ViewHolder> {
    public Context mCtx;
    private List<OutdoorviewList> outdoorviewLists;
    private static int currentPosition = 0;
    String purposr ="-";
    String purposr1;
    public Adapteroutdoorview(Context mCtx, List<OutdoorviewList> outdoorviewLists){
        this.mCtx = mCtx;
        this.outdoorviewLists = outdoorviewLists;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapteroutdoorview,parent,false);
        return new ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        OutdoorviewList viewList = outdoorviewLists.get(position);
        viewHolder.purposeID.setText(viewList.getPuspose());
        viewHolder.timeID.setText(viewList.getTime());
        viewHolder.odtypeID.setText(viewList.getOdtype());
        viewHolder.addressID.setText(viewList.getLocation());
        purposr1 = viewHolder.purposeID.getText().toString();
        if (purposr.equals(purposr1)){
            viewHolder.lvv.setVisibility(View.GONE);
        }
        else{
            viewHolder.lvv.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return outdoorviewLists.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView purposeID,timeID,addressID,odtypeID;
        LinearLayout lvv;
        public ViewHolder(View itemView){
            super(itemView);
            purposeID = itemView.findViewById(R.id.purposeID);
            timeID = itemView.findViewById(R.id.timeID);
            odtypeID = itemView.findViewById(R.id.odtypeID);
            addressID = itemView.findViewById(R.id.addressID);
            lvv = itemView.findViewById(R.id.lvv);
        }
    }
}
