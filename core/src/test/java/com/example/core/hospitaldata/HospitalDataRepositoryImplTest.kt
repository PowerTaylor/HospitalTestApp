package com.example.core.hospitaldata

import android.content.Context
import com.example.core.extensions.readJsonAsset
import com.example.core.hospitaldata.models.HospitalsDataModel
import com.example.core.hospitaldata.repositories.HospitalDataRepository
import com.example.core.hospitaldata.repositories.HospitalDataRepositoryImpl
import com.example.core.hospitaldata.repositories.HospitalFilterOptions
import com.google.gson.JsonParseException
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Test
import java.io.IOException

class HospitalDataRepositoryTest {

    private val context: Context = mockk(relaxed = true)
    private lateinit var repo: HospitalDataRepository

    @Before
    fun setUp() {
        repo = HospitalDataRepositoryImpl(context)

        mockkStatic("com.example.core.extensions.ContextExtensions")
    }

    @Test
    fun `Given the consumer has requested hospital data, when the json asset is parsed successfully, then return a list of mapped models`() {
        // Given
        every { context.readJsonAsset(any()) } returns rawDataAsJson

        val expectedList = listOf(
            hospitalDataModel1,
            hospitalDataModel2,
            hospitalDataModel3
        )

        // When
        val observer = repo.getListOfHospitals().test()

        // Then
        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValue(expectedList)
    }

    @Test
    fun `Given the consumer has requested NHS only hospital data, when the json asset is parsed successfully, then return a list of mapped NHS only models`() {
        // Given
        every { context.readJsonAsset(any()) } returns rawDataAsJson

        val expectedList = listOf(
            hospitalDataModel1,
            hospitalDataModel2
        )

        // When
        val observer = repo.getListOfHospitals(filterOption = HospitalFilterOptions.NHS).test()

        // Then
        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValue(expectedList)
    }

    @Test
    fun `Given the consumer has requested HAS_WEBSITE only hospital data, when the json asset is parsed successfully, then return a list of mapped HAS_WEBSITE only models`() {
        // Given
        every { context.readJsonAsset(any()) } returns rawDataAsJson

        val expectedList = listOf(
            hospitalDataModel1,
            hospitalDataModel2
        )

        // When
        val observer = repo.getListOfHospitals(filterOption = HospitalFilterOptions.NHS).test()

        // Then
        observer.assertComplete()
        observer.assertNoErrors()
        observer.assertValue(expectedList)
    }

    @Test
    fun `Given the consumer has requested hospital data, when the json asset parsing throws an exception, then return an error`() {
        // Given
        val exception = JsonParseException("Unable to parse API response!")
        every { context.readJsonAsset(any()) } throws exception

        // When
        val observer = repo.getListOfHospitals().test()

        // Then
        observer.assertError(JsonParseException::class.java)
    }

    @Test
    fun `Given the consumer has requested hospital data, when the json asset resources throws an exception, then return an error`() {
        // Given
        val exception = IOException("Unable to parse API response!")
        every { context.readJsonAsset(any()) } throws exception

        // When
        val observer = repo.getListOfHospitals().test()

        // Then
        observer.assertError(IOException::class.java)
    }

    private val hospitalDataModel1 = HospitalsDataModel(
        id = 1421,
        name = "East Riding Community Hospital",
        address1 = "Swinemoor Lane",
        address2 = "",
        address3 = "",
        city = "Beverley",
        county = "East Yorkshire",
        postCode = "HU17 0FA"
    )

    private val hospitalDataModel2 = HospitalsDataModel(
        id = 1611,
        name = "Zachary Merton Community Hospital",
        address1 = "Zachary Merton Community Hospital",
        address2 = "Glenville Road",
        address3 = "Rustington",
        city = "Littlehampton",
        county = "Sussex",
        postCode = "BN16 2EB"
    )

    private val hospitalDataModel3 = HospitalsDataModel(
        id = 17967,
        name = "Musculoskeletal physiotherapy service - Cranleigh Village Hospital",
        address1 = "",
        address2 = "6 High Street",
        address3 = "",
        city = "Cranleigh",
        county = "Surrey",
        postCode = "GU6 8AE"
    )

