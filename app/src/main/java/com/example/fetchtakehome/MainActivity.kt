package com.example.fetchtakehome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.fetchtakehome.databinding.ActivityMainBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()


        val listApi: ListAPI = retrofit.create<ListAPI>(ListAPI::class.java)
        fetchData(listApi)
    }

    private fun fetchData(api: ListAPI) {
        lifecycleScope.launch {
            try {
                val response = api.fetchItems()
                if (response.isNotEmpty()) {
                    Log.d(TAG, "First item in list: ${response[0]}")
                }


            }
            catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch: ${ex.message}", ex)
            }
        }

    }

}