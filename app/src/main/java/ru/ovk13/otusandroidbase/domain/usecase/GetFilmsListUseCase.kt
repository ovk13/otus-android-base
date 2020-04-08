package ru.ovk13.otusandroidbase.domain.usecase

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ovk13.otusandroidbase.data.model.GetFilmsDataModel
import ru.ovk13.otusandroidbase.domain.repository.FilmsRepository

class GetFilmsListUseCase(
    private val filmsRepository: FilmsRepository
) {
    fun getFilms(page: Int = 1, callback: LoadDataCallback) {
        try {
            filmsRepository.getFilmsList(page).enqueue(object : Callback<GetFilmsDataModel> {
                override fun onFailure(call: Call<GetFilmsDataModel>, t: Throwable) {
                    callback.onError(t)
                }

                override fun onResponse(
                    call: Call<GetFilmsDataModel>,
                    response: Response<GetFilmsDataModel>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body()!!)
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
        fun onSuccess(data: GetFilmsDataModel)
        fun onError(error: String)
        fun onError(e: Throwable)
    }
}