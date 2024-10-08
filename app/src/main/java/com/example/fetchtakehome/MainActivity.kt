package com.example.fetchtakehome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fetchtakehome.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val TAG = "MainActivity"
private lateinit var adapter: ItemAdapter
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Set up adapter and recyclerView
        adapter = ItemAdapter(emptyList())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Fetch Rewards Coding Exercise"



        // Set up Moshi and Retrofit
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val listApi: ListAPI = retrofit.create<ListAPI>(ListAPI::class.java)
        fetchData(listApi)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.actionSearch)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
        return true
    }
    // Function used to fetch data from URL
    private fun fetchData(api: ListAPI) {
        lifecycleScope.launch {
            try {
                val response = api.fetchItems()
                if (response.isEmpty()) {
                   Toast.makeText(this@MainActivity, "No items found!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d(TAG, "First item in list: ${response[0]}")
                    val sortedData = processItems(response)
                    // Set adapter to the sorted data and update recyclerView
                    adapter = ItemAdapter(sortedData)
                    binding.recyclerView.adapter = adapter
                }
            }
            catch (ex: Exception) {
                Toast.makeText(this@MainActivity, "Error in request!", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failed to fetch: ${ex.message}", ex)
            }
        }

    }



}