package com.pnu.pnuguide

import com.google.gson.Gson
import com.pnu.pnuguide.data.LabelMappings
import org.junit.Test
import org.junit.Assert.assertTrue
import java.io.File

class LabelMappingsTest {
    @Test
    fun allLabelsHaveMappings() {
        val labelsFile = File("src/main/assets/labels.json")
        val json = labelsFile.readText()
        val labels: Array<String> = Gson().fromJson(json, Array<String>::class.java)
        for (code in labels) {
            assertTrue("Mapping missing for $code", LabelMappings.labelToTitle.containsKey(code))
        }
    }
}
