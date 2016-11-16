import org.specs2.mutable.Specification

object JavaConversionsSpec extends Specification {
  "JavaConversions" should {
    "allows code to compile but produce runtime exceptions" in {
      // import this so we can work with a java collection
      import scala.collection.JavaConversions._

      val javaRetval = someJavaMethod
      javaRetval.map(_.toString) must_== Seq("1","2","3")

      // As a side effect, someone can now write this code that will only explode when executed @ runtime
      def aRarelyCalledMethod(): Unit = {
        val m = Map(1 -> "one")
        m.add(2 -> "two") // a - Any of the three of these throw an exception
        m.put(3, "three") // b
        m.clear           // c
      }

      // Meanwhile, a week into the release...things explode
      aRarelyCalledMethod() must throwA[java.lang.UnsupportedOperationException]
    }

    "masks type safety" in {
      val m = Map(1 -> "one")
//    m.contains("") wont compile

      import scala.collection.JavaConversions._
      m.contains("") must_== false
    }

    "could be used in a more limited sense" in {
      // import this so we can work with a java collection
      def getResult = {
        import scala.collection.JavaConversions.asScalaBuffer

        val javaRetval = someJavaMethod
        javaRetval.map(_.toString)
      }

      getResult must_== Seq("1","2","3")

      def aRarelyCalledMethod(): Unit = {
        val m = Map(1 -> "one")
//      m.add(2 -> "two") must throwA[java.lang.UnsupportedOperationException] wont compile
//      m.put(3, "three") must throwA[java.lang.UnsupportedOperationException] wont compile
//      m.clear           must throwA[java.lang.UnsupportedOperationException] wont compile
      }

      // Meanwhile, a week into the release...we're safe
      aRarelyCalledMethod() must not(throwA[Exception])
    }
  }

  def someJavaMethod: java.util.ArrayList[Int] = {
    val javaList = new java.util.ArrayList[Int]()
    (1 to 3).foreach(javaList.add)
    javaList
  }
}

