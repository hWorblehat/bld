package org.bld.api

import org.scalatest.FreeSpec

import scala.concurrent._
import duration._

class TargetsSpec extends FreeSpec {

    "A single target can be run" in {
        val targets = Targets()
            .create("hi"){"Hello"}

        val str = Await.result(targets.build("hi"), Duration.Inf)
        assert(str == "Hello")
    }
    
}