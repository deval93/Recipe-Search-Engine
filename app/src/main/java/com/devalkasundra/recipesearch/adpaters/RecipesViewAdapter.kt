package com.devalkasundra.recipesearch.adpaters

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.devalkasundra.recipesearch.R
import com.devalkasundra.recipesearch.interfaces.OnLoadMoreListener
import com.devalkasundra.recipesearch.models.Recipe


class RecipesViewAdapter(private val context: Context, private var recipes: ArrayList<Recipe>, recyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private val visibleThreshold = 10
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var loading: Boolean = false
    private var onLoadMoreListener: OnLoadMoreListener? = null
    private var pageLoaded = 0

    init {

        if (recyclerView.layoutManager is LinearLayoutManager) {

            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

            recyclerView
                    .addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView?,
                                                dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)

                            totalItemCount = linearLayoutManager.itemCount
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition()
                            if (!loading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener!!.onLoadMore(pageLoaded)
                                }
                                loading = true
                            }
                        }
                    })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (recipes[position].title != null) VIEW_ITEM else VIEW_PROG
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        vh = if (viewType == VIEW_ITEM) {
            val v = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_recipe, parent, false)

            RecipeViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_progressbar, parent, false)

            ProgressViewHolder(v)
        }
        return vh
//        val layoutView = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
//        return ViewHolder(layoutView)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecipeViewHolder) {
            holder.title.text = recipes[position].title.trim()
            holder.ingredients.text = recipes[position].ingredients.trim()

            Glide
                    .with(context)
                    .setDefaultRequestOptions(RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH))
                    .load(recipes[position].thumbnail).into(holder.thumbnail)

            holder.showDetails.setOnClickListener {
                val customTabsIntent = CustomTabsIntent.Builder()
                        .addDefaultShareMenuItem()
                        .setToolbarColor(context.resources.getColor(R.color.colorPrimary))
                        .setShowTitle(true)
                        .build()

                customTabsIntent.launchUrl(context, Uri.parse(recipes[position].href))
            }
        } else {

        }
    }

    fun setLoaded() {
        loading = false
        if (recipes.size > 0 && recipes.last().title == null)
            recipes.removeAt(recipes.size - 1)
        notifyDataSetChanged()
    }

    fun addRecipes(recipes: ArrayList<Recipe>, pageLoaded: Int) {
        this.recipes.addAll(recipes)
        this.pageLoaded = pageLoaded
        notifyDataSetChanged()
    }

    fun clearAll() {
        recipes.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    fun add(recipe: Recipe) {
        recipes.add(recipe)
        notifyDataSetChanged()
    }

    fun setRecipes(recipes: ArrayList<Recipe>, pageLoaded: Int) {
        this.recipes = recipes
        this.pageLoaded = pageLoaded
        notifyDataSetChanged()
    }

    class RecipeViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        var ingredients: TextView = itemView.findViewById(R.id.ingredients)
        var showDetails: ImageButton = itemView.findViewById(R.id.showDetails)
    }

    class ProgressViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {
        var progressBar: ProgressBar = v.findViewById<View>(R.id.progressBar) as ProgressBar
    }

}
