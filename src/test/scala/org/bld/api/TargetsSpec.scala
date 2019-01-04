package org.bld.api

import org.scalatest.FreeSpec

import scala.concurrent._
import duration._

import org.bld.util.mapping._

class TargetsSpec extends FreeSpec {

    "A single target can be run" in {
        val targets = Targets()
            .create("hi"){"Hello"}

        val str = Await.result(targets.build("hi"), Duration.Inf)
        assert(str == "Hello")
    }

    "Simple chain of targets can be run" in {
        val targets = Targets()
            .create("object")("World")
            .create("greeting"){
                _.input("object")
                    .transformWith(inputs => s"Hello ${inputs("object")}")
            }

        val res = Await.result(targets.build("greeting"), Duration.Inf)
        assert(res == "Hello World")
    }
    
}