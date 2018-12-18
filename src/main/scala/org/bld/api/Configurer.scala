package org.bld.api

import org.bld.util.mapping._
import org.bld.util.poly._

class Configurer[+M <: Mapping] private[api] (private val inputs: Map[Property, M]) {

	def input[K <: String, T](name: Key[K]): Configurer[M + (K->T)] =
		new Configurer(inputs.append(name, mkProp[T]))

	def transformWith[O](transform: ResolvedInputs[M] => O): Transform[O] = ???

	private def mkProp[T]: Property[T] = ???

}
