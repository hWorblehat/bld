package org.bld.util
package poly

import mapping._

import org.junit.runner.RunWith
import org.scalatest.FreeSpec
import org.scalatest.junit.JUnitRunner

import shapeless.test.illTyped

@RunWith(classOf[JUnitRunner])
class MapSpec extends FreeSpec {

	s"A polymorphic Map" - {

        "mapping any value type" - {

            "can be created" in {
                Map.create
            }

            "can have any value type appended" in {
                Map.create
                    .append("int", 3)
                    .append("double", 3.14)
                    .append("Option", Some("Hi"))
            }

            "returns the correctly typed value for a key" in {
                val map = Map.create
                    .append("int", 3)
                    .append("double", 3.14)
                    .append("Option", Some("Hi"))

                val d = map("double")
                val o: Option[String] = map("Option")

                assert(d === 3.14)
                assert(o.exists(_ === "Hi"))
            }

            "can be assigned to a variable typed to an empty map" in {
                import org.bld.util.mapping._

                val map = Map.create
                    .append("int", 3)
                    .append("double", 3.14)

                var map2: Map[Map.Id, *] = Map.create

                map2 %= map // Test successful if this line completes
            }

            "can be assigned to a variable typed to a sub-mapping" in {
                var map = Map.create
                    .append("int", 3)
                    .append("double", 3.14)

                val map2 = Map.create
                    .append("double", 2.1)
                    .append("int", 9)
                    .append("Option", Some("Hi"))

                map %= map2

                val i: Int = map("int")
                assert(i === 9)
                assert(map("double") === 2.1)
            }

            "won't compile when" - {
                
                "accessing a nonexistent value" in {
                    val map = Map.create.append("bool", true)

                    illTyped {"""map.get("int")"""}
                }

                "assigning to a variable not typed to a strict subset" in {
                    var map = Map.create
                        .append("int", 3)
                        .append("double", 3.14)

                    val map2 = Map.create
                        .append("int", 4)
                        .append("Option", Some("Hi"))

                   illTyped{"""map %= map2"""}
                }

            }

        }

        "mapping type construcor values" - {

            "can be created" in {
                Map.withValueTransform[Option]
            }

            "can have values of the correct type appended" in {
                Map.withValueTransform[Seq]
                    .append("names", Seq("Robert", "David", "Esmerelda"))
                    .append("values", List(3, 54, 897, 1))
            }

            "returns the correctly typed value for a key" in {
                val map = Map.withValueTransform[Seq]
                    .append("names", Seq("Robert", "David", "Esmerelda"))
                    .append("values", List(3, 54, 897, 1))

                val vals: Seq[Int] = map("values") // 'List' subtype is erased
                assert(vals === List(3, 54, 897, 1))
            }

        }

    }

}