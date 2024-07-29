package com.example.moviehub.fragments.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.moviehub.R
import com.example.moviehub.api.ApiServices
import com.example.moviehub.api.RetrofitClient
import com.example.moviehub.databinding.FragmentQuizBinding
import com.example.moviehub.model.MovieResult
import com.example.moviehub.utilities.Constants
import com.example.moviehub.utilities.SharedPreferencesHelper
import com.example.moviehub.utilities.Utility
import com.example.moviehub.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch


class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var  quizViewModel:QuizViewModel
    private lateinit var pref: SharedPreferencesHelper
    private lateinit var movieList: List<MovieResult>
    private var currentMovie: MovieResult? = null
    private val utility: Utility = Utility()
    private var score=0
    private lateinit var apiServices: ApiServices
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val retrofit = RetrofitClient.getInstance().getClient()
         apiServices = retrofit.create(ApiServices::class.java)
        quizViewModel = ViewModelProvider(this, ViewModelFactory(apiServices))[QuizViewModel::class.java]

        pref = SharedPreferencesHelper
        pref.initSharedPreferences(requireContext())

        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        binding.textScore.text = pref.getValue(Constants.SCORE, 0).toString()

        utility.checkNetworkConnection(requireContext())

        observeData()
        setListeners()
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            observeData()
        }

    }

    private fun observeData() {
        quizViewModel.getMovieList()
        quizViewModel.movieList.observe(viewLifecycleOwner) { movieList ->
            this.movieList = movieList
            if(movieList.isNotEmpty())
            binding.buttonStart.isEnabled = true
            utility.debug("The list for Quiz is$movieList")
        }
    }


    private fun startQuiz() {
        binding.quizQuesLayout.visibility=View.VISIBLE
        currentMovie = getRandomMovie()
        val moviePosterURL = Constants.POSTER_BASE_URL + currentMovie!!.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .placeholder(R.drawable.blank_person)
            .into(binding.quizImage)

        binding.submitButton.setOnClickListener {
            val userAnswer = binding.answerEditText.text.toString()
            if (userAnswer.equals(currentMovie!!.title, ignoreCase = true)) {
                score++
                pref.saveValue(Constants.SCORE, score)
                showCorrectLayout()
            } else {
                showWrongLayout()
            }
            binding.answerEditText.text.clear()
        }
    }

    private fun getRandomMovie(): MovieResult {
        return movieList.random()
    }

    private fun showCorrectLayout() {
        binding.textScore.text=score.toString()
        binding.quizQuesLayout.visibility = View.GONE
        binding.quizResultCorrect.visibility = View.VISIBLE
        binding.buttonNext.setOnClickListener {
            binding.quizResultCorrect.visibility = View.GONE
            startQuiz()
        }
    }

    private fun showWrongLayout() {
        binding.quizQuesLayout.visibility = View.GONE
        binding.quizResultWrong.visibility = View.VISIBLE
        binding.correctAnswerText.text = currentMovie!!.title
        binding.buttonAgain.setOnClickListener {
            binding.quizResultWrong.visibility = View.GONE
            startQuiz()
        }
    }
    private fun setListeners() {
        binding.buttonStart.setOnClickListener {
            if(utility.checkNetworkConnection(requireContext())){
            binding.quizInfoLayout.visibility=View.GONE
            startQuiz()}
        }
    }

    override fun onResume() {
        super.onResume()
        quizViewModel.getMovieList()
        lifecycleScope.launch {
            observeData()
        }
    }
}