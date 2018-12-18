package org.bld.util

import mapping._

package poly {

    trait Lookup[T[_], +M <: Mapping] {

        def get[V, K <: String : MapsTo[V]#In[M]#K](key: Key[K]): T[V]

	    def apply[V, K <: String : MapsTo[V]#In[M]#K](key: Key[K]): T[V]

    }

    /**
     * A map with polymorphic values.
     */
    final class Map[T[_], +M <: Mapping] private (private val underlying: scala.collection.immutable.Map[String, Any])
    extends poly.Lookup[T, M] {

	    override def get[V, K <: String : MapsTo[V]#In[M]#K](key: Key[K]): T[V] =
		    underlying.get(key.value).get.asInstanceOf[T[V]]

	    override def apply[V, K <: String : MapsTo[V]#In[M]#K](key: Key[K]): T[V] = get(key)

	    def append[K <: String, V](key: Key[K], value: T[V]): Map[T, M + (K -> V)] =
		    new Map(underlying.updated(key.value, value))

	    def %[S <:Mapping :Has[M]#AsSubset](other: Map[T, S]): this.type = other.asInstanceOf[this.type]

    }

    object Map {

	    final type Id[T] = T

	    def withValueTransform[T[_]]: Map[T, *] = new Map(scala.collection.immutable.Map())
	    def create: Map[Id, *] = withValueTransform[Id]

    }

}