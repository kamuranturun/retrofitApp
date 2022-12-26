package com.kamuran.mmaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kamuran.mmaps.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        //request

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible=true
            val response= try {
                RetrofitInstance.api.getTodos()
            }catch (e:IOException){
                Log.e("TAG","IOException, you might not have internet connection")
                binding.progressBar.isVisible=false
                return@launchWhenCreated
            }catch (e:HttpException){
                Log.e("TAG","HttpException , unexpected response")
                binding.progressBar.isVisible=false
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() !=null){
                todoAdapter.todos=response.body()!!
            }else{
                Log.e("TAG","Response not succesful")

            }
            binding.progressBar.isVisible=false

        }
    }

    private fun setupRecyclerView()= binding.reTodos.apply {
        todoAdapter= TodoAdapter()
        adapter=todoAdapter
        layoutManager= LinearLayoutManager(this@MainActivity)
    }
}