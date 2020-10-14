package com.example.core.hospitaldata.repositories

import com.example.core.hospitaldata.models.HospitalsDataModel
import io.reactivex.rxjava3.core.Single

interface HospitalDataRepository {

    fun getListOfHospitals(
        filterOption: HospitalFilterOptions = HospitalFilterOptions.DEFAULT
    ): Single<List<HospitalsDataModel>>
}

enum class HospitalFilterOptions {
    DEFAULT,
    NHS,
    HAS_WEBSITE;

    companion object {
        fun getFromIndex(index: Int): HospitalFilterOptions {
            try {
                return values()[index]
            } catch (e: ArrayIndexOutOfBoundsException) {
                throw RuntimeException("Filter not implemented!")
            }
        }
    }
}
