package com.example.muvi_app.ui.search

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.muvi_app.R
import com.example.muvi_app.data.pref.UserPreference
import com.example.muvi_app.data.pref.dataStore
import com.example.muvi_app.databinding.ActivitySearchBinding
import com.example.muvi_app.repository.MovieRepository
import com.example.muvi_app.repository.UserRepository
import com.example.muvi_app.ui.main.MainActivity
import com.example.muvi_app.ui.settings.SettingsActivity
import com.example.muvi_app.ui.social.SocialActivity
import com.google.android.material.tabs.TabLayout

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    internal lateinit var viewModel: SearchViewModel

    private lateinit var userRepository: UserRepository
    private lateinit var movieRepository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataStore = applicationContext.dataStore
        userRepository = UserRepository(UserPreference.getInstance(dataStore))
        movieRepository = MovieRepository(UserPreference.getInstance(dataStore))

        initializeViewModel()
        setupViewPagerAndTabs()
        setupSearchBar()
        setupView()
    }

    private fun initializeViewModel() {
        val viewModelFactory = SearchViewModelFactory(userRepository, movieRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    private fun setupViewPagerAndTabs() {
        val adapter = SearchPagerAdapter(supportFragmentManager)
        val movieFragment = MoviesFragment()
        val peopleFragment = PeopleFragment()

        adapter.addFragment(movieFragment, getString(R.string.movies))
        adapter.addFragment(peopleFragment, getString(R.string.people))

        binding.viewPager.adapter = adapter
        binding.searchTab.setupWithViewPager(binding.viewPager)

        binding.searchTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.position) {
                        0 -> viewModel.searchMovies(binding.searchView.query.toString())
                        1 -> viewModel.searchPeople(binding.searchView.query.toString())
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupSearchBar() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    when (binding.searchTab.selectedTabPosition) {
                        0 -> viewModel.searchMovies(it)
                        1 -> viewModel.searchPeople(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    class SearchPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragmentList = ArrayList<Fragment>()
        private val fragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }
    }

    private fun setupBottomAppBar() {
        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.searchButton.setOnClickListener {
            // do nothing
        }

        binding.socialButton.setOnClickListener {
            startActivity(Intent(this, SocialActivity::class.java))
            finish()
        }

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
    }

    private fun setupView() {
        setupFullscreenMode()
        setupBottomAppBar()
    }

    private fun setupFullscreenMode() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}