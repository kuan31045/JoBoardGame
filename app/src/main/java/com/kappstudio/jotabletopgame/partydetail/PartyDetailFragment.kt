package com.kappstudio.jotabletopgame.partydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.databinding.FragmentHomeBinding
import com.kappstudio.jotabletopgame.databinding.FragmentPartyDetailBinding
import com.kappstudio.jotabletopgame.game.GameAdapter
import com.kappstudio.jotabletopgame.game.GameNormalAdapter

class PartyDetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPartyDetailBinding.inflate(inflater)
        var games: MutableList<Game> = mutableListOf()
        repeat(13){
            games.add(
                Game(
                    name = "卡坦島$it"
                )
            )
        }


        binding.rvGame.adapter = GameNormalAdapter().apply{submitList(
            games
        )}

        return binding.root
    }


}