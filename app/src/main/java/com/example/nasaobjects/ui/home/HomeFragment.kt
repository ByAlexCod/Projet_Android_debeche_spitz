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
import com.example.nasaobjects.NasaObject
import com.example.nasaobjects.NasaService
import com.example.nasaobjects.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var disposable: CompositeDisposable = CompositeDisposable()
    private lateinit var  root: View;

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        this.root = root
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        var objectList: ArrayList<NasaObject> = ArrayList<NasaObject>()
        var adapter: NasaObjectAdapter = NasaObjectAdapter(context, objectList);
        var list : ListView = root.findViewById(R.id.nasaObjects)
        list.adapter = adapter
        this.disposable.add(
            NasaService.getNasaObjects().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError{
                    e -> println(e.message)
            }
            .subscribe{
                println(it.count())
                it.forEach{ ob -> adapter.add(ob)
                }
            })
    }
}