package com.example.hospitals.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.core.hospitaldata.models.HospitalsDataModel
import com.example.core.hospitaldata.repositories.HospitalDataRepository
import com.example.hospitals.models.HospitalItemModel
import com.example.hospitals.rules.RxImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HospitalViewModelTest {

    @get:Rule
    var testSchedulerRule = RxImmediateSchedulerRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val hospitalDataRepository: HospitalDataRepository = mockk(relaxed = true)

    private lateinit var listOfEvents: MutableList<HospitalViewState>
    private lateinit var testObserver: Observer<HospitalViewState>

    private lateinit var viewModel: HospitalViewModel

    @Test
    fun `Given a hospital id is provided, when the view model is called, then fetch the hospital data`() {
        // Given
        every { hospitalDataRepository.getHospital(any()) } returns Single.just(hospital)

        val expectedHospital = HospitalItemModel(
            name = "Manchester Hospital",
            addressLine1 = "Swinemoor Lane",
            addressLine2 = "Coombe Way",
            addressLine3 = "",
            city = "Manchester",
            postCode = "HU17 0FA",
            number = "1234567890",
            website = "www.website.co.uk",
            sector = "NHS Sector"
        )

        val expected = HospitalViewState(hospitalItemModel = expectedHospital)

        // When
        createViewModel()
        viewModel.initialise(123)

        // Then
        assertEquals(2, listOfEvents.size)
        assertEquals(expected, listOfEvents[1])
    }

    @Test
    fun `Given a hospital id is not provided, when the view model is called, then emit the failed state`() {
        // Given
        every { hospitalDataRepository.getHospital(any()) } returns Single.error(Exception())

        val expected = HospitalViewState(
            hospitalItemModel = null,
            showError = true
        )

        // When
        createViewModel()
        viewModel.initialise(123)

        // Then
        assertEquals(2, listOfEvents.size)
        assertEquals(expected, listOfEvents[1])
    }

    private fun createViewModel() {
        listOfEvents = mutableListOf()
        testObserver = Observer<HospitalViewState> { listOfEvents.add(it) }

        viewModel = HospitalViewModel(hospitalDataRepository)
            .also { it.viewState.observeForever(testObserver) }
    }

    private val hospital = HospitalsDataModel(
        id = 123,
        name = "Manchester Hospital",
        address1 = "Swinemoor Lane",
        address2 = "Coombe Way",
        address3 = "",
        city = "Manchester",
        county = "East Yorkshire",
        postCode = "HU17 0FA",
        phone = "1234567890",
        website = "www.website.co.uk",
        sector = "NHS Sector"
    )
}
