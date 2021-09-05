package com.hyochan.sendbird.util

import org.junit.Assert.*
import org.junit.Test

class QueryUtilTest {

    @Test
    fun query1() {
        val (orQuery, notQuery) = QueryUtil.parsQuery("qwe")

        assertEquals(orQuery.size, 1)
        assertEquals(notQuery.size, 0)
        assertEquals(orQuery.elementAt(0), "qwe")
    }

    @Test
    fun query2() {
        val (orQuery, notQuery) = QueryUtil.parsQuery("qwe|ewq-ew-qw")

        assertEquals(orQuery.size, 2)
        assertEquals(notQuery.size, 2)
        assertEquals(orQuery.elementAt(0), "qwe")
        assertEquals(orQuery.elementAt(1), "ewq")
        assertEquals(notQuery.elementAt(0), "ew")
        assertEquals(notQuery.elementAt(1), "qw")
    }

    @Test
    fun query3() {
        val (orQuery, notQuery) = QueryUtil.parsQuery("qwe||-|ewq-ew-qw")

        assertEquals(orQuery.size, 2)
        assertEquals(notQuery.size, 2)
        assertEquals(orQuery.elementAt(0), "qwe")
        assertEquals(orQuery.elementAt(1), "ewq")
        assertEquals(notQuery.elementAt(0), "ew")
        assertEquals(notQuery.elementAt(1), "qw")
    }

    @Test
    fun query4() {
        val (orQuery, notQuery) = QueryUtil.parsQuery("qwe||-|ewq-ew-112-qw|123")

        assertEquals(orQuery.size, 3)
        assertEquals(notQuery.size, 3)
        assertEquals(orQuery.elementAt(0), "qwe")
        assertEquals(orQuery.elementAt(1), "ewq")
        assertEquals(orQuery.elementAt(2), "123")
        assertEquals(notQuery.elementAt(0), "ew")
        assertEquals(notQuery.elementAt(1), "112")
        assertEquals(notQuery.elementAt(2), "qw")
    }

    @Test
    fun query5() {
        val (orQuery, notQuery) = QueryUtil.parsQuery("qwe-asd")

        assertEquals(orQuery.size, 1)
        assertEquals(notQuery.size, 1)
        assertEquals(orQuery.elementAt(0), "qwe")
        assertEquals(notQuery.elementAt(0), "asd")
    }

    @Test
    fun query6() {
        val (orQuery, notQuery) = QueryUtil.parsQuery("-asd")

        assertEquals(orQuery.size, 0)
        assertEquals(notQuery.size, 1)
        assertEquals(notQuery.elementAt(0), "asd")
    }

    @Test
    fun query7() {
        val (orQuery, notQuery) = QueryUtil.parsQuery("java or kotlin")

        assertEquals(orQuery.size, 2)
        assertEquals(notQuery.size, 0)
        assertEquals(orQuery.elementAt(0), "java")
        assertEquals(orQuery.elementAt(1), "kotlin")
    }

    @Test
    fun query8() {
        val (orQuery, notQuery) = QueryUtil.parsQuery("java not kotlin")

        assertEquals(orQuery.size, 1)
        assertEquals(notQuery.size, 1)
        assertEquals(orQuery.elementAt(0), "java")
        assertEquals(notQuery.elementAt(0), "kotlin")
    }
}