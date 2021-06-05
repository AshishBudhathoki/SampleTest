package com.demo.sampletest.features.album

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.demo.sampletest.R
import com.demo.sampletest.data.model.UserPhotos
import com.demo.sampletest.databinding.FragmentUserPhotosBinding
import com.demo.sampletest.features.photo.UserPhotoFragment
import com.demo.sampletest.utils.InjectorUtils
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import kotlin.properties.Delegates

class UserPhotosFragment : Fragment(R.layout.fragment_user_photos),
    PhotoAdapter.OnItemClickListener {
    private lateinit var viewAdapter: PhotoAdapter
    private var userId by Delegates.notNull<Int>()
    private lateinit var viewModel: UserPhotosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = UserPhotosViewModel(InjectorUtils.providePhotosRepository())
        val binding = FragmentUserPhotosBinding.bind(view)
        // Setup recyclerView
        viewAdapter = PhotoAdapter(this)
        binding.apply {
            recyclerView.apply {
                setHasFixedSize(true)
                adapter = viewAdapter
            }

            getDataFromUsersFragment()
            binding.textAlbumId.text = userId.toString()
            viewModel.getAllPhotos(userId)
            // ViewModel setup
            viewModel.getUserPhotos()
                .observe(viewLifecycleOwner, Observer<List<UserPhotos>> { photos ->
                    Timber.d("DATA IS: $photos")
                    when {
                        photos.isEmpty() -> {
                            recyclerView.visibility = View.GONE
                            emptyState.visibility = View.VISIBLE
                        }
                        photos != null -> {
                            recyclerView.visibility = View.VISIBLE
                            emptyState.visibility = View.GONE
                            viewAdapter.setData(photos)
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
                            viewModel.refreshAllPhotos()
                        }.show()
                    }
                    viewModel.onSnackbarShown()
                }
            })
        }
    }

    private fun getDataFromUsersFragment() {
        val bundle: Bundle? = arguments
        userId = bundle!!.get("USER_ID").toString().toInt()
        Timber.d("ROCKET ID: $userId")
    }

    override fun onItemClicked(photos: UserPhotos, itemView: View) {
        val bundle = Bundle()
        bundle.putString("PHOTO_ID", photos.id.toString())
        bundle.putString("AlBUM_ID", userId.toString())
        bundle.putString("PHOTO_URL", photos.url)
        bundle.putString("PHOTO_TITLE", photos.title)

        val userPhotosFragment = UserPhotoFragment()
        userPhotosFragment.arguments = bundle
        requireActivity()!!.supportFragmentManager.beginTransaction()
            .replace(
                (requireView().parent as ViewGroup).id,
                userPhotosFragment,
                "USER_PHOTO_FRAGMENT"
            )
            .addToBackStack(null)
            .commit()
    }

}