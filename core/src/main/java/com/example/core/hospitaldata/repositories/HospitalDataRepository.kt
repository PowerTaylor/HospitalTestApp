package com.example.core.hospitaldata.repositories

import com.example.core.hospitaldata.models.HospitalsDataModel
import io.reactivex.rxjava3.core.Single

interface HospitalDataRepository {

    /**
     * Get a list of hospitals.
     * @param filterOption - filter the hospitals via the enum provided.
     * @return a Single list of hospitals.
     */
    fun getListOfHospitals(
        filterOption: HospitalFilterOptions = HospitalFilterOptions.DEFAULT
    ): Single<List<HospitalsDataModel>>

    /**
     * Get a single hospital with a known id.
     * @param hospitalId - id of hospital to receive more data from.
     * @return a Single hospital with more data.
     */
    fun getHospital(
        hospitalId: Long
    ): Single<HospitalsDataModel>
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
