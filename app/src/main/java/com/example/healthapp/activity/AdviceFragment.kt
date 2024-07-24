package com.example.healthapp.activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.healthapp.R

class AdviceFragment : Fragment() {

    companion object {
        private const val ARG_RESULT = "result"

        fun newInstance(result: String): AdviceFragment {
            val fragment = AdviceFragment()
            val args = Bundle()
            args.putString(ARG_RESULT, result)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_advice, container, false)
        val resultTextView: TextView = view.findViewById(R.id.resultTextView)

        val result = arguments?.getString(ARG_RESULT)
        resultTextView.text = result

        return view
    }
}

