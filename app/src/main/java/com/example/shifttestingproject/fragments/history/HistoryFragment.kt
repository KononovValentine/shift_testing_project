package com.example.shifttestingproject.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.shifttestingproject.MainActivity
import com.example.shifttestingproject.MainViewModel
import com.example.shifttestingproject.R
import com.example.shifttestingproject.databinding.FragmentHystoryBinding

class HistoryFragment : Fragment() {

    lateinit var binding: FragmentHystoryBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HistoryAdapter
    lateinit var model: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHystoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        // слушатель нажатия на item в recyclerView
        adapter.onItemClick = {
            (activity as MainActivity).showBinInfoFromHistory(it)
        }

        // слушатель долгого нажатия на item в recyclerView
        adapter.onItemLongClick = {
            model.delete(it) {}
        }
    }

    // инициализация
    private fun init() {
        val viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        model = MainViewModel()
        recyclerView = binding.rvHistory
        adapter = HistoryAdapter()
        recyclerView.adapter = adapter

        viewModel.getAllBins().observe(viewLifecycleOwner) { listBins ->
            adapter.setList(listBins.asReversed())
        }

        Toast.makeText(activity, getString(R.string.history_info), Toast.LENGTH_LONG).show()
    }
}