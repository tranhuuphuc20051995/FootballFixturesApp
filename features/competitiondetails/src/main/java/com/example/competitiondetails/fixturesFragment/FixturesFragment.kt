package com.example.competitiondetails.fixturesFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.base.BaseFragment
import com.example.presentation.utils.Utilities
import com.example.competitiondetails.R
import com.example.competitiondetails.databinding.FixturesFragmentBinding
import com.example.competitiondetails.di.DaggerCompetitionDetailsComponent
import com.example.core.coreComponent
import com.example.presentation.models.Resource
import com.example.presentation.utils.Utilities.getCurrentDate
import com.example.presentation.utils.Utilities.hasInternetConnection
import com.example.presentation.viewmodels.CompetitionDetailsViewModel
import com.example.presentation.viewmodels.CompetitionsViewModel

import io.reactivex.disposables.Disposable
import javax.inject.Inject

class FixturesFragment : BaseFragment() {

    lateinit var binding: FixturesFragmentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FixturesAdapter
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel: CompetitionDetailsViewModel by viewModels { factory }
    var disposable: Disposable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerCompetitionDetailsComponent.factory().create(coreComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fixtures_fragment, container, false)
        val view = binding.root
        binding.click = MyHandler()
        getIntents()
        initRecyclerView()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSingleMatch(competitionId, getCurrentDate())
    }

    private fun getIntents() {
        competitionId = arguments?.getLong("id")!!
    }

    private fun initRecyclerView() {
        adapter = FixturesAdapter(ArrayList())
        recyclerView = binding.fixturesRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    private fun getSingleMatch(id: Long, date: String) {
        disposable = hasInternetConnection().doOnSuccess {
            if (it)
                viewModel.getSingleMatch(id, date).observe(viewLifecycleOwner, Observer { result ->
                    when (result.status) {
                        Resource.Status.LOADING -> {
                            println("Loading")
                        }
                        Resource.Status.ERROR -> {
                            println("Error")
                        }
                        Resource.Status.SUCCESS -> {
                            result.data?.let { data ->
                                if (data.matches.isNullOrEmpty()) {
                                    binding.fixturesRecyclerview.visibility = View.GONE
                                    binding.progressBar.visibility = View.GONE
                                    binding.noFixture.visibility = View.VISIBLE
                                } else {
                                    binding.progressBar.visibility = View.GONE
                                    binding.fixturesRecyclerview.visibility = View.VISIBLE
                                    adapter.updateAdapter(data.matches)
                                }
                            }
                        }
                    }
                })
            else {
                showNoInternet()
            }
        }.doOnError {
            showNoInternet()
        }.subscribe()
    }

    private fun showNoInternet() {
        binding.fixturesRecyclerview.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.noInternet.visibility = View.VISIBLE
    }

    inner class MyHandler {
        fun onTapToRetry(view: View) {
            binding.progressBar.visibility = View.VISIBLE
            binding.noInternet.visibility = View.GONE
            getSingleMatch(competitionId, getCurrentDate())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
    }

    companion object {
        fun newInstance() = FixturesFragment()
        var competitionId: Long = 0L
    }

}
