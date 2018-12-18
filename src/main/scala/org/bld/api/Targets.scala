package org.bld.api

import org.bld.util.poly.Map
import org.bld.util.mapping._

import scala.concurrent._

import impl._

final class Targets[M <: Mapping] private (private val targets: Map[Target, M]) {
	private val ec: ExecutionContext = ExecutionContext.global

	def create[K <: String](name: Key[K]): TargetBuilder[K] = new TargetBuilder(name)

	def build[O, K <: String : MapsTo[O]#In[M]#K](target: Key[K]): Future[O] = targets.get(target).start(ec)

	final class TargetBuilder[K <: String] private[Targets] (private val name: Key[K]) {
		def apply[O](config: (Configurer[*]) => () => O): Targets[M + (K->O)] =
			new Targets(targets.append(name, Targets.mkTarget(config)))

		def apply[O](value: => O): Targets[M + (K->O)] = apply(_ => () => value)
	}

}

object Targets {

	def apply(): Targets[*] = new Targets(Map.withValueTransform[Target])

	private def mkTarget[O](config: Configurer[*] => () => O): Target[O] = new Target(config)

}
