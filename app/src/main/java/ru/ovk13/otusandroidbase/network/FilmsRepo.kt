package ru.ovk13.otusandroidbase.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ovk13.otusandroidbase.data.FilmsResponse
import java.util.*

object FilmsRepo {

    fun loadItemsPage(callback: LoadDataCallback, page: Int = 1) {
        try {
            val filmsCall = Api.service.getFilms(Api.API_KEY, Locale.getDefault().language, page)
            filmsCall.enqueue(object : Callback<FilmsResponse> {
                override fun onFailure(call: Call<FilmsResponse>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(
                    call: Call<FilmsResponse>,
                    response: Response<FilmsResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body() as FilmsResponse)
                    } else {
                        callback.onError(response.code().toString())
                    }
                }

            })
        } catch (e: Exception) {
            Log.d("exception_name", e.message.toString())
        }
    }

    interface LoadDataCallback {
        fun onSuccess(data: FilmsResponse)
        fun onError(error: String)
        fun onError(е: Throwable)
    }

}