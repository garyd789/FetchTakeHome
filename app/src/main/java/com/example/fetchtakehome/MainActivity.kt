package com.example.fetchtakehome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
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



        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val listApi: ListAPI = retrofit.create<ListAPI>(ListAPI::class.java)
        fetchData(listApi)



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

                    // Set adapter to the retrieved data and update recyclerView
                    adapter = ItemAdapter(response)
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