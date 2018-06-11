package com.devalkasundra.recipesearch.utilities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import com.devalkasundra.recipesearch.R

object DNUtilities {

    fun hasConnection(c: Context): Boolean {
        val cm = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (wifiNetwork != null && wifiNetwork.isConnected) {
            return true
        }

        val mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (mobileNetwork != null && mobileNetwork.isConnected) {
            return true
        }

        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    fun showAlertView(act: Activity, resIdTitle: Int, resIdMessage: Int) {
        val alert = AlertDialog.Builder(act)
        alert.setTitle(resIdTitle)
        alert.setMessage(resIdMessage)
        alert.setPositiveButton(act.resources.getString(R.string.ok)
        ) { dialog, _ ->
            // TODO Auto-generated method stub
            dialog.dismiss()
        }

        alert.create()
        alert.show()
    }

    fun showAlertView(act: Activity, resIdTitle: Int, resIdMessage: String) {
        val alert = AlertDialog.Builder(act)
        alert.setTitle(resIdTitle)
        alert.setMessage(resIdMessage)
        alert.setPositiveButton(act.resources.getString(R.string.ok)
        ) { dialog, _ ->
            // TODO Auto-generated method stub
            dialog.dismiss()
        }

        alert.create()
        alert.show()
    }

    fun showAlertView(act: Activity, resIdTitle: String, resIdMessage: String) {
        val alert = AlertDialog.Builder(act)
        alert.setTitle(resIdTitle)
        alert.setMessage(resIdMessage)
        alert.setPositiveButton(act.resources.getString(R.string.ok)
        ) { dialog, _ ->
            // TODO Auto-generated method stub
            dialog.dismiss()
        }

        alert.create()
        alert.show()
    }

}