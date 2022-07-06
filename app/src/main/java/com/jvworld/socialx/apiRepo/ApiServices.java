package com.jvworld.socialx.apiRepo;

import com.jvworld.socialx.responseModel.ArticleResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiServices {

    @GET
    Call<ArticleResponseModel> getPost(@Url String pathApi);

}
