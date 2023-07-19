package com.shashank.radiusAgent.utils

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import com.shashank.radiusAgent.R
import com.shashank.radiusAgent.globals.Constants


class OptionUtils {

     companion object {
         fun processImageDrawable(context : Context, icon : String?, isEnabled : Boolean) : Drawable? {
             when(icon){
                 Constants.Companion.Icons.APARTMENT -> return processIconState(context, R.drawable.apartment, isEnabled)
                 Constants.Companion.Icons.CONDO -> return processIconState(context, R.drawable.condo, isEnabled)
                 Constants.Companion.Icons.BOAT -> return processIconState(context, R.drawable.boat, isEnabled)
                 Constants.Companion.Icons.LAND -> return processIconState(context, R.drawable.land, isEnabled)
                 Constants.Companion.Icons.ROOMS -> return processIconState(context, R.drawable.rooms, isEnabled)
                 Constants.Companion.Icons.NO_ROOMS -> return processIconState(context, R.drawable.no_room, isEnabled)
                 Constants.Companion.Icons.SWIMMING -> return processIconState(context, R.drawable.swimming, isEnabled)
                 Constants.Companion.Icons.GARDEN -> return processIconState(context, R.drawable.garden, isEnabled)
                 Constants.Companion.Icons.GARAGE -> return processIconState(context, R.drawable.garage, isEnabled)
             }
             return null
         }
         private fun processIconState(context : Context, icon: Int, isEnabled : Boolean): Drawable? {
             val mIcon = ContextCompat.getDrawable(context, icon)
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                 mIcon?.colorFilter = BlendModeColorFilter( if(isEnabled) ContextCompat.getColor(context, R.color.blue_cff) else ContextCompat.getColor(context, R.color.gray_808), BlendMode.SRC_ATOP)
             } else {
                 @Suppress("DEPRECATION")
                 mIcon?.setColorFilter(if(isEnabled) ContextCompat.getColor(context, R.color.blue_cff) else ContextCompat.getColor(context, R.color.gray_808), PorterDuff.Mode.SRC_ATOP)
             }
             return mIcon
         }
     }

}