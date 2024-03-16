package com.nizarfadlan.aplikasigithubuser.ui.detailsScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.nizarfadlan.aplikasigithubuser.R
import com.nizarfadlan.aplikasigithubuser.adapter.SectionsPagerAdapter
import com.nizarfadlan.aplikasigithubuser.data.Result
import com.nizarfadlan.aplikasigithubuser.data.local.entity.UserEntity
import com.nizarfadlan.aplikasigithubuser.databinding.FragmentDetailBinding
import com.nizarfadlan.aplikasigithubuser.ui.ViewModelFactory
import com.nizarfadlan.aplikasigithubuser.ui.base.BaseFragment
import com.nizarfadlan.aplikasigithubuser.utils.Helper.handleError

class DetailFragment : BaseFragment<FragmentDetailBinding>() {
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        val username = arguments?.getString(EXTRA_USERNAME)

        if (username?.isNotEmpty() == true) {
            setDetailUser(savedInstanceState, username)
            setViewPager(username)
        } else {
            val textToast = getString(R.string.not_found, "username")
            Toast.makeText(requireActivity(), textToast, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun setDetailUser(savedInstanceState: Bundle?, username: String) {
        username.takeIf { savedInstanceState === null }?.let { detailViewModel.getDetailUser(it) }
        detailViewModel.detailUser.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        setData(result.data)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        requireContext().handleError(DetailViewModel.TAG, result.error)
                    }
                }
            }
        }
    }

    private fun setData(data: UserEntity) {
        binding.apply {
            Glide.with(this@DetailFragment)
                .load(data.avatarUrl)
                .error(R.drawable.broken_image_24px)
                .into(ivUser)

            tvName.text = data.name ?: data.username
            tvUsername.text = data.username
            tvFollowers.text = resources.getString(R.string.followers, data.followers)
            tvFollowing.text = resources.getString(R.string.following, data.following)
            if (data.bio != null) {
                tvBio.text = data.bio
            } else {
                tvBio.visibility = View.GONE
            }
            setIconFavoriteState(data.isFavorite)

            btnOpenInBrowser.setOnClickListener {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    addCategory(Intent.CATEGORY_BROWSABLE)
                    setData(Uri.parse(data.htmlUrl))
                }
                startActivity(intent)
            }

            btnMore.setOnClickListener { v ->
                showMenuButtonMore(v, R.menu.popup_detail_menu, data)
            }

            fabFavorite.setOnClickListener {
                data.isFavorite = !data.isFavorite
                setFavoriteState(data.isFavorite, data.username)
                setIconFavoriteState(data.isFavorite)
            }
        }
    }

    private fun setFavoriteState(isFavorite: Boolean, username: String) {
        detailViewModel.updateFavoriteUser(isFavorite, username)
    }

    private fun setIconFavoriteState(isFavorite: Boolean) {
        binding.apply {
            val icon = if (isFavorite) R.drawable.favorite_fill_24px else R.drawable.favorite_24px
            val contentDescription = if (isFavorite) R.string.unfavorite else R.string.favorite
            fabFavorite.setImageResource(icon)
            fabFavorite.contentDescription = resources.getString(contentDescription)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showMenuButtonMore(v: View, @MenuRes menuRes: Int, data: UserEntity) {
        val popup = PopupMenu(requireContext(), v)
        popup.apply {
            setForceShowIcon(true)
            menuInflater.inflate(menuRes, menu)
            if (data.email.isNullOrEmpty()) menu.removeItem(R.id.action_contact_email)

            setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.action_contact_email -> {
                        val intentEmail = Intent(Intent.ACTION_SENDTO).apply {
                            setData(Uri.parse("mailto:${data.email}"))
                        }
                        startActivity(intentEmail)
                        true
                    }

                    R.id.action_share -> {
                        val textShare = """
                        ${data.name ?: data.username} (${data.username})
                        ${data.bio ?: ""}
                        
                        Followers: ${data.followers ?: "0"}
                        Following: ${data.following ?: "0"}
                        Email: ${data.email ?: "-"}
                        
                        ${data.htmlUrl}
                    """.trimIndent()
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, textShare)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share user")
                        startActivity(shareIntent)
                        true
                    }

                    else -> false
                }
            }
            show()
        }
    }

    private fun setViewPager(username: String) {
        binding.apply {
            val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity())
            sectionsPagerAdapter.username = username
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    private fun initToolbar() {
        binding.apply {
            toolbar.apply {
                title = "Detail User"
                inflateMenu(R.menu.main_menu)
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_settings -> {
                            findNavController().navigate(
                                DetailFragmentDirections.actionDetailFragmentToSettingFragment()
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

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDetailBinding = FragmentDetailBinding.inflate(inflater, container, false)

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }
}