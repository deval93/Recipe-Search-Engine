package com.devalkasundra.recipesearch

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.devalkasundra.recipesearch.adpaters.RecipesViewAdapter
import com.devalkasundra.recipesearch.apis.ApiClient
import com.devalkasundra.recipesearch.apis.ApiInterface
import com.devalkasundra.recipesearch.interfaces.OnLoadMoreListener
import com.devalkasundra.recipesearch.models.DataResponse
import com.devalkasundra.recipesearch.models.Recipe
import com.devalkasundra.recipesearch.utilities.DNUtilities
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnLoadMoreListener {

    private var adapter: RecipesViewAdapter? = null
    private val recipes: ArrayList<Recipe> = ArrayList()
    private var call: Call<DataResponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViewAdapter()
        fetchData("")
        searchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                call?.cancel()
                if (!DNUtilities.hasConnection(this@MainActivity)) {
                    DNUtilities.showAlertView(
                            this@MainActivity,
                            R.string.network_error,
                            R.string.no_network_connection)
                    adapter?.setLoaded()
                    return
                }
                if (!p0?.toString()?.trim()?.isBlank()!!) {
                    fetchData(p0.toString().trim())
                } else {
                    fetchData(p0.toString().trim())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                call?.cancel()
                adapter?.setLoaded()
            }

        })
    }

    private fun fetchData(queryString: String) {
        adapter?.clearAll()
        adapter?.add(Recipe())
        val apiService = ApiClient.client.create(ApiInterface::class.java)
        try {

            call = apiService.getRecipes(1.toString(), queryString)
            call?.enqueue(object : Callback<DataResponse> {
                override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                    adapter?.setLoaded()
                    if (response.isSuccessful) {
                        adapter?.setRecipes(response.body().results as java.util.ArrayList<Recipe>, 1)
                    } else {

                    }
                }

                override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                    adapter?.setLoaded()
                }
            })
        } catch (ignored: Exception) {
            Toast.makeText(this@MainActivity, ignored.message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    private fun setViewAdapter() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?
        recyclerView.setHasFixedSize(false)
        adapter = RecipesViewAdapter(this, recipes, recyclerView)
        recyclerView.adapter = adapter
        adapter?.setOnLoadMoreListener(this)
    }

    override fun onLoadMore(pageLoaded: Int) {
        //this will show loading
        adapter?.add(Recipe())

        if (!DNUtilities.hasConnection(this@MainActivity)) {
            DNUtilities.showAlertView(
                    this@MainActivity,
                    R.string.network_error,
                    R.string.no_network_connection)
            adapter?.setLoaded()
            return
        }

        Handler().postDelayed({
            loadNextPage((pageLoaded + 1))
        }, 500)
    }

    private fun loadNextPage(nextPage: Int) {
        val apiService = ApiClient.client.create(ApiInterface::class.java)
        try {
            call = apiService.getRecipes(nextPage.toString(), searchEt.text.toString().trim())
            call?.enqueue(object : Callback<DataResponse> {
                override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                    adapter?.setLoaded()
                    if (response.isSuccessful) {
                        adapter?.addRecipes(response.body().results as java.util.ArrayList<Recipe>, nextPage)
                    } else {

                    }
                }

                override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                    adapter?.setLoaded()
                }
            })
        } catch (ignored: Exception) {
            Toast.makeText(this, ignored.message, Toast.LENGTH_SHORT).show()
        }
    }
}
