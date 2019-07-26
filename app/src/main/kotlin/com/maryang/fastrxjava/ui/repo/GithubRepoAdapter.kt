package com.maryang.fastrxjava.ui.repo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maryang.fastrxjava.R
import com.maryang.fastrxjava.base.BaseApplication
import com.maryang.fastrxjava.entity.GithubRepo
import kotlinx.android.synthetic.main.item_github_repo.view.*
import org.jetbrains.anko.imageResource

class GithubRepoAdapter : RecyclerView.Adapter<GithubRepoAdapter.RepoViewHolder>() {

    var items: List<GithubRepo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder = RepoViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_github_repo, parent, false)
    )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RepoViewHolder, pos: Int) {
        holder.bind(items[pos])
        Log.d(BaseApplication.TAG, items.size.toString())
    }

    class RepoViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(repo: GithubRepo) {
            with(itemView) {
                repoName.text = repo.name
                repoDescription.text = repo.description
                repoStar.imageResource =
                    if (repo.star) R.drawable.baseline_star_24
                    else R.drawable.baseline_star_border_24
            }
        }
    }
}