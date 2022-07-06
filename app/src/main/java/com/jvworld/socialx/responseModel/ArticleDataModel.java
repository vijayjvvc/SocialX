package com.jvworld.socialx.responseModel;

import com.google.gson.annotations.SerializedName;

public class ArticleDataModel {

    @SerializedName("title")
    String title;

    @SerializedName("content")
    String contents;

    @SerializedName("urlToImage")
    String image;

    @SerializedName("publishedAt")
    String time;

    String author;

    @SerializedName("source")
    SourceModel sourceModel;

    public ArticleDataModel(String title, String contents, String image, String time, String author, SourceModel sourceModel) {
        this.title = title;
        this.contents = contents;
        this.image = image;
        this.time = time;
        this.author = author;
        this.sourceModel = sourceModel;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }

    public String getAuthor() {
        return author;
    }

    public SourceModel getSourceModel() {
        return sourceModel;
    }
}
