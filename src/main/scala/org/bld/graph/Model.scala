package org.bld.graph

import scala.collection.Traversable
import scala.collection.mutable.HashSet
import scala.util.{Try,Success,Failure}

trait Model {

    def bind[T](toBind: Key[T], binding: Binding[T]): Unit

    def getTransform[T](key: Key[T]): Transform

}

trait Binding[T]

trait Key[T]

//TODO how to bind target key to tranform output?
// Ideas
//    - Each "key" must be "configured" before subkeys can be accessed?

trait TransformBlueprint {

    def configure(inputs: InputConf, outputs: OutputConf): Transform

}

trait Transform {

    def execute(inputs: Inputs, outputs: Outputs): Unit

}

trait Inputs
trait InputConf

trait Outputs
trait OutputConf {

    def declare[T](outKey: Key[T]): Unit

}

object ExecutionPlan {

    object END extends Transform {
        override def execute(inputs: Inputs, outputs: Outputs): Unit = ()
    }

    def buildFor(targets: Key[_]*): Try[Unit] = {
        val unboundTargets = new HashSet[Key[_]]
        unboundTargets ++= targets

        val builder: GraphBuilder[Transform, Key[_]] = new GraphBuilder
        targets.foreach(builder.setEndNode(_, END))

        var result: Try[Option[Transform]] = Success(None)

        while(result.isSuccess && !unboundTargets.isEmpty) {
            val target: unboundTargets.head

        }

        return result.map(_ => ()) //TODO
    }

}
    /*
- Targets (keys) are edges
- Transforms are nodes

- Start with END node
- Add desired targets as edges
- For each edge without a start node
  - Look up start node from binding
  - For each outgoing edge of start node
    - If already in graph, link
    - else, add
  - For each incoming edge of start node
    - If already in graph
      - If (cycle), ERROR
      - link
    - else, add
    */