package jzy.spark.demo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jzy.spark.demo.databinding.FragmentHomeBinding
import jzy.spark.tellu.SmurfsView

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        SmurfsView(requireContext()).attach(binding.scrollView)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        println("onDestroyView  $this")
    }

    override fun onResume() {
        super.onResume()
        println("onResume  $this")
    }
}