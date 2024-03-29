package com.itacademy.ads.presentation

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.itacademy.ads.R
import com.itacademy.ads.databinding.FragmentSearchBinding
import com.itacademy.common.Resource
import com.itacademy.navigation.NavCommand
import com.itacademy.navigation.NavCommands
import com.itacademy.navigation.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), SearchView.OnQueryTextListener,
    MenuProvider {

    private lateinit var adsAdapter: AdsAdapter
    private val viewModel by viewModels<SearchViewModel>()
    private val favoritesViewModel by viewModels<FavoritesViewModel>()
    private val binding by viewBinding<FragmentSearchBinding>()
    private val menuHost: MenuHost
        get() = requireActivity()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        favoritesViewModel.init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setupRV()

        val swipe = binding.swipeSearch

        swipe.setOnRefreshListener {
            viewModel.getAds("")
            swipe.isRefreshing = false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            favoritesViewModel.favoriteIds
                .onEach { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val data = resource.data
                            Log.d("LIIIIST", "$data")
                            adsAdapter.submitFavoriteIds(data)
                        }

                        is Resource.Loading -> {

                        }

                        is Resource.Error -> {
                            adsAdapter.submitFavoriteIds(emptyList())
                        }
                    }
                }.collect()
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.ads
                .onEach { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val data = resource.data

                            if (data.isNullOrEmpty()) {
                                binding.tvEmptyList.visibility = View.VISIBLE
                            } else {
                                binding.tvEmptyList.visibility = View.GONE
                            }

                            adsAdapter.differ.submitList(data)
                            binding.progressBar.visibility = View.GONE
                        }
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
                .collect()
        }


    }


    private fun setupRV() {
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                com.itacademy.theme.R.drawable.default_divider
            )!!
        )

        adsAdapter = AdsAdapter(object : AdOnClickListener {
            override fun adSelect(adId: Long) {
                Log.d("navigateToAboutAd", adId.toString())
                navigate(
                    NavCommand(
                        NavCommands.DeepLink(
                            url = Uri.parse("waceplare://about/$adId")
                        )
                    )
                )
            }

            override fun addFavorite(adId: Long) {
                favoritesViewModel.addFavorite(adId)

            }

            override fun deleteFavorite(adId: Long) {
                favoritesViewModel.deleteFavorite(adId)
            }

        })
        binding.recyclerView.apply {
            addItemDecoration(dividerItemDecoration)
            layoutManager = LinearLayoutManager(activity)
            adapter = adsAdapter
        }

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_search -> {
                val sv = menuItem.actionView as SearchView
                sv.setOnQueryTextListener(this)
            }

        }
        return true
    }

    override fun onQueryTextSubmit(text: String?): Boolean {
        viewModel.getAds(text)
        return true
    }

    override fun onQueryTextChange(text: String?): Boolean {
        return true
    }


}