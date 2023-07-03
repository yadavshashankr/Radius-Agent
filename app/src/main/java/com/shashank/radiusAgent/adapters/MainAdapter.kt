package com.shashank.radiusAgent.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shashank.radiusAgent.contracts.MainActivityContract
import com.shashank.radiusAgent.databinding.ViewHolderMainBinding
import com.shashank.radiusAgent.network.model.FacilityModel
import com.shashank.radiusAgent.network.model.OptionsModel

@SuppressLint("NotifyDataSetChanged")
class MainAdapter : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    private var list = ArrayList<FacilityModel>()
    private lateinit var context : Context
    private lateinit var selectedOptionsProcessor : MainActivityContract.SelectedOptionsProcessor
    private var selectedPosition = 0

    fun addItems(context : Context, facilitiesList : ArrayList<FacilityModel>, selectedOptionsProcessor : MainActivityContract.SelectedOptionsProcessor, selectedPosition : Int){
        this.context = context
        this.selectedOptionsProcessor = selectedOptionsProcessor
        this.list = facilitiesList
        this.selectedPosition = selectedPosition
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewDataBinding : ViewHolderMainBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root)

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int): MyViewHolder {
       val binding = ViewHolderMainBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
       val binding = holder.viewDataBinding

        val item = this.list[position]
        binding.facilityModel = item

        val layoutManager = LinearLayoutManager(context.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvOptions.layoutManager = layoutManager

        val optionsAdapter = OptionsAdapter()
        binding.rvOptions.adapter = optionsAdapter

        optionsAdapter.addItems(context, item.options as ArrayList<OptionsModel>, selectedOptionsProcessor)
        binding.rvOptions.scrollToPosition(selectedPosition)
    }

    override fun getItemCount(): Int {
       return this.list.size
    }
}