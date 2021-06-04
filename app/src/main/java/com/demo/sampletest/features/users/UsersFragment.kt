package com.demo.sampletest.features.users

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.demo.sampletest.R
import com.demo.sampletest.data.model.UserInfo
import com.demo.sampletest.databinding.FragmentUsersBinding
import com.demo.sampletest.utils.InjectorUtils
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class UsersFragment : Fragment(R.layout.fragment_users), UserAdapter.OnItemClickListener {
    private lateinit var viewAdapter: UserAdapter
    private lateinit var viewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentUsersBinding.bind(view)

        viewModel = UsersViewModel(InjectorUtils.provideUserRepository())
        // Setup recyclerView
        viewAdapter = UserAdapter(this)
        binding.apply {
            recyclerView.apply {
                setHasFixedSize(true)
                adapter = viewAdapter
            }

            // ViewModel setup
            viewModel.getAllUsers()
                .observe(viewLifecycleOwner, Observer<List<UserInfo>> { launches ->
                    Timber.d("DATA IS: $launches")
                    when {
                        launches.isEmpty() -> {
                            recyclerView.visibility = View.GONE
                            emptyState.visibility = View.VISIBLE
                        }
                        launches != null -> {
                            recyclerView.visibility = View.VISIBLE
                            emptyState.visibility = View.GONE
                            viewAdapter.setData(launches)
                        }
                    }
                })

            // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
            viewModel.snackbar.observe(viewLifecycleOwner, Observer { text ->
                text?.let {
                    getView()?.let { it1 ->
                        Snackbar.make(it1, text, Snackbar.LENGTH_LONG).setAction(
                            getString(R.string.snackbar_action_retry)
                        ) {
                            viewModel.refreshAllUsers()
                        }.show()
                    }
                    viewModel.onSnackbarShown()
                }
            })
            // Swipe to refresh
        }
    }

    override fun onItemClicked(userId: Int, itemView: View) {
        Timber.d("Item clicked : ${userId}")
    }
}