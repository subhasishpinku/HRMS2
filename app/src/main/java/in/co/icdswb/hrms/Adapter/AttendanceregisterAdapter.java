package in.co.icdswb.hrms.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.co.icdswb.hrms.AdapterList.AttendanceregisterList;
import in.co.icdswb.hrms.R;

public class AttendanceregisterAdapter  extends RecyclerView.Adapter<AttendanceregisterAdapter.ViewHolder> {
    private Context mCtx;
    private Activity mactivity;
    private List<AttendanceregisterList> attendanceregisterLists;
    // for load more
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private ArrayList<HashMap<String,String>> mDataset;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private OnItemClickListener listener;
    private OnLoadMoreListener mOnLoadMoreListener;

    public interface OnItemClickListener {
        void onItemClick(HashMap<String, String> item);
    }
    public interface OnLoadMoreListener {
        void onLoadMore();
    }
    public void add(int position, HashMap<String,String> item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }
    public AttendanceregisterAdapter(Context mCtx, List<AttendanceregisterList> attendanceregisterLists, RecyclerView recyclerView){
        this.mCtx = mCtx;
        this.attendanceregisterLists = attendanceregisterLists;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapterattendenceregister, parent, false);
//        return new ViewHolder(view);
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapterattendenceregister, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progressbar, parent, false);
            return new ViewHolderLoading(view);
        }
      return null;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 int position) {
//        AttendanceregisterList attendanceregisterList = attendanceregisterLists.get(position);
//        holder.dateId.setText(attendanceregisterList.getDate());
//        holder.dayID.setText(attendanceregisterList.getDay());
//        holder.InID.setText(attendanceregisterList.getIntime());
//        holder.outID.setText(attendanceregisterList.getOuttime());
//        holder.remarksID.setText(attendanceregisterList.getRemarks());

        if (holder instanceof ViewHolder) {
            AttendanceregisterList attendanceregisterList = attendanceregisterLists.get(position);
            ViewHolder userViewHolder = (ViewHolder) holder;
            userViewHolder.dateId.setText(attendanceregisterList.getDate());
            userViewHolder.dayID.setText(attendanceregisterList.getDay());
            userViewHolder.InID.setText(attendanceregisterList.getIntime());
            userViewHolder.outID.setText(attendanceregisterList.getOuttime());
            userViewHolder.remarksID.setText(attendanceregisterList.getRemarks());
            userViewHolder.branchID.setText(attendanceregisterList.getInbranch()+" "+"||"+" "+attendanceregisterList.getOutbranch());
        } else if (holder instanceof ViewHolderLoading) {
            ViewHolderLoading loadingViewHolder = (ViewHolderLoading) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
        }




    @Override
    public int getItemCount() {
        return attendanceregisterLists == null ? 0 : attendanceregisterLists.size();
//         return        attendanceregisterLists.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public void setOnItemListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return attendanceregisterLists.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoaded() {
        isLoading = false;
    }
    private class ViewHolderLoading extends ViewHolder {
        public ViewHolderLoading(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.itemProgressbar);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView dateId,dayID;
       TextView InID,outID,remarksID,branchID;
        ProgressBar progressBar;;
        public ViewHolder(View itemView) {
            super(itemView);
            dateId = itemView.findViewById(R.id.dateId);
            dayID = itemView.findViewById(R.id.dayID);
            InID = itemView.findViewById(R.id.InID);
            outID = itemView.findViewById(R.id.outID);
            remarksID = itemView.findViewById(R.id.remarksID);
            branchID = itemView.findViewById(R.id.branchID);

        }

        public void bind(final HashMap<String,String> item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }



}