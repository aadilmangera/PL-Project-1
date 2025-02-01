import kotlin.random.Random
import kotlin.system.measureTimeMillis

const val BIN_SIZE = 10
const val ITEM_COUNT = 20
const val ITEM_SIZE_MIN = 1
const val ITEM_SIZE_MAX = 20

data class Bin(val maxCapacity: Int, val currentCapacity: Int = maxCapacity, val contents: List<Int> = listOf())
fun main() {
    println("Hello World!")
    val itemList = generateBaggingItemsUniform(ITEM_SIZE_MIN, ITEM_SIZE_MAX, ITEM_COUNT)
    val executionTime = measureTimeMillis {
        val result = onlineBagging(itemList, ::firstFit)
        println(result)
    }
    println("Execution time: $executionTime ms")
}

fun generateBaggingItemsUniform(min: Int, max: Int, count: Int): List<Int> {
    return generateSequence{ Random.nextInt(min, max)}.take(count).toList()
}

fun onlineBagging(itemList: List<Int>, algorithm: (bins: List<Bin>, currentItem: Int) -> List<Bin>): List<Bin> {
    return itemList.fold(listOf(Bin(BIN_SIZE)), algorithm)
}

fun firstFit(bins: List<Bin>, currentItem: Int): List<Bin> {
    val firstFitIndex = bins.indexOfFirst { bin -> bin.currentCapacity >= currentItem }
    println("Current Item Size: $currentItem, bins = $bins")
    return if (firstFitIndex == -1) {
        bins + Bin(bins[0].maxCapacity, bins[0].currentCapacity - currentItem, listOf(currentItem))
    } else {
        bins.mapIndexed{index, bin -> bin.copy(
            currentCapacity = if (index == firstFitIndex) bin.currentCapacity - currentItem else bin.currentCapacity,
            contents = if (index == firstFitIndex) bin.contents + currentItem else bin.contents
        )}
    }
}