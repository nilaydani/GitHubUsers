package com.nilay.githubusers.presentation.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.nilay.githubusers.adapter.UserLoadStateAdapter
import com.nilay.githubusers.adapter.UsersAdapter
import com.nilay.githubusers.databinding.FragmentSearchBinding
import com.nilay.githubusers.util.ConnectionLiveData
import com.nilay.githubusers.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @AndroidEntryPoint annotation lets this class gets an instance to the
 * [UserViewModel classs]
 * */
@AndroidEntryPoint
class SearchFragment : Fragment() {
    // instance of the userview model
    private val userViewModel by viewModels<UserViewModel>()

    /**
     * lateinit [ActivityMainBinding] class and promise the kotlin compiler
     * the value will ne initialised before using its
     * */
    private lateinit var binding: FragmentSearchBinding
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        connectionLiveData = ConnectionLiveData(requireActivity())
        connectionLiveData.observe(viewLifecycleOwner) { isNetworkAvailable ->
            isNetworkAvailable?.let {
                updateUI(it)
            }
        }
        return binding.root
    }

    private fun updateUI(it: Boolean) {
        // instance of user adapter class
        val adapter = UsersAdapter(requireActivity(), userViewModel, favAdapter = false) {
            userViewModel.addUsrToDB(it)
        }
        //bind the views
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            //add a load state footer to the recyclerview
            recyclerView.adapter = adapter.withLoadStateFooter(
                footer = UserLoadStateAdapter { adapter.retry() }
            )
            recyclerView.itemAnimator?.changeDuration = 0
            activityMainButtonRetry.setOnClickListener { adapter.retry() }
        }

        // restore the state for the adatper
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                swipeRefreshLayout.isRefreshing = false;
                adapter.refresh()
            }
        }

        // observer the data returned by the viewmodel
        userViewModel.users.observe(requireActivity()) {
            adapter.submitData(this.lifecycle, it)
        }

        /** bind and set the visibility of the [LoadState] view items**/
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                activityMainProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                activityMainButtonRetry.isVisible = loadState.source.refresh is LoadState.Error
                activityMainTextViewError.isVisible = loadState.source.refresh is LoadState.Error

                // display empty textview when items are less than 1
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    activityMainTextViewEmpty.isVisible = true
                } else {
                    activityMainTextViewEmpty.isVisible = false
                }
            }
        }

        binding.etSearch.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_GO) {
                binding.etSearch.text?.trim().toString().let {
                    setUpSearch()
                }
                true
            } else {
                false
            }
        }

        binding.etSearch.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                setUpSearch()
                true
            } else {
                false
            }
        }

    }

    private fun setUpSearch() {
        binding.etSearch.text?.trim().toString().let {
            if (it.isNotEmpty()) {
                binding.recyclerView.scrollToPosition(0)
                userViewModel.searchUsers(it)
            }
        }
    }

    companion object {
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_SECTION_NUMBER, sectionNumber)
//                }
            }
        }
    }
}