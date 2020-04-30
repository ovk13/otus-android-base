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

        setupBottomNavigation()

    }

    private fun setupBottomNavigation() {
        val navController = navHostFragment.findNavController()
        mainNavigation.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        val navController = navHostFragment.findNavController()
        if (navController.currentDestination?.parent?.startDestination != navController.currentDestination?.id) {
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
