package com.example.xyzreader.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xyzreader.R;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailTextAdapter extends RecyclerView.Adapter<ArticleDetailTextAdapter.ViewHolder> {
    Context context;
    List<String> articleList = new ArrayList<>();

    public ArticleDetailTextAdapter(Context c, String article) {
        context = c;
        articleList = articleToArray(article);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int textLayout = R.layout.article_detail_holder;
        View view = inflater.inflate(textLayout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.text_iv.setText(articleList.get(position));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    private List<String> articleToArray(String article) {
        int stringTrim = 500;
        List<String> artcilesArray = new ArrayList<String>();
        for (int i = 0; i <= article.length(); i++) {
            try {
                int nextNewline = article.indexOf("\n", i + stringTrim);
                artcilesArray.add(article.substring(i, nextNewline));
                i = nextNewline + 1;
            } catch (Exception e) {
                break;
            }
        }

//        String artcilesStrings[] = article.split("\\r?\\n");
        //       artcilesArray = Arrays.asList(artcilesStrings);
        return artcilesArray;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView text_iv;

        public ViewHolder(View itemView) {
            super(itemView);
            text_iv = itemView.findViewById(R.id.article_text);

        }
    }
}
