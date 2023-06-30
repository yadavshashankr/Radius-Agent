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
import com.shashank.radiusAgent.repositories.NetworkRepository
import com.shashank.radiusAgent.utils.launchOnMain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainActivityContract.View, SelectedOptionsContract {

    private lateinit var facilitiesList : ArrayList<FacilityModel>
    @Inject
    lateinit var apiService : ApiService
    @Inject
    lateinit var dao : Dao

    private lateinit var presenter : MainPresenter
    private var _binding : ActivityMainBinding? = null
    private val binding : ActivityMainBinding
    get() = _binding as ActivityMainBinding

    private val scope = CoroutineScope(Dispatchers.IO)

    private val mainAdapter = MainAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = MainPresenter(this, NetworkRepository(apiService, dao))

        initView()

        presenter.getData(presenter.isMoreThan24Hours())

    }

    private fun initView() {
        binding.rvMVP.adapter = mainAdapter
    }

    override fun onLoading() {
        binding.progress.visibility= View.VISIBLE
    }

    override suspend fun onSuccess(model: MainModel) {
        scope.launchOnMain {
            binding.progress.visibility= View.GONE
            facilitiesList = presenter.processOptions(model)
            mainAdapter.addItems(applicationContext, facilitiesList, this@MainActivity)
        }
    }

    override fun onError(message: String) {
        binding.progress.visibility= View.GONE
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun disableSelectedOptionsState(valueFacilityID: String, valueOptionsID: String) {
        val facilitiesList = presenter.disableSelectedOptionsState(facilitiesList, valueFacilityID, valueOptionsID)
        mainAdapter.addItems(this, facilitiesList, this)
    }
}