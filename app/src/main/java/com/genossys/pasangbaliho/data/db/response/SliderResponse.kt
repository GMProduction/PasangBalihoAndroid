package com.genossys.pasangbaliho.data.db.response

import com.genossys.pasangbaliho.data.db.entity.Slider

data class SliderResponse(
    val respon: String?,
    val message: String?,
    val slider: List<Slider>
)