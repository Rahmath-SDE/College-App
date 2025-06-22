package com.example.mcet

import java.time.LocalDate
import java.time.YearMonth

object CalendarUtils {
    fun generateDatesForMonth(year: Int, month: Int): List<LocalDate?> {
        val yearMonth = YearMonth.of(year, month)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstDayOfMonth = LocalDate.of(year, month, 1)
        val startDay = firstDayOfMonth.dayOfWeek.value % 7

        val totalCells = daysInMonth + startDay
        return List(totalCells) {
            if (it < startDay) null else LocalDate.of(year, month, it - startDay + 1)
        }
    }
}
