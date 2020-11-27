package com.example.nasaobjects.ui.home

import NasaObjectAdapter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaobjects.NasaObject
import com.example.nasaobjects.NasaService
import com.example.nasaobjects.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.util.*
import java.util.stream.Collectors


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var disposable: CompositeDisposable = CompositeDisposable()
    private lateinit var root: View;

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        this.root = root
        return root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        this.disposable.add(
                NasaService.getNasaObjects().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError { e ->
                            println(e.message)
                        }
                        .subscribe {
                            var filteredObjects: List<NasaObject> = it.stream().filter({it.getYear().year > LocalDate.now().minusYears(15).year}).collect(Collectors.toList())
                            val rvObjects = root.findViewById(R.id.nasa_objects) as RecyclerView
                            val adapter = NasaObjectAdapter(filteredObjects)
                            rvObjects.adapter = adapter
                            rvObjects.layoutManager = LinearLayoutManager(context)
                        })
    }
}