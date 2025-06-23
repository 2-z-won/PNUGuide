package com.pnu.pnuguide.data

object CourseData {
    fun loadStamps(): List<Stamp> {
        return LabelMappings.labelToTitle.map { (code, title) ->
            Stamp(id = code, name = title)
        }
    }
}
