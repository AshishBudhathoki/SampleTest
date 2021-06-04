package com.demo.sampletest.features.photo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.demo.sampletest.R
import com.demo.sampletest.databinding.FragmentPhotoBinding
import com.squareup.picasso.Picasso

class UserPhotoFragment : Fragment(R.layout.fragment_photo) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPhotoBinding.bind(view)
        val bundle: Bundle = requireArguments()
        binding.apply {
            textPhotoid.text = bundle.get("PHOTO_ID").toString()
            textAlbumid.text = bundle.get("AlBUM_ID").toString()
            textImageDescription.text = bundle.get("PHOTO_TITLE").toString()
            Picasso.get().load(bundle.get("PHOTO_URL").toString()).into(imagePhoto);
        }
    }
}