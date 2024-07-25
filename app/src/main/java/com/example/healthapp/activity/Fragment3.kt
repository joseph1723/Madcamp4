package com.example.healthapp.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.healthapp.R
import java.util.logging.Handler

data class Exercise(
    val name: String,
    val answers: Map<Int, Int>,
    val description: String,
    val videoUrl: String
)

data class ExerciseProbability(
    val name: String,
    val description: String,
    val videoUrl: String,
    val probability: Double
)

class Fragment3 : Fragment() {

    private lateinit var genieImageView: ImageView
    private lateinit var questionTextView: TextView
    private lateinit var yesButton: Button
    private lateinit var maybeYesButton: Button
    private lateinit var unknownButton: Button
    private lateinit var maybeNoButton: Button
    private lateinit var noButton: Button

    private val questions = mapOf(
        1 to "좌우를 따로 하는 운동입니까?",
        2 to "당기는 운동입니까?",
        3 to "머신을 사용하는 운동입니까?",
        4 to "하체 운동입니까?"
    )

    private val exercises = listOf(
        Exercise("스플릿 레그 프레스 머신", mapOf(1 to 1, 2 to 1, 3 to 1, 4 to 1), "스플릿 레그 프레스 머신에 대한 설명", "https://www.youtube.com/embed/videoid1"),
        Exercise("체스트 프레스 머신", mapOf(1 to 1, 2 to 1, 3 to 1, 4 to 0), "체스트 프레스 머신에 대한 설명", "https://www.youtube.com/embed/videoid2"),
        Exercise("런지", mapOf(1 to 1, 2 to 1, 3 to 0, 4 to 1), "런지에 대한 설명", "https://www.youtube.com/embed/videoid3"),
        Exercise("덤벨 숄더 프레스", mapOf(1 to 1, 2 to 1, 3 to 0, 4 to 0), "덤벨 숄더 프레스에 대한 설명", "https://www.youtube.com/embed/videoid4"),
        Exercise("스플릿 레그 컬 머신", mapOf(1 to 1, 2 to 0, 3 to 1, 4 to 1),"스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5"),
        Exercise("시티드 로우 머신", mapOf(1 to 1, 2 to 0, 3 to 1, 4 to 0), "스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5"),
        Exercise("덤벨 데드리프트", mapOf(1 to 1, 2 to 0, 3 to 0, 4 to 1),"스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5"),
        Exercise("덤벨 로우", mapOf(1 to 1, 2 to 0, 3 to 0, 4 to 0),"스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5"),
        Exercise("레그 프레스 머신", mapOf(1 to 0, 2 to 1, 3 to 1, 4 to 1),"스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5"),
        Exercise("체스트 프레스 머신", mapOf(1 to 0, 2 to 1, 3 to 1, 4 to 0),"스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5"),
        Exercise("바벨 스쿼트", mapOf(1 to 0, 2 to 1, 3 to 0, 4 to 1),"스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5"),
        Exercise("바벨 벤치 프레스", mapOf(1 to 0, 2 to 1, 3 to 0, 4 to 0),"스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5"),
        Exercise("레그 컬 머신", mapOf(1 to 0, 2 to 1, 3 to 1, 4 to 1),"스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5"),
        Exercise("렛 풀다운 머신", mapOf(1 to 0, 2 to 0, 3 to 1, 4 to 0),"스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5"),
        Exercise("바벨 데드리프트", mapOf(1 to 0, 2 to 0, 3 to 0, 4 to 1),"스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5"),
        Exercise("바벨 로우", mapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0),"스플릿 레그 컬 머신에 대한 설명", "https://www.youtube.com/embed/videoid5")
    )

    private val questionsSoFar = mutableListOf<Int>()
    private val answersSoFar = mutableListOf<Double>()
    private var isMatShown = false

    private val questionsLeft: List<Int>
        get() = questions.keys.toList() - questionsSoFar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_3, container, false)

        genieImageView = view.findViewById(R.id.itemImage)
        questionTextView = view.findViewById(R.id.questionTextView)
        yesButton = view.findViewById(R.id.yesButton)
        maybeYesButton = view.findViewById(R.id.maybeYesButton)
        unknownButton = view.findViewById(R.id.unknownButton)
        maybeNoButton = view.findViewById(R.id.maybeNoButton)
        noButton = view.findViewById(R.id.noButton)

        yesButton.setOnClickListener {
            handleAnswer(1.0)
        }

        maybeYesButton.setOnClickListener {
            handleAnswer(0.75)
        }

        unknownButton.setOnClickListener {
            handleAnswer(0.5)
        }

        maybeNoButton.setOnClickListener {
            handleAnswer(0.25)
        }

        noButton.setOnClickListener {
            handleAnswer(0.0)
        }

        askNextQuestion()

        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setGenieImageViewSth() {
        genieImageView.setOnTouchListener(object : View.OnTouchListener {
            private val handler = android.os.Handler()
            private var isLongPress = false

            private val longPressRunnable = Runnable {
                isLongPress = true
                showHimSth()
            }

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        isLongPress = false
                        handler.postDelayed(longPressRunnable, 3000)
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        handler.removeCallbacks(longPressRunnable)
                        if (!isLongPress) {
                            v?.performClick()
                        }
                    }
                }
                return true
            }
        })
    }

    private fun handleAnswer(answer: Double) {
        val currentQuestion = questionsLeft.firstOrNull()
        if (currentQuestion != null) {
            questionsSoFar.add(currentQuestion)
            answersSoFar.add(answer)
            askNextQuestion()
        }
    }

    private fun askNextQuestion() {
        val nextQuestion = questionsLeft.firstOrNull()
        if (nextQuestion == null) {
            val result = calculateProbabilities().maxByOrNull { it.probability }

            result?.let {
                showResultDialogue(it.name, it.description, it.videoUrl)
            }
        } else {
            questionTextView.text = questions[nextQuestion]
        }
    }
    private fun showHimSth() {
        if (!isMatShown) {
            isMatShown = true
            val dialogView = layoutInflater.inflate(R.layout.mat_dialog, null)
            AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("확인") { _, _ ->
                    isMatShown = false
                }
                .show()
        }
    }
    private fun showResultDialogue(result: String, description: String, videoUrl: String) {
        val dialogView = layoutInflater.inflate(R.layout.alert_dialog_result, null)
        val resultTextView: TextView = dialogView.findViewById(R.id.resultTextView)
        val descriptionTextView: TextView = dialogView.findViewById(R.id.descriptionTextView)
        val youtubeWebView: WebView = dialogView.findViewById(R.id.youtubeWebView)

        resultTextView.text = result
        descriptionTextView.text = description
        val videoHtml =
            "<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl\" frameborder=\"0\" allowfullscreen></iframe>"
        youtubeWebView.settings.javaScriptEnabled = true
        youtubeWebView.loadData(videoHtml, "text/html", "utf-8")

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("확인") { _, _ ->
                resetFragment()
            }.show()
    }

    private fun resetFragment() {
        questionsSoFar.clear()
        answersSoFar.clear()
        askNextQuestion()
    }

    private fun calculateProbabilities(): List<ExerciseProbability> {
        val probabilities = mutableListOf<ExerciseProbability>()
        for (exercise in exercises) {
            val probability = calculateExerciseProbability(exercise)
            probabilities.add(ExerciseProbability(exercise.name, exercise.description, exercise.videoUrl, probability))
        }
        return probabilities
    }

    private fun calculateExerciseProbability(exercise: Exercise): Double {
        val P_exercise = 1.0 / exercises.size
        var P_answers_given_exercise = 1.0
        var P_answers_given_not_exercise = 1.0

        for ((question, answer) in questionsSoFar.zip(answersSoFar)) {
            P_answers_given_exercise *= maxOf(
                1 - kotlin.math.abs(answer - exercise.answers[question]!!),
                0.01
            )
            val P_answers_not_exercise = exercises
                .filter { it.name != exercise.name }
                .map { 1 - kotlin.math.abs(answer - it.answers[question]!!) }
                .average()
            P_answers_given_not_exercise *= maxOf(P_answers_not_exercise, 0.01)
        }

        val P_answer =
            P_exercise * P_answers_given_exercise + (1 - P_exercise) * P_answers_given_not_exercise

        return (P_answers_given_exercise * P_exercise) / P_answer
    }
}
