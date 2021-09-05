package com.hyochan.sendbird.util

object QueryUtil {

    fun parsQuery(query: String): Pair<Set<String>, Set<String>> {
        val allowRegex = Regex("[\\w\\s]")
        val filterRegex = Regex("\\|| or |-| not ")
        val orRegex = Regex("(?:\\|| or )($allowRegex*)")
        val notRegex = Regex("(?:-| not )($allowRegex*)")

        val orQuery = mutableSetOf<String>()
        filterRegex.split(query).first().apply {
            if (isNotEmpty()) {
                orQuery.add(this)
            }
        }
        orQuery.addAll(orRegex.findQuery(query))
        val notQuery = notRegex.findQuery(query)

        return Pair(orQuery, notQuery)
    }

    private fun Regex.findQuery(query: String): Set<String> {
        val result = mutableSetOf<String>()
        this.findAll(query).forEach {
            val queryResult = it.groupValues[1].trim()
            if (queryResult.isNotEmpty()) {
                result.add(queryResult)
            }
        }
        return result
    }
}