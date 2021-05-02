package com.example.newsapp

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(this)

        val items = fetchData()
        mAdapter = NewsListAdapter(this)
        recycler_view.adapter = mAdapter

    }

    private fun fetchData() {
        // val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=aa499d3304ed4934b992db2deed4e800"
        // error E/Volley: [1763] NetworkUtility.shouldRetryException: Unexpected response code 403 for https://newsapi.org/v2/top-headlines?country=in&apiKey=aa499d3304ed4934b992db2deed4e800
        val url = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
        val newsArray = ArrayList<News>()
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    val newsJsonArray = response.getJSONArray("articles")
                    for (i in 0 until newsJsonArray.length()) {
                        val newsJsonObject = newsJsonArray.getJSONObject(i)
                        val news = News(
                                newsJsonObject.getString("title"),
                                newsJsonObject.getString("author"),
                                newsJsonObject.getString("url"),
                                newsJsonObject.getString("urlToImage"),
                        )
                        newsArray.add(news)
                    }
                    mAdapter.updateNews(newsArray)
                },
                {
                    Log.d("error :", it.localizedMessage!!.toString())
                })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}