package com.kappstudio.joboardgame.ui.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.kappstudio.joboardgame.bindTextViewDate
import com.kappstudio.joboardgame.databinding.InfoWindowBinding
import com.kappstudio.joboardgame.ui.party.PartyViewModel

class PartyInfoWindowAdapter(
    private val context: Context,
    private val viewModel: PartyViewModel,
) : GoogleMap.InfoWindowAdapter {

    private fun render(
        marker: Marker,
        binding: InfoWindowBinding,
    ) {
        val party = viewModel.parties.value?.filter {
            it.id == marker.snippet
        }?.get(0)

        if (party != null) {
            binding.apply {

                tvTitle.text = party.title
                tvLocation.text = party.location.address
                bindTextViewDate(tvTime, party.partyTime)
                tvPeople.text = "${party.playerIdList.size}/${party.requirePlayerQty}"

                party.gameNameList.forEach {
                    tvGame.text = "${tvGame.text}${it}"

                    if (it != party.gameNameList.last()) {
                        tvGame.text = "${tvGame.text}, "

                    }
                }
            }
        }
    }

    override fun getInfoContents(marker: Marker): View {
        val layoutInflater = LayoutInflater.from(context)
        val binding = InfoWindowBinding.inflate(layoutInflater)
        render(marker, binding)
        return binding.root
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
}