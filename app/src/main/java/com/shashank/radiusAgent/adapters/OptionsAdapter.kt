package com.shashank.radiusAgent.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shashank.radiusAgent.contracts.SelectedOptionsContract
import com.shashank.radiusAgent.databinding.ViewHolderOptionsBinding
import com.shashank.radiusAgent.globals.Constants
import com.shashank.radiusAgent.network.model.OptionsModel
import com.shashank.radiusAgent.utils.OptionUtils

@SuppressLint("NotifyDataSetChanged")
class OptionsAdapter : RecyclerView.Adapter<OptionsAdapter.MyViewHolder>() {

    private var list = ArrayList<OptionsModel>()
    private lateinit var selectedOptionsContract : SelectedOptionsContract
    private lateinit var context : Context

    fun addItems(context: Context, arrayListOptionsModel : ArrayList<OptionsModel>, selectedOptionsContract : SelectedOptionsContract){
        this.context = context
        this.selectedOptionsContract = selectedOptionsContract
        this.list = arrayListOptionsModel
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewDataBinding : ViewHolderOptionsBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root)

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int): MyViewHolder {
        val binding = ViewHolderOptionsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder : MyViewHolder, position : Int) {
        holder.setIsRecyclable(false)
        val binding = holder.viewDataBinding
        val item = this.list[position]
        binding.optionsModel = item

        binding.ivOption.setImageDrawable(OptionUtils.processImageDrawable(context, item.icon, item.isEnabled))

        binding.isSelected = item.name.equals(Constants.selected_name)

        binding.tvOption.setOnClickListener{
            Constants.selected_name = binding.tvOption.text.toString()

            val values = item.selectedFacility?.split("-")
            try {
                val valueFacilityID = values?.get(0)
                val valueOptionsID = values?.get(1)
                selectedOptionsContract.disableSelectedOptionsState(valueFacilityID as String, valueOptionsID as String, position)
            }catch (e :java.lang.Exception){
                e.printStackTrace()
                selectedOptionsContract.disableSelectedOptionsState("", "", position)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.list.size
    }
}