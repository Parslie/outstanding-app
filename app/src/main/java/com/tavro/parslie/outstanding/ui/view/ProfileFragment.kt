package com.tavro.parslie.outstanding.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.databinding.FragmentProfileBinding
import com.tavro.parslie.outstanding.ui.viewmodel.ContextualViewModelFactory
import com.tavro.parslie.outstanding.ui.viewmodel.ProfileViewModel
import com.tavro.parslie.outstanding.util.Status

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: ProfileViewModel

    private var userID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userID = requireArguments().getInt(USER_ID_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        initViewModel()

        viewModel.fetchUser(userID)
        return binding.root
    }

    private fun initViewModel() {
        val viewModelFactory = ContextualViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        viewModel.getUserData().observe(requireActivity()) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.profileProgressBar.visibility = View.GONE
                    binding.profileUsername.text = it.data?.username
                    binding.profileDescription.text = it.data?.description
                }
                Status.LOADING -> {
                    binding.profileProgressBar.visibility = View.VISIBLE
                    binding.profileUsername.text = ""
                    binding.profileDescription.text = ""
                }
                Status.ERROR -> {
                    binding.profileProgressBar.visibility = View.GONE
                    binding.profileUsername.text = "N/A"
                    binding.profileDescription.text = "N/A"
                }
            }
        }
    }

    companion object {
        const val USER_ID_KEY = "user_id"

        @JvmStatic
        fun newInstance(id: Int): ProfileFragment {
            val instance = ProfileFragment()
            val args = Bundle()
            args.putInt(USER_ID_KEY, id)
            instance.arguments = args
            return instance
        }
    }
}