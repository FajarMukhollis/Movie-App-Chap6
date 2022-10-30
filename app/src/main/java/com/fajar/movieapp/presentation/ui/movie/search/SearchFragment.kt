package com.fajar.movieapp.presentation.ui.movie.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fajar.movieapp.databinding.FragmentSearchBinding
import com.fajar.movieapp.presentation.ui.movie.search.adapter.SearchAdapter
import com.fajar.movieapp.resource.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private val adapter: SearchAdapter by lazy {
        SearchAdapter {
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(it.id!!)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onInputTextChanged()
        initList()
        observeData()
    }

    private fun onInputTextChanged() {
        binding.tilSearch.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(query: Editable?) {
                viewModel.searchMovie(query.toString())
            }
        })
    }

    private fun observeData() {
        viewModel.searchResult.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Error -> {}
                is Resource.Empty -> {}
                is Resource.Success -> {
                    adapter.submitList(it.payload)
                }
            }
        }
    }

    private fun initList() {
        binding.rvSearchMovies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}