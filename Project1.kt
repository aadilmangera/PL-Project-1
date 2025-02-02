import kotlin.math.ceil
import kotlin.random.Random
import kotlin.time.measureTimedValue


fun main(){
    val BIN_CAPACITY = 10
    val ITEM_COUNT = listOf(100,1000,10000,100000)
    val ITEM_SIZE_MIN = 3
    val ITEM_SIZE_MAX = 10
    val ITERATIONS = 5
    require(ITEM_SIZE_MAX <= BIN_CAPACITY){"Largest Item size greater than bin capacity"}
    println("Bin Capacity: $BIN_CAPACITY")
    println("Item Sizes: $ITEM_SIZE_MIN-$ITEM_SIZE_MAX")
    ITEM_COUNT.forEach{ itemCount ->
        println("Item Count: $itemCount")
        for(i in 0 until ITERATIONS){
            println("Iteration: ${i + 1}")
            val itemList = generateSequence{ Random.nextInt(ITEM_SIZE_MIN, ITEM_SIZE_MAX)}.take(itemCount).toList()
            val itemListTotalWeight = itemList.sum()
            val minBinNeeded = ceil(itemListTotalWeight.toDouble() / BIN_CAPACITY).toInt()
            println("Items Total Weight: $itemListTotalWeight, Minimum Bins Needed: $minBinNeeded")
            val timedDescendingFirstFit = measureTimedValue { descendingFirstFit(BIN_CAPACITY, itemList) }
            val descendingFirstFitBinsResult = timedDescendingFirstFit.value
            val descendingFirstFitBinsUsed = descendingFirstFitBinsResult.size
            val descendingFirstFitFillRate = itemListTotalWeight.toDouble() / (descendingFirstFitBinsUsed * BIN_CAPACITY).toDouble()
            println("Descending First Fit: time - ${timedDescendingFirstFit.duration}, bins used: ${descendingFirstFitBinsUsed}, fill rate: $descendingFirstFitFillRate%")
            val timedBestFit = measureTimedValue { bestFit(BIN_CAPACITY, itemList) }
            val bestFitBinsResult = timedBestFit.value
            val bestFitBinsUsed = bestFitBinsResult.size
            val bestFitFillRate = itemListTotalWeight.toDouble() / (bestFitBinsUsed * BIN_CAPACITY).toDouble()
            println("Best Fit: time - ${timedBestFit.duration}, bins used: ${bestFitBinsUsed}, fill rate: $bestFitFillRate%")
        }
    }
}


data class Bin(val maxCapacity: Int, val currentCapacity: Int = maxCapacity, val contents: List<Int> = listOf()) {
    override fun toString(): String {
        return "Max Capacity: ${maxCapacity}, current Capacity: ${currentCapacity}, Contents: $contents"
    }
}

fun descendingFirstFit(binCapacity: Int, itemList: List<Int>): List<Bin> {
    val sortedItemList = itemList.sortedDescending()
    return sortedItemList.fold(listOf(Bin(binCapacity)), ::firstFit)
}

fun firstFit(bins: List<Bin>, currentItem: Int): List<Bin> {
    val firstFitIndex = bins.indexOfFirst { bin -> bin.currentCapacity >= currentItem }
//    println("Current Item Size: $currentItem, bins = $bins")
    return if (firstFitIndex == -1) {
        bins + Bin(bins[0].maxCapacity, bins[0].maxCapacity - currentItem, listOf(currentItem))
    } else {
        bins.mapIndexed{ index, bin -> bin.copy(
            currentCapacity = if (index == firstFitIndex) bin.currentCapacity - currentItem else bin.currentCapacity,
            contents = if (index == firstFitIndex) bin.contents + currentItem else bin.contents
        )}
    }
}

fun bestFit(binCapacity: Int, itemList: List<Int>): List<Bin> {
    return itemList.fold(listOf(Bin(binCapacity))) {bins, currentItem ->
        val bestFitBinIndex = bins.indices // List out indices
            .filter{bins[it].currentCapacity >= currentItem} // Filter out boxes that cannot hold the incoming item
            .minByOrNull{bins[it].currentCapacity - currentItem} // Find the box that is best fit
        if (bestFitBinIndex == null) {
            bins + Bin(bins[0].maxCapacity, bins[0].maxCapacity - currentItem, listOf(currentItem))
        } else {
            bins.mapIndexed{ index, bin -> bin.copy(
                currentCapacity = if (index == bestFitBinIndex) bin.currentCapacity - currentItem else bin.currentCapacity,
                contents = if (index == bestFitBinIndex) bin.contents + currentItem else bin.contents
            )}
        }
    }
}