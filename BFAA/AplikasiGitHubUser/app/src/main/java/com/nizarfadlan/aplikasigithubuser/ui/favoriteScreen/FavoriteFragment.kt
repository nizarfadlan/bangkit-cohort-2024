package com.nizarfadlan.aplikasigithubuser.ui.favoriteScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nizarfadlan.aplikasigithubuser.R
import com.nizarfadlan.aplikasigithubuser.adapter.ListUserAdapter
import com.nizarfadlan.aplikasigithubuser.data.remote.response.ItemsItem
import com.nizarfadlan.aplikasigithubuser.databinding.FragmentFavoriteBinding
import com.nizarfadlan.aplikasigithubuser.ui.ViewModelFactory
import com.nizarfadlan.aplikasigithubuser.ui.base.BaseFragment

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val listAdapter by lazy { ListUserAdapter { username -> moveToDetailUser(username) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        binding.rvFavoriteUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        favoriteViewModel.getAllFavoriteUsers()
        favoriteViewModel.favoriteUser.observe(viewLifecycleOwner) { listFavoriteUser ->
            val listUsers = listFavoriteUser.map {
                ItemsItem(
                    id = it.id,
                    login = it.username,
                    avatarUrl = it.avatarUrl
                )
            }
            listAdapter.listUsers = listUsers
            showNotFound(listFavoriteUser.isEmpty(), "favorite user")
        }
    }

    private fun moveToDetailUser(username: String) {
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
                username
            )
        )
    }

    private fun showNotFound(isNotFound: Boolean, text: String = "data") {
        if (isNotFound) {
            setTextNotFound(text)
            binding.tvNoData.visibility = View.VISIBLE
        } else {
            binding.tvNoData.visibility = View.GONE
        }
    }

    private fun setTextNotFound(text: String) {
        binding.apply {
            val textNotFound = resources.getString(R.string.not_found, text)
            tvNoData.text = textNotFound
        }
    }

    private fun initToolbar() {
        binding.apply {
            toolbar.apply {
                title = "Favorite User"
                inflateMenu(R.menu.main_menu)
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_settings -> {
                            findNavController().navigate(
                                FavoriteFragmentDirections.actionFavoriteFragmentToSettingFragment()
                            )
                            true
                        }

                        else -> false
                    }
                }
                menu.findItem(R.id.menu_favorite)?.isVisible = false
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
}