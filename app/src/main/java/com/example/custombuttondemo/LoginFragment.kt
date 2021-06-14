package com.example.custombuttondemo

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.custombuttondemo.databinding.FragmentLoginBinding
import com.example.custombuttondemo.databinding.FragmentRegisterBinding
import com.scottyab.aescrypt.AESCrypt

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
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
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setClickListener(View.OnClickListener {
            validdateNLogin()
        })
    }
    fun validdateNLogin(){
        if (binding.metUserName.text.isEmpty()
            || binding.metPassword.text.isEmpty()
        ) {
            Toast.makeText(context, getString(R.string.fields_required), Toast.LENGTH_SHORT).show()
        } else {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            val savedUserName =  AESCrypt.decrypt(Constants.KEY_USER_NAME,sharedPref.getString(Constants.KEY_USER_NAME,""))
            val savedPassword =  AESCrypt.decrypt(Constants.KEY_PASSWORD,sharedPref.getString(Constants.KEY_PASSWORD,""))
            val enteredUserName = binding.metUserName.text.toString()
            val enteredPass = binding.metPassword.text.toString()
             if(savedPassword.equals(enteredPass) && savedUserName.equals(enteredUserName)) {
                 val app = context?.applicationContext as? CustomButtonApp
                 app?.startTime = System.currentTimeMillis()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, WelcomeFragment.newInstance())?.commit()
            } else {
                 Toast.makeText(context, getString(R.string.error_login), Toast.LENGTH_SHORT).show()

             }

        }
    }
    companion object {

        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}