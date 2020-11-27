package com.example.nasaobjects.ui.home

import NasaObjectAdapter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaobjects.*
import com.example.nasaobjects.ui.NasaObjectMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.LocalDate
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList


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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch { getObjects(disposable, root) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getObjects(disposable: CompositeDisposable, root: View) = withContext(Dispatchers.IO) {
        val localObjects = NasaCustomDatabase.getDatabase(root.context).nasaObjectDao().findAll().stream().map { NasaObjectMapper.entityToObject(it) }.collect(Collectors.toList())
        disposable.add(
            NasaService.getNasaObjects().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e ->

                }
                .subscribe {
                    try {
                        var filteredObjectEntities: ArrayList<NasaObject> = ArrayList(it.stream().filter({ it.getYear().year > (LocalDate.now().minusYears(15)).year }).collect(Collectors.toList()))
                        filteredObjectEntities.addAll(localObjects)
                        filteredObjectEntities = ArrayList(filteredObjectEntities.sortedBy { it.getName().toLowerCase(Locale.ROOT) })
                        val rvObjects = root.findViewById(R.id.nasa_objects) as RecyclerView
                        val adapter = NasaObjectAdapter(filteredObjectEntities, root.context)
                        rvObjects.adapter = adapter
                        Toast.makeText(root.context, getString(R.string.toast_fetched_data), Toast.LENGTH_LONG).show()
                        rvObjects.layoutManager = LinearLayoutManager(context)
                    } catch (E: Exception) {
                        Toast.makeText(root.context, getString(R.string.get_impossible), Toast.LENGTH_LONG)
                    }
                })
    }
}
