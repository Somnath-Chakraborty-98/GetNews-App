package com.example.getnewsapp.ui.fragments



import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.getnewsapp.R
import com.example.getnewsapp.ui.NewsActivity
import com.example.getnewsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_news_details.*


class NewsDetailsFragment : Fragment(R.layout.fragment_news_details) {

    lateinit var viewModel: NewsViewModel
    val args: NewsDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        val article = args.article

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view,"Saved",Snackbar.LENGTH_SHORT).show()
        }


    }

}