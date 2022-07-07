package com.lenatopoleva.weather.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.lenatopoleva.weather.CITY
import com.lenatopoleva.weather.MESSAGE_FROM_FIRST_FRAGMENT
import com.lenatopoleva.weather.R
import com.lenatopoleva.weather.REQUEST_KEY
import java.util.regex.Pattern


class SecondFragment : Fragment() {
    private var cityEditText: EditText? = null
    private var textInputLayout: TextInputLayout? = null

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setOnClickListeners()
        // Получаем данные из аргументов и отображаем в виде toast
        val message: String? = arguments?.getString(MESSAGE_FROM_FIRST_FRAGMENT)
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun initViews(view: View) {
        cityEditText = view.findViewById(R.id.cityEditText)
        textInputLayout = view.findViewById(R.id.textInputLayout)
    }

    private fun setOnClickListeners() {
        // Устанавливаем слушателя нажатий на Enter клавиатуры
        cityEditText?.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                            Log.d("CHOSEN_CITY", "enter clicked")
                            if (validateCity(cityEditText?.text.toString())) {
                                sendMessage()
                                navigateBack()
                                return true
                            }
                        }
                        else -> {}
                    }
                }
                return false
            }
        })

        cityEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateCity(cityEditText?.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun validateCity(city: String): Boolean {
        val isCity = checkEnterCity.matcher(city).matches()
        return if (isCity) {
            hideError()
            true
        } else {
            showError()
            false
        }
    }

    private fun showError() {
        textInputLayout?.error = getString(R.string.error)
    }

    private fun hideError() {
        textInputLayout?.error = null
    }

    private fun sendMessage() {
        val chosenCity = cityEditText?.text.toString()
        // Отправляем полученный город в FirstFragment
        activity?.supportFragmentManager?.setFragmentResult(
            REQUEST_KEY,
            bundleOf(CITY to chosenCity)
        )
    }

    private fun navigateBack() {
        activity?.supportFragmentManager?.popBackStack()
    }

    companion object {
        fun newInstance(messageToSecondFragment: String?): SecondFragment {
            val secondFragment = SecondFragment()

            val args: Bundle = Bundle()
            args.putString(MESSAGE_FROM_FIRST_FRAGMENT, messageToSecondFragment)
            secondFragment.arguments = args
            return secondFragment
        }

        val checkEnterCity = Pattern.compile("^[а-яА-ЯЁa-zA-Z]+(?:[\\s-][а-яА-ЯЁa-zA-Z]+)*$")
    }
}