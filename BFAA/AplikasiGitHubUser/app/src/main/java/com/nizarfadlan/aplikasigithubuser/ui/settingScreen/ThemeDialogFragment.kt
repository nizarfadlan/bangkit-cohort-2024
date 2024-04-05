package com.nizarfadlan.aplikasigithubuser.ui.settingScreen

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nizarfadlan.aplikasigithubuser.utils.ThemeMode

class ThemeDialogFragment(private val onThemeSelected: (ThemeMode) -> Unit) : DialogFragment() {
    override fun onCreateDialog(
        savedInstanceState: Bundle?
    ): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose Theme")
            .setCancelable(true)
            .setNegativeButton(getString(android.R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setItems(
                arrayOf("Light", "Dark", "System")
            ) { _, which ->
                val selectedTheme = when (which) {
                    0 -> ThemeMode.LIGHT
                    1 -> ThemeMode.DARK
                    else -> ThemeMode.SYSTEM
                }
                onThemeSelected(selectedTheme)
            }
            .create()
    }
}