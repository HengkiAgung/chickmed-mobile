package com.example.chickmed.ui.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chickmed.AppViewModel
import com.example.chickmed.data.di.Injection
import com.example.chickmed.data.repository.ArticleRepository
import com.example.chickmed.data.repository.ReportRepository
import com.example.chickmed.data.repository.ScheduleRepository
import com.example.chickmed.data.repository.UserRepository
import com.example.chickmed.ui.screen.Auth.AuthViewModel
import com.example.chickmed.ui.screen.account.change_password.ChangePasswordViewModel
import com.example.chickmed.ui.screen.account.my_account.MyAccountViewModel
import com.example.chickmed.ui.screen.account.profile.ProfileViewModel
import com.example.chickmed.ui.screen.analysis.detail_analysis.DetailAnalysisViewModel
import com.example.chickmed.ui.screen.analysis.report.ReportViewModel
import com.example.chickmed.ui.screen.article.article.ArticleViewModel
import com.example.chickmed.ui.screen.article.detail_article.DetailArticleViewModel
import com.example.chickmed.ui.screen.home.HomeViewModel
import com.example.chickmed.ui.screen.schedule.ScheduleViewModel

class ViewModelFactory(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
    private val reportRepository: ReportRepository,
    private val scheduleRepository: ScheduleRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    private val viewModelMap = mapOf(
        HomeViewModel::class.java to {
            HomeViewModel(
                articleRepository,
                userRepository,
            )
        },
        ReportViewModel::class.java to { ReportViewModel(reportRepository) },
        ScheduleViewModel::class.java to { ScheduleViewModel(scheduleRepository) },
        ArticleViewModel::class.java to { ArticleViewModel(articleRepository) },
        DetailArticleViewModel::class.java to { DetailArticleViewModel(articleRepository) },
        ProfileViewModel::class.java to { ProfileViewModel(userRepository) },
        MyAccountViewModel::class.java to { MyAccountViewModel(userRepository) },
        DetailAnalysisViewModel::class.java to { DetailAnalysisViewModel(reportRepository) },
        AuthViewModel::class.java to { AuthViewModel(userRepository) },
        AppViewModel::class.java to { AppViewModel(userRepository) },
        ChangePasswordViewModel::class.java to { ChangePasswordViewModel(userRepository) },
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        viewModelMap[modelClass]?.let {
            return it.invoke() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideArticleRepository(context),
                    Injection.provideUserRepository(context),
                    Injection.provideReportRepository(context),
                    Injection.provideScheduleRepository(context)
                )
            }.also { INSTANCE = it }
    }
}
