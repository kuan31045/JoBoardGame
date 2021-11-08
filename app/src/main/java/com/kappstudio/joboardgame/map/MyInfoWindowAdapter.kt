package com.kappstudio.joboardgame.map

import com.google.android.gms.maps.GoogleMap

class MyInfoWindowAdapter() : GoogleMap.InfoWindowAdapter {

    //指定自定義資訊視窗，顯示佈局的樣式
    var mWindow: View = (context as Activity).layoutInflater.inflate(R.layout.info_window, null)

    private fun render(marker: Marker, view: View) {

        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvAdultAmount = view.findViewById<TextView>(R.id.tv_adult_amount)
        val tvChildAmount = view.findViewById<TextView>(R.id.tv_child_amount)

        //透過 marker.snippet 傳遞口罩數量，將資料拆解後，指定到對應的 UI 欄位上顯示
        val mask = marker.snippet.toString().split(",")

        //藥局名稱
        tvName.text = marker.title

        //成人口罩數量
        tvAdultAmount.text = mask[0]

        //小孩口罩數量
        tvChildAmount.text = mask[1]
    }

    override fun getInfoContents(marker: Marker): View {
        render(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
}