package com.maryang.fastrxjava.ui.repo

import android.util.Log
import com.maryang.fastrxjava.base.BaseApplication
import com.maryang.fastrxjava.data.repository.GithubRepository
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.util.applySchedulersExtension
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.getStackTraceString

class GithubRepoViewModel {
    private val repository = GithubRepository()

    fun onClickStar(repo: GithubRepo) =
        (if (repo.star)
            repository.unstar(repo.owner.userName, repo.name)
        else
            repository.star(repo.owner.userName, repo.name))
            .observeOn(AndroidSchedulers.mainThread())

    fun searchGithubOtherRepoObservable(username: String) =
        Single.create<List<GithubRepo>> { emitter ->
            repository.searchOtherRepos(username)
                .subscribe({
                    Completable.merge(
                        it.map {
                            repository.checkStar(it.owner.userName, it.name)
                                .doOnComplete { it.star = true }
                                .onErrorComplete()
                        }
                    ).subscribe {
                        Log.d(BaseApplication.TAG, "emitter -> onSuccess()")
                        emitter.onSuccess(it)
                    }
                }, { Log.d(BaseApplication.TAG, it.getStackTraceString()) })
        }
            .applySchedulersExtension()

}