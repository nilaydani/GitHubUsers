package com.nilay.githubusers.presentation.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.nilay.githubusers.R
import com.nilay.githubusers.databinding.ActivityUserDetailBinding
import com.nilay.githubusers.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_user_detail.*
/**
 * A placeholder fragment containing a simple view.
 */
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val userViewModel by viewModels<UserViewModel>()
    private var _binding: ActivityUserDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.callUserDetailsApi(intent?.getStringExtra(BUNDLE_ARG_USER_NAME)?:"android")
        _binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(_binding?.root)
        userViewModel.liveDataUserDetail.observe(this, Observer { result->
            println(result)
            Glide.with(this)
                .load(result.avatar_url)
                .centerCrop()
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(avatarImageView)

            nameTextView.text = getString(R.string.user_name, result?.name ?: "unknown")
            locationTextView.text =
                getString(R.string.location, result?.location ?: "unknown")
            companyTextView.text = getString(R.string.company, result?.company ?: "unknown")
            followersTextView.text =
                getString(R.string.followers, result.followers.toString())
            publicGistsTextView.text =
                getString(R.string.publicGists, result.public_gists.toString())
            publicReposTextView.text =
                getString(R.string.publicRepos, result.public_repos.toString())
            lastUpdateTextView.text =
                getString(R.string.lastUpdate, result.updated_at.substring(0, 10))
            accountCreatedTextView.text =
                getString(R.string.accountCreated, result.created_at.substring(0, 10))

            val text = result.html_url //"Underlined Text"
            val content = SpannableString(text)
            content.setSpan(UnderlineSpan(), 0, text.length, 0)
            htmlURLTextView.text = content

            htmlURLTextView.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(htmlURLTextView.text.toString())
                startActivity(i)
            }
        })
    }

    companion object {
        const val BUNDLE_ARG_USER_NAME = "username"
    }
}