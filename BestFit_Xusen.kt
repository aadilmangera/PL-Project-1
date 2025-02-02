import kotlin.random.Random

/**
 * Best-fit online algorithm for bin packing
 */

fun main(){
    val binList = mutableListOf<MutableList<Int>>()
    val binCapacity = 20
    val itemList:List<Int> = genRandomInts(20000,1..binCapacity)
    val runtime = Runtime.getRuntime()

    println("Before task:")
    printMemoryInfo(runtime)

    val startTime=System.currentTimeMillis()

    bestFit(binList,itemList,binCapacity)

    val endTime=System.currentTimeMillis()

    println("\nAfter task:")
    printMemoryInfo(runtime)

    println()
    binList.forEach{
        print("(")
        it.forEach{ v -> print("$v ") }
        print(") ")
    }
    println("\nNumber of bins used: "+binList.size)
    println("Total time of cost: "+ (endTime - startTime) + "ms")
}

/*
Function logic:
1) Add the first item to a new bin directly;
2) From the second item to the last one:
   a. Search the list of bins to find the one whose load is maximum and the remaining capacity is enough for the current item;
   b. If a such bin exists, put the current item into it; otherwise, put the current item into a new bin;
   C. Repeat step a and b until there is no item left.
 */
fun bestFit(binList:MutableList<MutableList<Int>>, itemList: List<Int>, binCapacity:Int){
    itemList.forEach {
        if (it > binCapacity){
            println("Error! The size of item, $it, is greater then bin's capacity: $binCapacity")
            return
        }
        if(binList.isEmpty()){
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

fun genRandomInts(size: Int, range: IntRange): List<Int> {
    return List(size) { Random.nextInt(range.first, range.last + 1) }
}

fun printMemoryInfo(runtime: Runtime) {
    val total = runtime.totalMemory() / 1024 / 1024
    val free = runtime.freeMemory() / 1024 / 1024
    val used = total - free

    println("Total Memory (MB): $total")
    println("Free Memory (MB): $free")
    println("Used Memory (MB): $used")
}