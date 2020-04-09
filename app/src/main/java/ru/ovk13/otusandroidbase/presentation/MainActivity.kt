package ru.ovk13.otusandroidbase.presentation

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import ru.ovk13.otusandroidbase.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        getDataFromState(savedInstanceState)

        setupBottomNavigation()

    }

    private fun setupBottomNavigation() {
        val navController = navHostFragment.findNavController()
        mainNavigation.setupWithNavController(navController)
    }

    //
//    private fun getDataFromState(savedInstanceState: Bundle?) {
//        savedInstanceState?.apply {
//            visitedFilmsIds = this.getIntArray(VISITED_FILMS)?.toMutableList() ?: mutableListOf()
//            favouriteFilmsIds = this.getIntArray(FAVOURITE_FILMS)?.toMutableList()
//                ?: mutableListOf()
//        }
//    }
//
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putIntArray(VISITED_FILMS, visitedFilmsIds.toIntArray())
//        outState.putIntArray(FAVOURITE_FILMS, favouriteFilmsIds.toIntArray())
//    }
//
    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else {
            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            val okListener = DialogInterface.OnClickListener { _, _ -> super.onBackPressed() }
            val cancelListener = DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() }

            dialogBuilder.setMessage(R.string.exitDialogMessage)
                .setTitle(R.string.exitDialogTitle)
                .setNegativeButton(R.string.exitDialogNo, cancelListener)
                .setPositiveButton(R.string.exitDialogYes, okListener)

            val dialog: AlertDialog = dialogBuilder.create()
            dialog.show()
        }
    }
}
