package com.projects.pasBal.data.db.response

import com.projects.pasBal.data.db.entity.Slider

data class SliderResponse(
    val respon: String?,
    val message: String?,
    val slider: List<Slider>
)