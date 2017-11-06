package dev.m.hussein.jobtask.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dev.m.hussein.jobtask.model.TipsModel;
import dev.m.hussein.jobtask.model.TodoModel;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Dev. M. Hussein on 11/5/2017.
 */

public interface ApiService {

    class Constants {
        // api service links.
        public static final String BASE_SERVER_URI = "http://www.thejerb.com/jerb/public/api/";
        public static final String BASE_IMAGE_URI = "http://www.thejerb.com/jerb/public/uploads/tips/";

    }

    /**
     * get tips method
     */
    @GET("tips")
    Call<ResponseBody> getTips();

    /**
     * get Todos method
     */
    @POST("guest_todos")
    Call<ResponseBody> getTodos();


    class connection {



        private static Retrofit open() {

            return new Retrofit.Builder()
                    .baseUrl(Constants.BASE_SERVER_URI)
                    .build();

        }




        /**
         * load tips data
         *
         *
         * @param onTipsLoaded using send service data to ui.
         * */
        public static Call<ResponseBody> loadTips(final OnTipsLoaded onTipsLoaded) {
            Retrofit retrofit = ApiService.connection.open();
            ApiService apiService = retrofit.create(ApiService.class);
            // create server request based on request type.

            Call<ResponseBody> call = apiService.getTips();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.code() != 200) {
                            onTipsLoaded.setonTipsLoaded(null);
                            return;
                        }
                        String stData = response.body().string();
                        Gson gson = new GsonBuilder().create();
                        TipsModel tipsModel = gson.fromJson(stData , TipsModel.class);
                        onTipsLoaded.setonTipsLoaded(tipsModel);
                    } catch (IOException e) {
                        e.printStackTrace();
                        call.cancel();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();

                }
            });
            return call;
        }




        /**
         * load todos data
         *
         *
         * @param onTodoLoaded using send service data to ui.
         * */
        public static Call<ResponseBody> loadTodo(final OnTodoLoaded onTodoLoaded) {
            Retrofit retrofit = ApiService.connection.open();
            ApiService apiService = retrofit.create(ApiService.class);
            // create server request based on request type.

            Call<ResponseBody> call = apiService.getTodos();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.code() != 200) {
                            onTodoLoaded.setonTodoLoaded(null);
                            return;
                        }
                        String stData = response.body().string();
                        Gson gson = new GsonBuilder().create();
                        TodoModel tipsModel = gson.fromJson(stData , TodoModel.class);
                        onTodoLoaded.setonTodoLoaded(tipsModel);
                    } catch (IOException e) {
                        e.printStackTrace();
                        call.cancel();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();

                }
            });
            return call;
        }





    }



    public interface OnTipsLoaded {
        void setonTipsLoaded(TipsModel tipsModel);
    }

    public interface OnTodoLoaded {
        void setonTodoLoaded(TodoModel todoModel);
    }
}
