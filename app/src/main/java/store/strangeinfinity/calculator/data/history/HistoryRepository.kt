package store.strangeinfinity.calculator.data.history

import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val dao: CalculationDao) {

    val allCalculations: Flow<List<CalculationEntity>> = dao.getAllCalculations()

    suspend fun saveCalculation(expression: String, result: String) {
        if (expression.isBlank() || result.isBlank()) return
        dao.insertCalculation(
            CalculationEntity(
                expression = expression,
                result = result,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun delete(id: Long) {
        dao.deleteById(id)
    }

    suspend fun clearHistory() {
        dao.clearAll()
    }
}
