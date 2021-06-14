package com.example.custombuttondemo

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.custombuttondemo.databinding.FragmentRegisterBinding
import com.scottyab.aescrypt.AESCrypt


class RegisterFragment : Fragment() {


    private lateinit var binding: FragmentRegisterBinding
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
        binding = DataBindingUtil.inflate<FragmentRegisterBinding>(
            inflater,
            R.layout.fragment_register,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setClickListener(View.OnClickListener {
            validateNRegisterUser()
        })

    }

    fun validateNRegisterUser() {
        if (binding.metFullname.text.isEmpty()
            || binding.metUserName.text.isEmpty()
            || binding.metPassword.text.isEmpty()
        ) {
            Toast.makeText(context, getString(R.string.fields_required), Toast.LENGTH_SHORT).show()
        } else {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            sharedPref.edit().putString(
                Constants.KEY_FULL_NAME,
                AESCrypt.encrypt(Constants.KEY_FULL_NAME, binding.metFullname.text.toString())
            ).apply()
            sharedPref.edit().putString(
                Constants.KEY_PASSWORD,
                AESCrypt.encrypt(Constants.KEY_PASSWORD, binding.metPassword.text.toString())
            ).apply()
            sharedPref.edit().putString(
                Constants.KEY_USER_NAME,
                AESCrypt.encrypt(Constants.KEY_USER_NAME, binding.metUserName.text.toString())
            ).apply()
            sharedPref.edit().putBoolean(Constants.KEY_ISREGISTERD,true).apply()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, LoginFragment.newInstance())?.commit()


        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}