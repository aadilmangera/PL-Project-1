import java.io.FileOutputStream
import kotlin.math.ceil
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue


fun main(){
    val BIN_CAPACITY = 10
    val ITEM_COUNT: List<Int> = listOf(100,1000,10000)
    val ITEM_SIZE_MIN = 1
    val ITEM_SIZE_MAX = 10
    val ITERATIONS = 10
    // Common Sense checks
    require(BIN_CAPACITY >= 1){"BIN_CAPACITY must be >= 1"}
    require(ITEM_SIZE_MAX <= BIN_CAPACITY){"Max item size ($ITEM_SIZE_MAX) should be smaller than or equal to Bin Capacity $BIN_CAPACITY"}
    require(ITEM_SIZE_MIN >= 0) { "Min Item size should be greater than or equal to 0" }
    require(ITEM_SIZE_MAX >= ITEM_SIZE_MIN){"Max Item size should be equal or greater than min"}

    println("Bin Capacity: $BIN_CAPACITY")
    println("Item Sizes: $ITEM_SIZE_MIN-$ITEM_SIZE_MAX")
    // Go through item count and run iterations, then flatten out the nested lists
    val itemCountTestRunReport = ITEM_COUNT.map{ itemCount ->
        println("Item Count: $itemCount")
        (1..ITERATIONS).map { currentIteration ->
            println("Iteration: $currentIteration")
            val itemList = generateRandomItemUniform(ITEM_SIZE_MIN, ITEM_SIZE_MAX, itemCount)
            val itemListTotalWeight = itemList.sum()
            val minBinNeeded = ceil(itemListTotalWeight.toDouble() / BIN_CAPACITY).toInt()
            println("Items Total Weight: $itemListTotalWeight, Minimum Bins Needed: $minBinNeeded")
            listOf(
                Pair("Descending First Fit", ::descendingFirstFitAlgorithm),
                Pair("Best Fit", ::bestFitAlgorithm)
            ).map{pair ->
                val type = pair.first
                val algorithm = pair.second
                val timedRun = measureTimedValue {algorithm(BIN_CAPACITY, itemList)}
                val result = timedRun.value
                val binsUsed = result.size
                val fillRate = itemListTotalWeight.toDouble() / (binsUsed * BIN_CAPACITY)
                RunReport(
                    type,
                    itemCount,
                    ITEM_SIZE_MIN,
                    ITEM_SIZE_MAX,
                    BIN_CAPACITY,
                    timedRun.duration.toDouble(DurationUnit.MILLISECONDS),
                    binsUsed,
                    fillRate
                )
            }
        }.flatten()
    }.flatten()
    // Write out to CSV File
    FileOutputStream("RunResult.csv").apply {
        val fileWriter = bufferedWriter()
        fileWriter.write(""""Algorithm", "Item Count", "Item Size-Min", "Item Size-Max", "Bin-Capacity", "Duration-ms ", "Bins Used", "Fill Rate"""")
        fileWriter.newLine()
        itemCountTestRunReport.forEach {
            val (type, itemCount, itemSizeMin, itemSizeMax, binCapacity, duration, binsUsed, fillRate) = it
            fileWriter.write(""""$type", $itemCount, $itemSizeMin, $itemSizeMax, $binCapacity, $duration, $binsUsed, $fillRate""")
            fileWriter.newLine()
        }
        fileWriter.flush()
    }
}

data class Bin(val maxCapacity: Int, val currentCapacity: Int = maxCapacity, val contents: List<Int> = listOf()) {
    override fun toString(): String {
        return "Max Capacity: ${maxCapacity}, current Capacity: ${currentCapacity}, Contents: $contents"
    }
}

data class RunReport(
    val type: String,
    val itemCount: Int,
    val itemSizeMin: Int,
    val itemSizeMax: Int,
    val binCapacity: Int,
    val duration: Double,
    val binsUsed: Int,
    val fillRate: Double
)

fun generateRandomItemUniform(min: Int, max: Int, count: Int): List<Int>{
    return generateSequence{ Random.nextInt(min, max)}.take(count).toList()
}

fun descendingFirstFitAlgorithm(binCapacity: Int, itemList: List<Int>): List<Bin> {
    return itemList.sortedDescending()
        .fold(listOf()) {bins, currentItem ->
            val firstFitIndex = bins.indexOfFirst{bin -> bin.currentCapacity >= currentItem}
            addToBins(binCapacity, bins, currentItem, firstFitIndex, {index -> index == -1})
        }
}

fun bestFitAlgorithm(binCapacity: Int, itemList: List<Int>): List<Bin> {
    return itemList.fold(listOf()) {bins, currentItem ->
        val bestFitBinIndex = bins.indices // List out indices
            .filter{bins[it].currentCapacity >= currentItem} // Filter out boxes that cannot hold the incoming item
            .minByOrNull{bins[it].currentCapacity - currentItem} // Find the box that is best fit, return null if none available
        addToBins(binCapacity, bins, currentItem, bestFitBinIndex) { index -> index == null }
    }
}

fun addToBins(binCapacity: Int, bins: List<Bin>, currentItem: Int, addIndex: Int?, newBinCondition: (index: Int?) -> Boolean): List<Bin> {
    return if (newBinCondition(addIndex)) {
        bins + Bin(binCapacity, binCapacity-currentItem, listOf(currentItem))
    } else {
        bins.mapIndexed{ index, bin -> bin.copy(
            currentCapacity = if (index == addIndex) bin.currentCapacity - currentItem else bin.currentCapacity,
            contents = if (index == addIndex) bin.contents + currentItem else bin.contents
        )}
    }
}