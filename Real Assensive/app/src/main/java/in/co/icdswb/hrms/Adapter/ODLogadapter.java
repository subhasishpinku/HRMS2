package in.co.icdswb.hrms.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.co.icdswb.hrms.ActivityUrl.CleanAdapterInterface;
import in.co.icdswb.hrms.AdapterList.ODLogList;
import in.co.icdswb.hrms.MapViewLatLang;
import in.co.icdswb.hrms.R;

public class ODLogadapter  extends RecyclerView.Adapter<ODLogadapter.ViewHolder> {
    private Context mCtx;
    private List<ODLogList> odLogLists;
    String type2;
    String  colorcode;
    private static int currentPosition = 0;
    public ODLogadapter(Context mCtx,List<ODLogList> odLogLists){
        this.mCtx = mCtx;
        this.odLogLists = odLogLists;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_odlogadapter, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,
                                 int position) {
        ODLogList logList = odLogLists.get(position);
        holder.timeId.setText(logList.getTime());
        holder.locationId.setText(logList.getAddress());
        holder.odtypeID.setText(logList.getOdtype());
        holder.latId.setText(logList.getLat());
        holder.langId.setText(logList.getLang());
        colorcode = logList.getColorcode();
        holder.odtypeID.setTextColor(Color.parseColor(colorcode));
        type2 = logList.getOdtype();
        Log.e("BLUE", colorcode);
        holder.viewmapId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitude = holder.latId.getText().toString();
                String langtitude = holder.langId.getText().toString();
                String place = holder.locationId.getText().toString();
                Intent intent = new Intent(mCtx, MapViewLatLang.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("langtitude",langtitude);
                intent.putExtra("place",place);
                mCtx.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return odLogLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView,viewmapId;
        TextView timeId,locationId,odtypeID,latId,langId;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            timeId = itemView.findViewById(R.id.timeId);
            locationId = itemView.findViewById(R.id.locationId);
            odtypeID = itemView.findViewById(R.id.odtypeID);
            viewmapId = itemView.findViewById(R.id.viewmapId);
            latId = itemView.findViewById(R.id.latId);
            langId = itemView.findViewById(R.id.langId);
        }

    }
    public void clearApplications() {
        int size = this.odLogLists.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                odLogLists.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }


    }
    public void clear(CleanAdapterInterface cleanAdapterInterface) {
        final int size = odLogLists.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                odLogLists.remove(0);
            }

            notifyItemRangeRemoved(0, size);
            cleanAdapterInterface.cleanStatus(true);
        }

        else
            cleanAdapterInterface.cleanStatus(false);
    }



}