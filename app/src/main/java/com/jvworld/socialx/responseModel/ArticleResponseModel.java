package com.jvworld.socialx.responseModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class ArticleResponseModel {

    @SerializedName("articles")
    private ArrayList<ArticleDataModel> articleDataModels;

    public ArrayList<ArticleDataModel> getArticleDataModels() {
        return articleDataModels;
    }

    public void setArticleDataModels(ArrayList<ArticleDataModel> articleDataModels) {
        this.articleDataModels = articleDataModels;
    }
}
