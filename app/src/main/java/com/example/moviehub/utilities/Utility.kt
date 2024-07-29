package com.example.moviehub.utilities

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.moviehub.R
import com.google.android.material.snackbar.Snackbar


class Utility {
    fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message,Snackbar.LENGTH_SHORT ).show()
    }
    fun showRating(view: View, rating: Double, ratingCircleLayoutId: Int) {
        val ratingCircleLayout: LinearLayout = view.findViewById(ratingCircleLayoutId)
        val greenDrawableId=R.drawable.rating_circle_green
        val yellowDrawableId=R.drawable.rating_circle_yellow
        val redDrawableId=R.drawable.rating_circle_red

        if (rating >= 8) {
            ratingCircleLayout.setBackgroundResource(greenDrawableId)
        } else if (rating >= 5) {
            ratingCircleLayout.setBackgroundResource(yellowDrawableId)
        } else {
            ratingCircleLayout.setBackgroundResource(redDrawableId)
        }
    }

    fun showToast(context: Context, message:String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
     fun logError(message: String, throwable: Throwable) {
        Log.e("MovieHuB", "$message: ${throwable.message}", throwable)
    }
    fun debug(message: String) {
        Log.d("MovieHuB", message)
    }

     fun checkNetworkConnection(context:Context):Boolean{
        if (!NetworkManager.isNetworkAvailable(context)) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle(ContextCompat.getString(context, R.string.no_network))
            alertDialog.setMessage(ContextCompat.getString(context, R.string.connect_network))
            alertDialog.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
            return false
        }
        return true
    }

}