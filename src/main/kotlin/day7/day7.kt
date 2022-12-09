package day7

import AoCTask
import runAoC


sealed class Item {
    abstract val path: String
    abstract val size: Int

    class File(
        override val path: String,
        override val size: Int
    ) : Item()

    class Directory(
        override val path: String,
        val parent: Directory? = null,
        var files: MutableMap<String, Item> = mutableMapOf()
    ) : Item() {
        override val size: Int
            get() = files.values.sumOf { it.size }
    }
}

val day7p1: AoCTask = { input: String ->
    val root = Item.Directory("/")
    var pwd: Item.Directory = root

    val iterator = input.drop(1).lines().filter { it.isNotEmpty() }.listIterator()
    while (iterator.hasNext()) {
        val line = iterator.next()
        when {
            line.startsWith("$") -> {
                when {
                    line.contains("cd") -> {
                        val name = line.substringAfterLast("$ cd ")
                        pwd = if (name == "..") {
                            pwd.parent!!
                        } else {
                            pwd.files[name.trim()] as Item.Directory
                        }
                    }
                    line.contains("ls") -> {
                        while (iterator.hasNext()) {
                            val item = iterator.next()
                            when {
                                item.startsWith("$") -> break
                                item.startsWith("dir") -> {
                                    val dirName = item.substringAfterLast("dir ")
                                    pwd.files[dirName] = Item.Directory(dirName, pwd)
                                }
                                else -> {
                                    val content = item.split(" ")
                                    pwd.files[content[1]] = Item.File(content[1], content[0].toInt())
                                }
                            }
                        }
                        iterator.previous()
                    }
                }
            }
        }
    }

    root.files.values
        .mapNotNull { it as? Item.Directory }
        .sumOf { doDeeper(0, it) }
}

fun doDeeper(acc: Int, dir: Item.Directory): Int {
    val size = dir.size
    var total = if (size <= 100000) {
        size
    } else {
        0
    }
    dir.files.values
        .mapNotNull { it as? Item.Directory }
        .forEach {
            total += doDeeper(0, it)
        }
    return total + acc
}

val day7p2: AoCTask = { input ->
    val root = Item.Directory("/")
    var pwd: Item.Directory = root

    val iterator = input.drop(1).lines().filter { it.isNotEmpty() }.listIterator()
    while (iterator.hasNext()) {
        val line = iterator.next()
        when {
            line.startsWith("$") -> {
                when {
                    line.contains("cd") -> {
                        val name = line.substringAfterLast("$ cd ")
                        pwd = if (name == "..") {
                            pwd.parent!!
                        } else {
                            pwd.files[name.trim()] as Item.Directory
                        }
                    }
                    line.contains("ls") -> {
                        while (iterator.hasNext()) {
                            val item = iterator.next()
                            when {
                                item.startsWith("$") -> break
                                item.startsWith("dir") -> {
                                    val dirName = item.substringAfterLast("dir ")
                                    pwd.files[dirName] = Item.Directory(dirName, pwd)
                                }
                                else -> {
                                    val content = item.split(" ")
                                    pwd.files[content[1]] = Item.File(content[1], content[0].toInt())
                                }
                            }
                        }
                        iterator.previous()
                    }
                }
            }
        }
    }

    val totalDiskSize: Int = 70000000
    val updateSize: Int = 30000000
    val rootSize: Int = root.size
    val spareToFreeUp = updateSize - (totalDiskSize - rootSize)
    println("neet this much space $spareToFreeUp")

    fun directories(acc: List<Item.Directory>, dir: Item.Directory): List<Item.Directory> {
        val childDirs: List<Item.Directory> = dir.files.values
            .mapNotNull { it as? Item.Directory }
            .flatMap { directories(acc, it) }
        return childDirs + dir
    }

    directories(emptyList(), root).filter { it.size > spareToFreeUp }.minOf { it.size }
}

fun main() = runAoC(7, day7p1, day7p2)
