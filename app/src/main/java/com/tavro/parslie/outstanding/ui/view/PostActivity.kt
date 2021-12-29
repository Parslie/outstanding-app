package com.tavro.parslie.outstanding.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.data.model.Post
import com.tavro.parslie.outstanding.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post)

        val post: Post = intent.getParcelableExtra("post")!!
        binding.postContent.text = post.content
        binding.postTitle.text = post.title
        binding.postDateCreated.text = post.formatDateCreated("yyyy-MM-dd hh:mm:ss")
    }
}