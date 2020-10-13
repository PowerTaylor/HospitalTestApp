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
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
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

    private lateinit var listOfEvents: MutableList<HospitalViewState>
    private lateinit var testObserver: Observer<HospitalViewState>

    private lateinit var viewModel: HospitalListViewModel

    @Test
    fun `Given the view model is being initialised, when the list of hospitals fetch is successful, then emit the list state`() {
        // Given
        every { hospitalDataRepository.getListOfHospitals() } returns Single.just(listOfHospitals)

        val expected = listOf(
            HospitalViewItemModel(
                id = 123,
                name = "Manchester Hospital",
                city = "Manchester"
            )
        )

        // When
        createViewModel()

        // Then
        assertEquals(1, listOfEvents.size)
        assertThat(
            listOfEvents[0],
            instanceOf(HospitalViewState.ListOfHospitalsFetched::class.java)
        )

        val item1List = (listOfEvents[0] as
                HospitalViewState.ListOfHospitalsFetched).listOfHospitals

        assertEquals(expected, item1List)
    }

    @Test
    fun `Given the view model is being initialised, when the list of hospitals fetch fails, then emit the list failed state`() {
        // Given
        every { hospitalDataRepository.getListOfHospitals() } returns Single.error(Exception())

        // When
        createViewModel()

        // Then
        assertEquals(1, listOfEvents.size)
        assertThat(listOfEvents[0], instanceOf(HospitalViewState.Error.ListFetchFailed::class.java))
    }

    @Test
    fun `Given the user wants to filter the list, when the user selects a filter, then request a filtered list`() {
        // Given
        every { hospitalDataRepository.getListOfHospitals() } returns Single.just(listOfHospitals)

        // When
        createViewModel()
        viewModel.onFilterClicked(index = 1)

        // Then
        verify(atMost = 1) { hospitalDataRepository.getListOfHospitals(filterOptions = HospitalFilterOptions.NHS) }
    }

    private fun createViewModel() {
        listOfEvents = mutableListOf()
        testObserver = Observer<HospitalViewState> { listOfEvents.add(it) }

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
            postCode = "HU17 0FA"
        )
    )
}
