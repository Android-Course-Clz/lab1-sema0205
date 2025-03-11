package ru.sema0205.lab1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation

class PostAdapter : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    var onLikeClickListener: ((Post) -> Unit)? = null
    var onCommentClickListener: ((Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPostAuthorAvatar: ImageView = itemView.findViewById(R.id.ivPostAuthorAvatar)
        private val tvPostAuthorName: TextView = itemView.findViewById(R.id.tvPostAuthorName)
        private val tvPostAuthorNickname: TextView =
            itemView.findViewById(R.id.tvPostAuthorNickname)
        private val tvPostText: TextView = itemView.findViewById(R.id.tvPostText)
        private val ivPostImage: ImageView = itemView.findViewById(R.id.ivPostImage)
        private val layoutLike: LinearLayout = itemView.findViewById(R.id.layoutLike)
        private val ivLike: ImageView = itemView.findViewById(R.id.ivLike)
        private val tvLikesCount: TextView = itemView.findViewById(R.id.tvLikesCount)
        private val layoutComment: LinearLayout = itemView.findViewById(R.id.layoutComment)
        private val tvCommentsCount: TextView = itemView.findViewById(R.id.tvCommentsCount)

        fun bind(post: Post) {
            tvPostAuthorName.text = post.authorName
            tvPostAuthorNickname.text = post.authorNickname
            tvPostText.text = post.text
            tvLikesCount.text = post.likesCount.toString()
            tvCommentsCount.text = post.commentsCount.toString()

            Log.d("PostAdapter", "Загрузка аватара поста: ${post.authorAvatarUrl}")

            ivPostAuthorAvatar.load(post.authorAvatarUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.ic_profile_placeholder)
                error(R.drawable.ic_profile_placeholder)
            }

            if (post.imageUrl != null) {
                ivPostImage.visibility = View.VISIBLE
                Log.d("PostAdapter", "Загрузка изображения поста: ${post.imageUrl}")

                ivPostImage.load(post.imageUrl) {
                    crossfade(true)
                }
            } else {
                ivPostImage.visibility = View.GONE
            }

            ivLike.setImageResource(
                if (post.isLiked) R.drawable.ic_like_filled else R.drawable.ic_like
            )

            layoutLike.setOnClickListener {
                onLikeClickListener?.invoke(post)
            }

            layoutComment.setOnClickListener {
                onCommentClickListener?.invoke(post)
            }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}