package com.example.hospitals.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.hospitals.models.HospitalViewItemModel
import com.example.core.hospitaldata.models.HospitalsDataModel
import com.example.core.hospitaldata.repositories.HospitalDataRepository
import com.example.core.hospitaldata.repositories.HospitalFilterOptions
import com.example.hospitals.rules.RxImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HospitalListViewModelTest {

    @get:Rule
    var testSchedulerRule = RxImmediateSchedulerRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val hospitalDataRepository: HospitalDataRepository = mockk(relaxed = true)

    private lateinit var listOfEvents: MutableList<ViewState>
    private lateinit var testObserver: Observer<ViewState>

    private lateinit var viewModel: HospitalListViewModel

    @Test
    fun `Given the view model is being initialised, when the list of hospitals fetch is successful, then emit the list state`() {
        // Given
        every { hospitalDataRepository.getListOfHospitals() } returns Single.just(listOfHospitals)

        val expectedList = listOf(
            HospitalViewItemModel(
                id = 123,
                name = "Manchester Hospital",
                city = "Manchester"
            )
        )

        val expected = ViewState(
            listOfHospitals = expectedList
        )

        // When
        createViewModel()

        // Then
        assertEquals(1, listOfEvents.size)
        assertEquals(expected, listOfEvents[0])
    }

    @Test
    fun `Given the view model is being initialised, when the list of hospitals fetch fails, then emit the list failed state`() {
        // Given
        every { hospitalDataRepository.getListOfHospitals() } returns Single.error(Exception())

        val expected = ViewState(
            listOfHospitals = emptyList(),
            showError = true
        )

        // When
        createViewModel()

        // Then
        assertEquals(1, listOfEvents.size)
        assertEquals(expected, listOfEvents[0])
    }

    @Test
    fun `Given the user wants to filter the list, when the user selects a filter, then request a filtered list`() {
        // Given
        every { hospitalDataRepository.getListOfHospitals() } returns Single.just(listOfHospitals)

        // When
        createViewModel()
        viewModel.onFilterConfirmed(index = 1)

        // Then
        verify(atMost = 1) { hospitalDataRepository.getListOfHospitals(filterOption = HospitalFilterOptions.NHS) }
    }

    @Test
    fun `Given the user wants to filter the list, when the user selects a filter, then show the currently selected filter`() {
        // Given
        every { hospitalDataRepository.getListOfHospitals() } returns Single.just(listOfHospitals)

        val expectedList = listOf(
            HospitalViewItemModel(
                id = 123,
                name = "Manchester Hospital",
                city = "Manchester"
            )
        )

        val expected = ViewState(
            listOfHospitals = expectedList,
            showError = false,
            showFilter = true,
            currentFilterOptionIndex = 0
        )

        // When
        createViewModel()
        viewModel.onFilterClicked()

        // Then
        assertEquals(2, listOfEvents.size)
        assertEquals(expected, listOfEvents[1])
    }

    private fun createViewModel() {
        listOfEvents = mutableListOf()
        testObserver = Observer<ViewState> { listOfEvents.add(it) }

        viewModel = HospitalListViewModel(hospitalDataRepository)
            .also { it.viewState.observeForever(testObserver) }
    }

    private val listOfHospitals = listOf(
        HospitalsDataModel(
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
    )
}
