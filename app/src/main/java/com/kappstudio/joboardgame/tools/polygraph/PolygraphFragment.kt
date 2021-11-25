package com.kappstudio.joboardgame.tools.polygraph

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.databinding.FragmentPolygraphBinding
 
class PolygraphFragment : Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var binding: FragmentPolygraphBinding
    val viewModel: PolygraphViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPolygraphBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setUpSensorStuff()

        viewModel.lieValue.observe(viewLifecycleOwner, {
            val color = if (it < 60) Color.GREEN else Color.RED
            binding.tvSquare.setBackgroundColor(color)
            binding.tvSquare.text = "唬爛指數: $it%"
            binding.progressView.setProgress(it,true)
            binding.progressView.setProgressColor(color)
        })

        viewModel.navTolie.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(PolygraphFragmentDirections.navToLieDialog())
                viewModel.onNavToLie()
            }
        })

        return binding.root
    }

    private fun setUpSensorStuff() {
        sensorManager =
            appInstance.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_UI,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            viewModel.calEventValue(event.values)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}