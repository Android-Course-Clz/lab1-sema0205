package ru.sema0205.lab1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import androidx.recyclerview.widget.DividerItemDecoration

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var btnFollow: Button
    private lateinit var btnMessage: Button
    private lateinit var ivProfileAvatar: ImageView
    private lateinit var ivProfileBackground: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupListeners()
        loadProfileData()
        setupRecyclerView()
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerViewPosts)
        btnFollow = findViewById(R.id.btnFollow)
        btnMessage = findViewById(R.id.btnMessage)
        ivProfileAvatar = findViewById(R.id.ivProfileAvatar)
        ivProfileBackground = findViewById(R.id.ivProfileBackground)
    }

    private fun setupListeners() {
        btnFollow.setOnClickListener {
            val isFollowing = btnFollow.text.toString() != getString(R.string.follow)
            if (isFollowing) {
                btnFollow.text = getString(R.string.follow)
                Toast.makeText(this, "Вы отписались от пользователя", Toast.LENGTH_SHORT).show()
            } else {
                btnFollow.text = "Подписан"
                Toast.makeText(this, "Вы подписались на пользователя", Toast.LENGTH_SHORT).show()
            }
        }

        btnMessage.setOnClickListener {
            Toast.makeText(this, "Открыть диалог с пользователем", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProfileData() {
        val avatarUrl =
            "https://pbs.twimg.com/profile_images/1893803697185910784/Na5lOWi5_400x400.jpg"
        val backgroundUrl = "https://pbs.twimg.com/profile_banners/44196397/1739948056/1500x500"

        Log.d("ImageLoading", "Загрузка аватара: $avatarUrl")

        ivProfileAvatar.load(avatarUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(R.drawable.ic_profile_placeholder)
            error(R.drawable.ic_profile_placeholder)
        }

        Log.d("ImageLoading", "Загрузка фона: $backgroundUrl")

        ivProfileBackground.load(backgroundUrl) {
            crossfade(true)
        }
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        postAdapter.onLikeClickListener = { post ->
            val updatedPosts = postAdapter.currentList.toMutableList()
            val position = updatedPosts.indexOfFirst { it.id == post.id }
            if (position != -1) {
                val updatedPost = post.copy(
                    isLiked = !post.isLiked,
                    likesCount = if (post.isLiked) post.likesCount - 1 else post.likesCount + 1
                )
                updatedPosts[position] = updatedPost
                postAdapter.submitList(updatedPosts)
            }
        }

        postAdapter.onCommentClickListener = { post ->
            Toast.makeText(this, "Комментарии: ${post.commentsCount}", Toast.LENGTH_SHORT).show()
        }

        val posts = listOf(
            Post(
                id = 1,
                authorName = "Илон Маск",
                authorNickname = "@elonmusk",
                authorAvatarUrl = "https://pbs.twimg.com/profile_images/1893803697185910784/Na5lOWi5_400x400.jpg",
                text = "Just @Grok it",
                imageUrl = "https://pbs.twimg.com/media/Ge35uqHWQAArfYs?format=jpg",
                likesCount = 132456,
                commentsCount = 6789
            ),
            Post(
                id = 2,
                authorName = "Илон Маск",
                authorNickname = "@elonmusk",
                authorAvatarUrl = "https://pbs.twimg.com/profile_images/1893803697185910784/Na5lOWi5_400x400.jpg",
                text = "Congratulations @SpaceX team on an orbital launch & capture of Starship Super Heavy!",
                imageUrl = null,
                likesCount = 245678,
                commentsCount = 8976
            ),
            Post(
                id = 3,
                authorName = "Илон Маск",
                authorNickname = "@elonmusk",
                authorAvatarUrl = "https://pbs.twimg.com/profile_images/1893803697185910784/Na5lOWi5_400x400.jpg",
                text = "\"The diplomats want war and the soldiers want peace.\"\n" +
                        "\n" +
                        "一 Elon Musk",
                imageUrl = "https://pbs.twimg.com/media/GlnCDsEa8AICwjd?format=jpg&name=large",
                likesCount = 123678,
                commentsCount = 4321
            )
        )
        postAdapter.submitList(posts)
    }
}