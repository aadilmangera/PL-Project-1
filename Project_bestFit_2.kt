fun bestFitFunctional(binSize: Int, items: List<Int>): List<List<Int>> {
    return items.fold(emptyList<List<Int>>() to emptyList<Int>()) { (bins, binRemaining), item ->
        if (item > binSize) {
            println("Item $item is larger than bin capacity $binSize.")
            return@fold bins to binRemaining
        }

        val bestIndex = binRemaining.indices // find the best-fit bin index
            .filter { binRemaining[it] >= item }
            .minByOrNull { binRemaining[it] - item }

        if (bestIndex == null) { // no suitable bin, create a new bin
            val newBins = bins + listOf(listOf(item))
            val newBinRemaining = binRemaining + (binSize - item)
            newBins to newBinRemaining
        } else { // place the item in the best fit bin
            val newBins = bins.mapIndexed { index, bin ->
                if (index == bestIndex) bin + item else bin
            }
            val newBinRemaining = binRemaining.mapIndexed { index, space ->
                if (index == bestIndex) space - item else space
            }
            newBins to newBinRemaining
        }
    }.first // extract the list of bins from the pair
}

fun main() {
    val binSize = 20
    val items = List(10000) { kotlin.random.Random.nextInt(1, binSize + 1) }

    val startTime = System.currentTimeMillis()
    val result = bestFitFunctional(binSize, items)
    val endTime = System.currentTimeMillis()

    println("Number of bins used: ${result.size}")
    println("Execution time: ${endTime - startTime} ms")
}