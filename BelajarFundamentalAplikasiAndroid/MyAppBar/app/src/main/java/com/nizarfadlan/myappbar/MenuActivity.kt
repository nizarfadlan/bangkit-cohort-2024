package com.nizarfadlan.myappbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import com.nizarfadlan.myappbar.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            searchBar.inflateMenu(R.menu.option_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                // Handle menu item selection (such as in the action bar MainActivity
                true
            }
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    val searchText = searchView.text.toString()
                    searchBar.setText(searchText)
                    searchView.hide()

                    if (searchText.isEmpty()) {
                        Toast.makeText(this@MenuActivity, "Search text is empty", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MenuActivity, searchText, Toast.LENGTH_SHORT).show()
                    }
                    false
                }
        }
    }
}