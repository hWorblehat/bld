package org.bld.util

import shapeless.Witness

object mapping {

	type Key[K <: String] = Witness.Aux[K]

	sealed trait Mapping

	/** Represents an empty mapping */
	sealed trait * extends Mapping

	sealed trait SingleMapping
	final class ->[K <: String, +V](val key: K, val value: V) extends SingleMapping

	trait +[+H <: Mapping, +T <: SingleMapping] extends Mapping

	@annotation.implicitNotFound("Map does not contain key: ${K}")
	sealed trait EntryWitness[-T <: Mapping, K <: String] {
		type Val
	}

	object EntryWitness {

		@annotation.implicitNotFound("Map does not contain key: ${K}")
		trait Aux[-T <: Mapping, K <: String, V] extends EntryWitness[T, K] {
			override type Val = V
		}

		implicit def witnessTail[H <: Mapping, K <: String, V]: Aux[H + (K -> V), K, V] = null
		implicit def witnessHead[H <: Mapping, T <: SingleMapping, K <: String, V]
		(implicit _head: Aux[H, K, V]): Aux[H + T, K, V] = null
	}

	sealed trait MapsTo[V] {
		sealed trait In[M <: Mapping] {
			final type K[K <: String] = EntryWitness.Aux[M, K, V]
		}
	}

	//	trait Without[+T <: Mapping, E <: SingleMapping] {
	//		type Rest <: Mapping
	//	}
	//
	//	object Without {
	//		type Aux[+T <: Mapping, E <: SingleMapping, R <: Mapping] = Without[T, E] { type Rest = R }
	//
	//		private val aux: Aux[Mapping, SingleMapping, Mapping] = new Without[Mapping, SingleMapping] {
	//			override type Rest = Mapping
	//		}
	//
	//		implicit def withoutHead[K <: String, V, T <: Mapping]: Aux[K -> V + T, K -> V, T] =
	//			aux.asInstanceOf[Aux[K -> V + T, K -> V, T]]
	//
	//		implicit def withoutTail[H <: SingleMapping, T <: Mapping, E <: SingleMapping]
	//		(implicit tailWithout: Without[T, E]): Aux[H + T, E, H + tailWithout.Rest] =
	//			aux.asInstanceOf[Aux[H + T, E, H + tailWithout.Rest]]
	//	}

	/**
	  * A witness that one mapping contains all elements of another.
	  *
	  * Note - the type parameter B is marked as contravariant here. It's actually covariant,
	  * but it's used in a place above that requires a contravariant type.
	  * @tparam A The containing mapping
	  * @tparam B The contained mapping
	  */
	@annotation.implicitNotFound("Map with type [${A}] does not contain all the required mappings: ${B}")
	sealed trait Contains[-A <: Mapping, -B <: Mapping]

	object Contains {

		// Mapping 'A' contains non-empty mapping 'B' if...
		implicit def contains[H <: Mapping, // (head of B)
				A <: Mapping : Has[H]#AsSubset, // 'A' contains all members of 'B' but the last and...
				V, K <: String : MapsTo[V]#In[A]#K // 'A' contains the last element of 'B'
		]: Contains[A, H + (K->V)] = null

		// All mappings 'contain' the empty mapping
		implicit def containsNone[A <: Mapping]: Contains[A, *] = null
	}

	sealed trait Has[T <: Mapping] {
		type AsSubset[S <: Mapping] = Contains[S, T]
	}

}
