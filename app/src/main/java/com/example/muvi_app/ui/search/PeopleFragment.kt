package com.example.muvi_app.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muvi_app.data.adapter.SearchAdapter
import com.example.muvi_app.data.response.Profile
import com.example.muvi_app.data.response.UserResponse
import com.example.muvi_app.databinding.FragmentPeopleBinding
import com.example.muvi_app.ui.detail.MovieDetailActivity
import com.example.muvi_app.ui.social.SocialActivity

class PeopleFragment : Fragment() {

    private lateinit var binding: FragmentPeopleBinding
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observePeopleList()
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter(object : SearchAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Any) {
                if (data is Profile) {
                    val intent = Intent(requireContext(), SocialActivity::class.java).apply {
                        putExtra("USER_ID", data.id)
                    }
                    startActivity(intent)
                }
            }
        })
        binding.rvPeopleResult.adapter = searchAdapter
        binding.rvPeopleResult.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observePeopleList() {
        val viewModel = (activity as SearchActivity).viewModel
        viewModel.peopleList.observe(viewLifecycleOwner) { people ->
            people?.let {
                searchAdapter.setData(it)
            }
        }
    }
}