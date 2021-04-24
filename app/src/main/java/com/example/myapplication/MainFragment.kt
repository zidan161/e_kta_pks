package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentMainBinding
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.ImageListener

class MainFragment : Fragment(), MainPresenter.MainView, ImageListener {

    private var binding: FragmentMainBinding? = null
    private val bind get() = binding!!
    private lateinit var presenter: MainPresenter
    private lateinit var adapter: EventViewAdapter
    private val events: ArrayList<Event> = arrayListOf()

    private var news: MutableList<Int> = mutableListOf(R.drawable.pks1, R.drawable.pks2, R.drawable.pks3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        val view = bind.root

        bind.cvNews.pageCount = news.size
        bind.cvNews.setImageListener(this)
        bind.cvNews.setImageClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.ID, "https://pks.id/content/survey-sebut-39-persen-masyarakat-takut-bicara-politik-bukhori-pemerintahan-jokowi-berpotensi-bawa-mundur-demokrasi")
            startActivity(intent)
        }

        presenter = MainPresenter(this)
        presenter.getNews()

        return view
    }

    override fun setNews(data: List<String>) {

    }

    override fun setEvents(data: List<Event>) {
        events.addAll(data)
        adapter = EventViewAdapter(events)
        bind.rvEvents.layoutManager = LinearLayoutManager(context)
        bind.rvEvents.adapter = adapter
    }

    override fun setImageForPosition(position: Int, imageView: ImageView?) {
        Picasso.get().load(news[position]).into(imageView)
    }
}