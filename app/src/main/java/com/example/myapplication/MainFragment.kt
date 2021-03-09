package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.myapplication.databinding.FragmentMainBinding
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.ImageListener

class MainFragment : Fragment(), MainPresenter.MainView, ImageListener {

    private var binding: FragmentMainBinding? = null
    private val bind get() = binding!!
    private lateinit var presenter: MainPresenter

    private var news: MutableList<Int> = mutableListOf(R.drawable.pks1, R.drawable.pks2, R.drawable.pks3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        val view = bind.root

        bind.cvNews.pageCount = news.size
        bind.cvNews.setImageListener(this)

        presenter = MainPresenter(this)
        presenter.getNews()

        return view
    }

    override fun setNews(data: List<String>) {
        TODO("Not yet implemented")
    }

    override fun setImageForPosition(position: Int, imageView: ImageView?) {
        Picasso.get().load(news[position]).into(imageView)
    }
}