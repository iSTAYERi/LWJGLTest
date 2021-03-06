package engine

import java.util.*

class Utils {

    companion object {

        @Throws(Exception::class)
        fun loadResource(fileName: String): String{
            var result = ""
            Utils::class.java.javaClass.getResourceAsStream(fileName).
                    use { `in` -> Scanner(`in`, "UTF-8").
                            use({ scanner -> result = scanner.useDelimiter("\\A").next() }) }
            return result
        }
    }

}