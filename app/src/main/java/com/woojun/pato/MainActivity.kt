package com.woojun.pato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.woojun.pato.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusBarColor = ContextCompat.getColor(this, R.color.primary)
        window.statusBarColor = statusBarColor

        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.storeFragment -> {
                    binding.bottomMenu1Selected.visibility = View.VISIBLE
                    binding.bottomMenu1.visibility = View.INVISIBLE

                    binding.bottomMenu2Selected.visibility = View.INVISIBLE
                    binding.bottomMenu2.visibility = View.VISIBLE

                    binding.bottomMenu3Selected.visibility = View.INVISIBLE
                    binding.bottomMenu3.visibility = View.VISIBLE
                }
                R.id.chatFragment -> {
                    binding.bottomMenu1Selected.visibility = View.INVISIBLE
                    binding.bottomMenu1.visibility = View.VISIBLE

                    binding.bottomMenu2Selected.visibility = View.VISIBLE
                    binding.bottomMenu2.visibility = View.INVISIBLE

                    binding.bottomMenu3Selected.visibility = View.INVISIBLE
                    binding.bottomMenu3.visibility = View.VISIBLE
                }
                R.id.myInfoFragment -> {
                    binding.bottomMenu1Selected.visibility = View.INVISIBLE
                    binding.bottomMenu1.visibility = View.VISIBLE

                    binding.bottomMenu2Selected.visibility = View.INVISIBLE
                    binding.bottomMenu2.visibility = View.VISIBLE

                    binding.bottomMenu3Selected.visibility = View.VISIBLE
                    binding.bottomMenu3.visibility = View.INVISIBLE
                }
            }
        }

        binding.store.setOnClickListener {
            navController.navigate(R.id.storeFragment, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.chatFragment, false)
                    .build())

            binding.store.isEnabled = false
            binding.bottomMenu1Selected.isEnabled = false

            binding.chat.isEnabled = true
            binding.bottomMenu2Selected.isEnabled = true

            binding.myInfo.isEnabled = true
            binding.bottomMenu3Selected.isEnabled = true
        }
        binding.chat.setOnClickListener {
            navController.navigate(R.id.chatFragment, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.bottom_navigation_graph, false)
                    .build())

            binding.store.isEnabled = true
            binding.bottomMenu1Selected.isEnabled = true

            binding.chat.isEnabled = false
            binding.bottomMenu2Selected.isEnabled = false

            binding.myInfo.isEnabled = true
            binding.bottomMenu3Selected.isEnabled = true
        }
        binding.myInfo.setOnClickListener {
            navController.navigate(R.id.myInfoFragment, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.chatFragment, false)
                    .build())

            binding.store.isEnabled = true
            binding.bottomMenu1Selected.isEnabled = true

            binding.chat.isEnabled = true
            binding.bottomMenu2Selected.isEnabled = true

            binding.myInfo.isEnabled = false
            binding.bottomMenu3Selected.isEnabled = false
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}