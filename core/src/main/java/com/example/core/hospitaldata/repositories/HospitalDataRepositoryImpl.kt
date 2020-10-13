package com.example.core.hospitaldata.repositories

import android.content.Context
import com.example.core.extensions.readJsonAsset
import com.example.core.hospitaldata.models.HospitalsDataModel
import com.example.core.hospitaldata.models.HospitalsRawDataModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single
import java.io.IOException

class HospitalDataRepositoryImpl(
    private val appContext: Context
) : HospitalDataRepository {

    private val gson = Gson()

    override fun getListOfHospitals(
        filterOptions: HospitalFilterOptions
    ): Single<List<HospitalsDataModel>> {
        return Single.create { emitter ->
            try {
                val json = appContext.readJsonAsset("Hospitals.json")
                val modelType = object : TypeToken<List<HospitalsRawDataModel>>() {}.type
                val listOfRawDataModels = gson.fromJson<List<HospitalsRawDataModel>>(json, modelType)

                emitter.onSuccess(listOfRawDataModels.mapToHospitalDataModel())
            } catch (e: JsonSyntaxException) {
                emitter.onError(RuntimeException("Unable to parse API response!"))
            } catch (e: IOException) {
                emitter.onError(IOException("Unable to parse API response!"))
            }
        }
    }

    private fun List<HospitalsRawDataModel>.mapToHospitalDataModel(): List<HospitalsDataModel> =
        map {
            HospitalsDataModel(
                id = it.organisationID,
                name = it.organisationName,
                address1 = it.address1,
                address2 = it.address2,
                address3 = it.address3,
                city = it.city,
                county = it.county,
                postCode = it.postcode
            )
        }
}
