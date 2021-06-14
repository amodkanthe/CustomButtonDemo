package com.example.custombuttondemo

import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.custombuttondemo.databinding.FragmentWelcomeBinding
import com.scottyab.aescrypt.AESCrypt


class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentWelcomeBinding>(
            inflater,
            R.layout.fragment_welcome,
            container,
            false
        )

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnableCode)
        (context?.applicationContext as? CustomButtonApp)?.startTimeBackground =
            System.currentTimeMillis()

    }

    override fun onResume() {
        super.onResume()
        checkSessionBackgroundTimeOut(Constants.SESSION_TIME_BACKGRROUND)
        (context?.applicationContext as? CustomButtonApp)?.startTime = System.currentTimeMillis()
        handler.postAtTime(runnableCode, 1000)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setClickListener(View.OnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, LoginFragment.newInstance())?.commit()
        })
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val savedUserFullName = AESCrypt.decrypt(
            Constants.KEY_FULL_NAME,
            sharedPref.getString(Constants.KEY_FULL_NAME, "")
        )
        binding.tvWelcome.setText(getString(R.string.welcome) + " " + savedUserFullName)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            WelcomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    val handler: Handler = Handler()
    val runnableCode = object : Runnable {
        override fun run() {
            checkSessionTimeOut(Constants.SESSION_TIME)
            handler.postDelayed(this, 1000)
        }


    }

    fun checkSessionTimeOut(sessionTimeLimit: Int) {
        val endTime = System.currentTimeMillis()
        val startTime = (context?.applicationContext as? CustomButtonApp)?.startTime
        val timeSpend: Long = endTime - (startTime ?: 0)
        if ((timeSpend / 1000) > sessionTimeLimit) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, LoginFragment.newInstance())?.commit()

        }
    }

    fun checkSessionBackgroundTimeOut(sessionTimeLimit: Int) {
        val endTime = System.currentTimeMillis()
        val startTime = (context?.applicationContext as? CustomButtonApp)?.startTimeBackground
        if ((startTime ?: 0) != 0L) {
            val timeSpend: Long = endTime - (startTime ?: 0)
            if ((timeSpend / 1000) > sessionTimeLimit) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, LoginFragment.newInstance())?.commit()

            }
        }
    }
}
