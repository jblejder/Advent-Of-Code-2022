import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import java.io.File


suspend fun getData(day: Int): String {
    val file = File("./data/${day}.txt")
    if (file.exists()) {
        return file.readText()
    }
    val data = downloadData(day)
    file.createNewFile()
    file.writeText(data)
    return data
}

suspend fun downloadData(day: Int): String {
    val client = HttpClient()
    val result = client.get {
        url("https://adventofcode.com/2022/day/$day/input")
        cookie(
            "session",
            dotenv().get("session")
        )
    }
    if (result.status.value != 200) {
        throw IllegalStateException("no data yet")
    }
    return result.body()
}

typealias AoCTask = (String) -> Any

val emptyTask: AoCTask = { "" }

fun runAoC(day: Int, phase1: AoCTask = emptyTask, phase2: AoCTask = emptyTask) {
    runBlocking {
        val data = getData(day)
        val phase1Result = phase1(data)
        println("phase 1 result: $phase1Result")
        val phase2Result = phase2(data)
        println("phase 2 result: $phase2Result")
    }
}