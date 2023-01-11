package com.example.shifttestingproject

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shifttestingproject.data.repository.Repository
import com.example.shifttestingproject.db.BinDbModel
import com.example.shifttestingproject.model.HistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

@Suppress("UNUSED_EXPRESSION")
class MainViewModel : ViewModel() {

    var repository = Repository()
    val response: MutableLiveData<Response<HistoryItem>> = MutableLiveData()

    // посылает api запрос на получение данных по bin коду
    fun getBinData(bin: Int) {
        viewModelScope.launch {
            response.value = repository.getBin(bin)
        }
    }

    // парсит полученный ответ из api запроса в формат базы данных и записывает его
    fun parseResponseToBinModel(response: MutableLiveData<Response<HistoryItem>>) {
        val binModel = BinDbModel(
            city = response.value?.body()?.bank?.city ?: NONE,
            bankName = response.value?.body()?.bank?.name ?: NONE,
            phone = response.value?.body()?.bank?.phone ?: NONE,
            url = response.value?.body()?.bank?.url ?: NONE,
            brand = response.value?.body()?.brand ?: NONE,
            alpha2 = response.value?.body()?.country?.alpha2 ?: NONE,
            currency = response.value?.body()?.country?.currency ?: NONE,
            emoji = response.value?.body()?.country?.emoji ?: NONE,
            latitude = response.value?.body()?.country?.latitude?.toString() ?: NONE,
            longitude = response.value?.body()?.country?.longitude?.toString() ?: NONE,
            countryName = response.value?.body()?.country?.name ?: "",
            numeric = response.value?.body()?.country?.numeric ?: "",
            length = response.value?.body()?.number?.length?.toString() ?: NONE,
            luhn = convertBooleanToString(response.value?.body()?.number?.luhn),
            prepaid = convertBooleanToString(response.value?.body()?.prepaid),
            scheme = response.value?.body()?.scheme ?: "",
            type = response.value?.body()?.type ?: ""
        )
        Handler(Looper.getMainLooper()).postDelayed({
            insert(binModel) {}
        }, 1000)
    }

    // добавляет запись в базу данных
    fun insert(binModel: BinDbModel, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.insertBin(binModel) {
                onSuccess
            }
        }

    // удаляет запись из базы данных
    fun delete(binModel: BinDbModel, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.deleteBin(binModel) {
                onSuccess
            }
        }

    // конвертирует значение boolean в значение string (yes/no/none)
    private fun convertBooleanToString(bool: Boolean?): String {
        return if (bool != null) {
            if (bool) YES else NO
        } else {
            NONE
        }
    }
}