package com.android.democarselectionapp

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.android.democarselectionapp.model.NetworkLiveData
import com.google.android.material.snackbar.Snackbar

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun Spinner.getItem(action: (position: String) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (parent != null) {
                action.invoke(parent.getItemAtPosition(position).toString())
            }
        }
    }
}

fun Application.initializeNetworkLiveData() {
    NetworkLiveData.init(this)
}

fun showSnackBar(view: View) {
    val snackbar =
        Snackbar.make(
            view,
            "Please check your Internet connection",
            Snackbar.LENGTH_LONG
        )
            .setAction("Action", null)
    snackbar.setActionTextColor(Color.BLUE)
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(Color.LTGRAY)
    val textView =
        snackbarView.findViewById(com.android.democarselectionapp.R.id.snackbar_text) as TextView
    textView.setTextColor(Color.BLUE)
    textView.textSize = 20f
    snackbar.show()
}

fun getInterConnectivity(
    context: FragmentActivity,
    isInternetConnected: (Boolean) -> Unit
) {
    NetworkLiveData.observe(context, { isNetworkAvailable ->
        isInternetConnected.invoke(isNetworkAvailable)
    })
}

fun FragmentActivity.listenToNetworkUpdate(view: View) {
    getInterConnectivity(this) { isNetworkAvailable ->
        when {
            !isNetworkAvailable -> {
                showSnackBar(view)
            }
        }
    }
}
