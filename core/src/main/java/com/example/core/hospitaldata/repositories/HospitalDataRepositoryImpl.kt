package com.example.core.hospitaldata.repositories

import android.content.Context
import com.example.core.extensions.readJsonAsset
import com.example.core.hospitaldata.models.HospitalsDataModel
import com.example.core.hospitaldata.models.HospitalsRawDataModel
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single
import java.io.IOException

class HospitalDataRepositoryImpl(
    private val appContext: Context
) : HospitalDataRepository {

    private val hospitalDataJson = "Hospitals.json"
    private val nhsSector = "NHS Sector"

    private val gson = Gson()

    override fun getListOfHospitals(
        filterOption: HospitalFilterOptions
    ): Single<List<HospitalsDataModel>> {
        return Single.create { emitter ->
            try {
                var listOfRawDataModels = getData()

                if (filterOption != HospitalFilterOptions.DEFAULT) {
                    listOfRawDataModels = listOfRawDataModels
                        .filter { filterCondition(filterOption, it) }
                }

                emitter.onSuccess(listOfRawDataModels.mapToHospitalDataModel())
            } catch (e: JsonSyntaxException) {
                emitter.onError(RuntimeException("Unable to parse API response!"))
            } catch (e: IOException) {
                emitter.onError(IOException("Unable to parse API response!"))
            }
        }
    }

    override fun getHospital(hospitalId: Long): Single<HospitalsDataModel> {
        // This could be fetched from memory/cache if performance was an issue.
        return Single.create { emitter ->
            try {
                val listOfRawDataModels = getData().mapToHospitalDataModel()

                val hospital = listOfRawDataModels
                    .firstOrNull { it.id == hospitalId }

                if (hospital == null) {
                    emitter.onError(RuntimeException("Unable to find hospital!"))
                } else {
                    emitter.onSuccess(hospital)
                }
            } catch (e: JsonSyntaxException) {
                emitter.onError(RuntimeException("Unable to parse API response!"))
            } catch (e: IOException) {
                emitter.onError(IOException("Unable to parse API response!"))
            }
        }
    }

    private fun filterCondition(
        filterOption: HospitalFilterOptions,
        model: HospitalsRawDataModel
    ): Boolean {
        return when (filterOption) {
            HospitalFilterOptions.DEFAULT -> true
            HospitalFilterOptions.NHS -> !model.sector.isBlank() && model.sector == nhsSector
            HospitalFilterOptions.HAS_WEBSITE -> model.website.isNotBlank()
        }
    }

    @Throws(IOException::class, JsonParseException::class)
    private fun getData(): List<HospitalsRawDataModel> {
        val json = appContext.readJsonAsset(hospitalDataJson)
        val modelType = object : TypeToken<List<HospitalsRawDataModel>>() {}.type
        return gson.fromJson(json, modelType)
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
                postCode = it.postcode,
                phone = it.phone,
                website = it.website,
                sector = it.sector
            )
        }
}
