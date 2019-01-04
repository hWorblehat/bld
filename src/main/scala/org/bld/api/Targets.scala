package org.bld.api

import org.bld.util.poly.Map
import org.bld.util.mapping._

import scala.concurrent._

import impl._

final class Targets[M <: Mapping] private (private val targets: Map[Target, M]) {
	private val ec: ExecutionContext = ExecutionContext.global

	def create[K <: String](name: Key[K]): TargetBuilder[K] = new TargetBuilder(name)

	def target[K <: String](name: Key[K]): TargetBuilder2[K] = new TargetBuilder2(name)

	def build[O, K <: String : MapsTo[O]#In[M]#K](target: Key[K]): Future[O] = targets.get(target).start(ec)

	final class TargetBuilder[K <: String] private[Targets] (private val name: Key[K]) {
		def apply[O](config: (Configurer[*]) => () => O): Targets[M + (K->O)] =
			new Targets(targets.append(name, Targets.mkTarget(config)))

		def apply[O](value: => O): Targets[M + (K->O)] = apply(_ => () => value)
	}

	final class TargetBuilder2[K <: String] private[Targets] (private val name: Key[K]) {
		def apply[T]: TargetBuilder3[K, T, *] = new TargetBuilder3(name)
	}

	final class TargetBuilder3[K <: String, T, M2 <: Mapping] private[Targets] (private val name: Key[K]) {
		def withInput[K2 <: String](key: Key[K2]): InputBuilder[K2] = new InputBuilder(key)

		final class InputBuilder[K2 <: String] private[TargetBuilder3] (private val key: Key[K2]) {
			def apply[T2]: TargetBuilder3[K, T, M2 + (K2->T2)] = new TargetBuilder3(name)
		}
	}

}

object Targets {

	def apply(): Targets[*] = new Targets(Map.withValueTransform[Target])

	private def mkTarget[O](config: Configurer[*] => () => O): Target[O] = new Target(config)

}
