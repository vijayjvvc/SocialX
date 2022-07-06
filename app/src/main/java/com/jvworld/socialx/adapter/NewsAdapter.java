package com.jvworld.socialx.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jvworld.socialx.R;
import com.jvworld.socialx.responseModel.ArticleDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VH> {

    private Context context;
    //    private ArrayList<NewsModel> newsModelArrayList;
    private ArrayList<ArticleDataModel> newsModelArrayList;


//    public NewsAdapter(Context context, ArrayList<NewsModel> newsModelArrayList) {
//        this.context = context;
//        this.newsModelArrayList = newsModelArrayList;
//    }


    public NewsAdapter(Context context, ArrayList<ArticleDataModel> newsModelArrayList) {
        this.context = context;
        this.newsModelArrayList = newsModelArrayList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout,
                parent, false);
        return new VH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        holder.newsTitle.setText(newsModelArrayList.get(position).getTitle());
        holder.newsContent.setText(newsModelArrayList.get(position).getContents());
        String timeData = newsModelArrayList.get(position).getTime();
        String authorData = newsModelArrayList.get(position).getAuthor();
//        holder.newsTime.setText(timeData + authorData);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date dateToday = new Date();
        String today = dateFormat.format(dateToday);

        String setBoth= "<font color=#ACA8A7>"+ holder.dateSelector(timeData,today)
                +" </font> <font color=#000>"+ authorData +"</font>";

        holder.newsTime.setText(Html.fromHtml(setBoth));

        String img = newsModelArrayList.get(position).getImage();

        if (img != null) {
            Glide.with(context).load(img).into(holder.newsImage);
        }
    }

    @Override
    public int getItemCount() {
        return newsModelArrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView newsTitle, newsTime, newsContent;
        ImageView newsImage;

        public VH(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsContent = itemView.findViewById(R.id.news_content);
            newsTime = itemView.findViewById(R.id.news_time_author);
            newsImage = itemView.findViewById(R.id.news_image_view);
        }

        private String dateSelector(String date, String today) {
            String newsDt = "";
            int hr = Integer.parseInt(today.substring(9, 11));
            int newsHr = Integer.parseInt(date.substring(11, 13));
            if (hr == newsHr) {
                int min = Integer.parseInt(today.substring(12, 14));
                int newsMin = Integer.parseInt(date.substring(14, 16));
                newsDt = String.valueOf(min - newsMin) + " minute ago";
            } else {
                newsDt = String.valueOf(hr - newsHr) + " hour ago";
            }

            return newsDt;
        }
    }
}
