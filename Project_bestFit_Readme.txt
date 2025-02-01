Steps & Corresponding Functions

1.Initialize bins and track remaining space
· fold(emptyList<List<Int>>() to emptyList<Int>()) { (bins, binRemaining), item -> ... }
· Uses fold to iterate over items while maintaining an immutable list of bins and remaining capacities.

2.Iterate through each item
· The fold function processes each item while keeping track of bin contents and available space.
· Handles invalid items: If an item is larger than binSize, it prints an error and skips processing.

3.Find the Best-Fit bin
· binRemaining.indices.filter { binRemaining[it] >= item }.minByOrNull { binRemaining[it] - item }
· Finds the bin with the least remaining space that can still accommodate the item.
· Returns null if no suitable bin exists.

4.Place the item in the selected bin or create a new bin
· If bestIndex == null, a new bin is created:
val newBins = bins + listOf(listOf(item))
val newBinRemaining = binRemaining + (binSize - item)

· Otherwise, the item is placed in the best bin:
val newBins = bins.mapIndexed { index, bin ->
    if (index == bestIndex) bin + item else bin
}
val newBinRemaining = binRemaining.mapIndexed { index, space ->
    if (index == bestIndex) space - item else space
}
· Uses mapIndexed to create a new immutable list instead of modifying existing bins.