    private val rawDataAsJson = "[\n" +
            "  {\n" +
            "    \"OrganisationID\": 1421,\n" +
            "    \"OrganisationCode\": \"RV9HE\",\n" +
            "    \"OrganisationType\": \"Hospital\",\n" +
            "    \"SubType\": \"Mental Health Hospital\",\n" +
            "    \"Sector\": \"NHS Sector\",\n" +
            "    \"OrganisationStatus\": \"Visible\",\n" +
            "    \"IsPimsManaged\": \"True\",\n" +
            "    \"OrganisationName\": \"East Riding Community Hospital\",\n" +
            "    \"Address1\": \"Swinemoor Lane\",\n" +
            "    \"Address2\": \"\",\n" +
            "    \"Address3\": \"\",\n" +
            "    \"City\": \"Beverley\",\n" +
            "    \"County\": \"East Yorkshire\",\n" +
            "    \"Postcode\": \"HU17 0FA\",\n" +
            "    \"Latitude\": 53.85313415527344,\n" +
            "    \"Longitude\": -0.4114723205566406,\n" +
            "    \"ParentODSCode\": \"RV9\",\n" +
            "    \"ParentName\": \"Humber NHS Foundation Trust\",\n" +
            "    \"Phone\": \"01482 886600\",\n" +
            "    \"Email\": \"newhospital@nhs.net\",\n" +
            "    \"Website\": \"http://www.humber.nhs.uk\",\n" +
            "    \"Fax\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"OrganisationID\": 1611,\n" +
            "    \"OrganisationCode\": \"5P603\",\n" +
            "    \"OrganisationType\": \"Hospital\",\n" +
            "    \"SubType\": \"Hospital\",\n" +
            "    \"Sector\": \"NHS Sector\",\n" +
            "    \"OrganisationStatus\": \"Visible\",\n" +
            "    \"IsPimsManaged\": \"True\",\n" +
            "    \"OrganisationName\": \"Zachary Merton Community Hospital\",\n" +
            "    \"Address1\": \"Zachary Merton Community Hospital\",\n" +
            "    \"Address2\": \"Glenville Road\",\n" +
            "    \"Address3\": \"Rustington\",\n" +
            "    \"City\": \"Littlehampton\",\n" +
            "    \"County\": \"Sussex\",\n" +
            "    \"Postcode\": \"BN16 2EB\",\n" +
            "    \"Latitude\": 50.80788040161133,\n" +
            "    \"Longitude\": -0.5006316304206848,\n" +
            "    \"ParentODSCode\": \"RDR\",\n" +
            "    \"ParentName\": \"Sussex Community NHS Trust\",\n" +
            "    \"Phone\": \"01903 858100\",\n" +
            "    \"Email\": \"\",\n" +
            "    \"Website\": \"http://www.sussexcommunity.nhs.uk/services\",\n" +
            "    \"Fax\": \"\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"OrganisationID\": 17967,\n" +
            "    \"OrganisationCode\": \"NDA04\",\n" +
            "    \"OrganisationType\": \"Hospital\",\n" +
            "    \"SubType\": \"Hospital\",\n" +
            "    \"Sector\": \"Independent Sector\",\n" +
            "    \"OrganisationStatus\": \"Visible\",\n" +
            "    \"IsPimsManaged\": \"True\",\n" +
            "    \"OrganisationName\": \"Musculoskeletal physiotherapy service - Cranleigh Village Hospital\",\n" +
            "    \"Address1\": \"\",\n" +
            "    \"Address2\": \"6 High Street\",\n" +
            "    \"Address3\": \"\",\n" +
            "    \"City\": \"Cranleigh\",\n" +
            "    \"County\": \"Surrey\",\n" +
            "    \"Postcode\": \"GU6 8AE\",\n" +
            "    \"Latitude\": 51.14077377319336,\n" +
            "    \"Longitude\": -0.4865259528160095,\n" +
            "    \"ParentODSCode\": \"NDA\",\n" +
            "    \"ParentName\": \"Virgin Care Services Ltd\",\n" +
            "    \"Phone\": \"01483 782400\",\n" +
            "    \"Email\": \"\",\n" +
            "    \"Website\": \"\",\n" +
            "    \"Fax\": \"\"\n" +
            "  }]"
}
