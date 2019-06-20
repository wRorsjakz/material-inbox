package com.example.bottomappbar.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bottomappbar.Entity.Email;
import com.example.bottomappbar.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainFragmentRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_VIEW_TYPE = 0;
    private static final int ITEM_VIEW_TYPE = 1;
    private static final int LAST_ITEM_VIEW_TYPE = 2;

    private Context context;
    private List<Email> emailList = new ArrayList<>();
    private int[] images; // An array of profile pics
    private String date;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private mainFragmentItemClickListener mListener;

    public interface mainFragmentItemClickListener {
        void itemClick(Email email, int position);
    }

    public void setmListener(mainFragmentItemClickListener mListener) {
        this.mListener = mListener;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MainFragmentRVAdapter(Context context, int[] images, String date) {
        this.context = context;
        this.images = images;
        this.date = date;
    }

    // RecyclerView Header ViewHolder
    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView date;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.rvHeader_date);
        }
    }

    // RecyclerView Item ViewHolder
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout layoutContainer;
        private CircleImageView profilePic;
        private TextView subject, sender, content;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutContainer = itemView.findViewById(R.id.rv_item_container);
            profilePic = itemView.findViewById(R.id.rvItem_profile);
            subject = itemView.findViewById(R.id.rvItem_subject);
            sender = itemView.findViewById(R.id.rvItem_sender);
            content = itemView.findViewById(R.id.rvItem_content);

            layoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != 0 && position != emailList.size() - 1) {
                        if (mListener != null) {
                            mListener.itemClick(emailList.get(position), position);
                        }
                    }
                }
            });
        }
    }

    public void setEmailList(List<Email> emailList) {
        // Add placeholder item to the first position
        emailList.add(0, new Email(null, null, null, null, 999));
        // Add placeholder item to the last position
        emailList.add(new Email(null, null, null, null, 999));
        this.emailList = emailList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == HEADER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.mainfragment_rv_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.mainfragment_rv_item, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == HEADER_VIEW_TYPE) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.date.setText(date);
        } else if (holder.getItemViewType() == LAST_ITEM_VIEW_TYPE) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.subject.setVisibility(View.GONE);
            itemViewHolder.sender.setVisibility(View.GONE);
            itemViewHolder.content.setVisibility(View.GONE);
            itemViewHolder.profilePic.setVisibility(View.GONE);
        } else {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Email currentEmail = emailList.get(position);
            itemViewHolder.subject.setText(currentEmail.getSubject());
            itemViewHolder.sender.setText(currentEmail.getSender());
            String content = "- " + currentEmail.getContent();

            itemViewHolder.content.setText(content);
            Glide.with(context).load(images[currentEmail.getProfilePic()]).into(itemViewHolder.profilePic);
        }

    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_VIEW_TYPE;
        else if (position == emailList.size() - 1)
            return LAST_ITEM_VIEW_TYPE;
        else
            return ITEM_VIEW_TYPE;
    }
}
