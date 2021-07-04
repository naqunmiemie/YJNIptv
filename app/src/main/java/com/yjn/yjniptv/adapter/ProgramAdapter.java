package com.yjn.yjniptv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yjn.common.Utils.L;
import com.yjn.yjniptv.R;

import static com.yjn.yjniptv.data.ProgramList.programHashMap;


public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ListViewHolder> {
    private Context mContext;
    private IListSwitchChannel iListSwitchChannel;

    public interface IListSwitchChannel{
        void onClick(View view, int position);
    }

    public void programListOnclick(IListSwitchChannel iListSwitchChannel){
        this.iListSwitchChannel=iListSwitchChannel;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_program_list,null,false);
        final  ListViewHolder holder = new ListViewHolder(view);
        mContext = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
        holder.tvProgramNum.setText(String.valueOf(position));
        holder.tvProgramTitle.setText(programHashMap.get(position).getTitle());
        holder.llProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iListSwitchChannel != null){
                    iListSwitchChannel.onClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return programHashMap.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder{

        View view;
        LinearLayout llProgram;
        TextView tvProgramTitle;
        TextView tvProgramNum;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            llProgram = view.findViewById(R.id.ll_program);
            tvProgramTitle = view.findViewById(R.id.tv_program_title);
            tvProgramNum = view.findViewById(R.id.tv_program_num);

        }


    }
}
