package jzy.spark.demo.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import jzy.spark.demo.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        binding.appBar.setOnApplyWindowInsetsListener { v, insets ->
//            println(insets.getInsets(WindowInsets.Type.systemBars()))
//            binding.appBar.updatePadding(top = 0)
//            insets
//        }
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