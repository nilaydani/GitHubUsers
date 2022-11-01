package com.nilay.githubusers.presentation.fav

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.nilay.githubusers.adapter.UserLoadStateAdapter
import com.nilay.githubusers.adapter.UsersAdapter
import com.nilay.githubusers.databinding.FragmentFavBinding
import com.nilay.githubusers.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_fav.*


/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val userViewModel by viewModels<UserViewModel>()
    private var _binding: FragmentFavBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
//        userViewModel.getAllFavUsers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = UsersAdapter(requireActivity(), userViewModel, favAdapter = true) {
            userViewModel.addUsrToDB(it)
        }
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        recyclerViewFav.itemAnimator = null
        //add a load state footer to the recyclerview
        recyclerViewFav.adapter = adapter.withLoadStateFooter(
            footer = UserLoadStateAdapter { adapter.retry() }
        )
        recyclerViewFav.itemAnimator?.changeDuration = 0
        userViewModel.liveDataFav.observe(viewLifecycleOwner){
            adapter.submitData(this.lifecycle,PagingData.from(it))
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            userViewModel.getAllFavUsers()
            hideKeyboard(requireActivity())
        }
    }
    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}