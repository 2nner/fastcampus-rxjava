package com.maryang.fastrxjava.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.maryang.fastrxjava.R
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.entity.User
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_github_repos.*


class GithubReposActivity : AppCompatActivity() {

    private val viewModel: GithubReposViewModel by lazy {
        GithubReposViewModel()
    }
    private val adapter: GithubReposAdapter by lazy {
        GithubReposAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_repos)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = this.adapter

        refreshLayout.setOnRefreshListener { load() }

        load(true)
    }

    private fun load(showLoading: Boolean = false) {
        if (showLoading)
            showLoading()

        viewModel.getGithubRepos()
            .subscribe(object : DisposableSingleObserver<List<GithubRepo>>() {
                override fun onSuccess(t: List<GithubRepo>) {
                    hideLoading()
                    adapter.items = t
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                }
            })

        /*
        viewModel.getGithubRepos()
            .toMaybe()
            .flatMap { viewModel.getUser() }
            .subscribe()
        */

        /*
        viewModel.getGithubRepos(
            {
                hideLoading()
                adapter.items = it
            },
            {
                hideLoading()
            }
        )
        */
    }

    private fun load2() {
        viewModel.getUser()
            .subscribe(object : DisposableMaybeObserver<User>() {
                override fun onSuccess(t: User) {
                    hideLoading()
                }

                override fun onComplete() {
                    hideLoading()
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                }
            })
    }

    private fun load3() {
        viewModel.updateUser()
            .subscribe(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    hideLoading()
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                }
            })
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        refreshLayout.isRefreshing = false
    }
}
