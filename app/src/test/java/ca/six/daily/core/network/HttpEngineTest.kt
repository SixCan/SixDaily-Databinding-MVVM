package ca.six.daily.core.network

import org.junit.Assert.*
import org.junit.Test

class HttpEngineTest {

    @Test
    fun testRequest_getFakeResponse(){
        HttpEngine.isMock = true
        HttpEngine.mockJson = "{}"
        val resp = HttpEngine.requestString(HttpEngine.http,"test1")
        assertEquals("{}", resp)
    }

    @Test
    fun testRequest_getRealData(){
        HttpEngine.isMock = false
        val resp = HttpEngine.requestString(HttpEngine.http,"version/android/2.5.0")
        println("szw resp = $resp") //=> {status:1, msg:..., url:..., latest:2.6.0}
    }



    @Test
    fun testRequest_getFakeResponse_andResponseCodeIsError(){

    }

    @Test
    fun testRequest_getFakeResponse_inObservable(){

    }

}
