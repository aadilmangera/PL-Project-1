import kotlin.random.Random

/**
 * Best-fit online algorithm for bin packing
 */

fun main(){
    val binList = mutableListOf<MutableList<Int>>()
    val binCapacity = 20
    val itemList:List<Int> = generateRandomIntList(10000,1..binCapacity)
    val runtime = Runtime.getRuntime()

    println("Before task:")
    printMemoryUsage(runtime)

    val startTime=System.currentTimeMillis()

    bestFit(binList,itemList,binCapacity)

    val endTime=System.currentTimeMillis()

    println("\nAfter task:")
    printMemoryUsage(runtime)

    println()
    binList.forEach{
        print("(")
        it.forEach{ v -> print("$v ") }
        print(") ")
    }
    println("\nNumber of bins used: "+binList.size)
    println("Total time of cost: "+ (endTime - startTime) + "ms")
}

fun bestFit(binList:MutableList<MutableList<Int>>, itemList: List<Int>, binCapacity:Int){
    itemList.forEach {
        if (it > binCapacity){
            println("Error! The size of item, $it, is greater then bin's capacity: $binCapacity")
            return
        }
        if(binList.size == 0){
            binList.add(mutableListOf(it))
        }else{
            val idx=getTargetBin(binList,it,binCapacity)
            if(idx==-1){
                binList.add(mutableListOf(it))
            }
            else binList[idx].add(it)
        }
    }
}

fun getTargetBin(binList:MutableList<MutableList<Int>>, item: Int, binCapacity:Int): Int{
    var maxLoad = 0
    var idx:Int = -1
    var res:Int = -1

    binList.forEach {
        idx++
        val sum=it.sum()
        if(sum+item == binCapacity)
            return idx
        if(sum+item < binCapacity && maxLoad < sum){
            maxLoad=sum
            res=idx
        }
    }
    return res
}

fun generateRandomIntList(size: Int, range: IntRange): List<Int> {
    return List(size) { Random.nextInt(range.first, range.last + 1) }
}

fun printMemoryUsage(runtime: Runtime) {
    val totalMemory = runtime.totalMemory() / 1024 / 1024  // in MB
    val freeMemory = runtime.freeMemory() / 1024 / 1024    // in MB
    val usedMemory = totalMemory - freeMemory

    println("Total Memory (MB): $totalMemory")
    println("Free Memory (MB): $freeMemory")
    println("Used Memory (MB): $usedMemory")
}