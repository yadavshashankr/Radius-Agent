package com.shashank.radiusAgent.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shashank.radiusAgent.contracts.SelectedOptionsContract
import com.shashank.radiusAgent.network.model.FacilityModel
import com.shashank.radiusAgent.adapters.MainAdapter
import com.shashank.radiusAgent.contracts.MainActivityContract
import com.shashank.radiusAgent.databinding.ActivityMainBinding
import com.shashank.radiusAgent.db.Dao
import com.shashank.radiusAgent.network.api.ApiService
import com.shashank.radiusAgent.network.model.MainModel
import com.shashank.radiusAgent.presenter.MainPresenter
import com.shashank.radiusAgent.repositories.FacilitiesRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainActivityContract.View, SelectedOptionsContract {

    @Inject
    lateinit var apiService : ApiService
    @Inject
    lateinit var dao : Dao
    private lateinit var facilitiesList : ArrayList<FacilityModel>
    private lateinit var presenter : MainPresenter
    private var _binding : ActivityMainBinding? = null
    private val binding : ActivityMainBinding
    get() = _binding as ActivityMainBinding
    private val mainAdapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = MainPresenter(this, FacilitiesRepository(apiService, dao))

        initView()

        presenter.getData()

    }

    private fun initView() {
        binding.rvMVP.adapter = mainAdapter
    }

    override fun onLoading() {
        binding.progress.visibility = View.VISIBLE
    }

    override fun onSuccess(model : MainModel?) {
        binding.progress.visibility = View.GONE
        if (model == null)return
        facilitiesList = presenter.processOptions(model)
        mainAdapter.addItems(applicationContext, facilitiesList, this@MainActivity, 0)
    }

    override fun onError(message : String?) {
        binding.progress.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun disableSelectedOptionsState(valueFacilityID : String, valueOptionsID : String, selectedPosition : Int) {
        val facilitiesList = presenter.disableSelectedOptionsState(facilitiesList, valueFacilityID, valueOptionsID)
        val finalPosition = presenter.getFinalPosition(selectedPosition)
        mainAdapter.addItems(this, facilitiesList, this, finalPosition)
    }
}