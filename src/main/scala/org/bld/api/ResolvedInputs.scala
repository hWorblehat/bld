package org.bld.api

import org.bld.util.mapping._

trait ResolvedInputs[+M <: Mapping] {

	def get[V, K <: String : MapsTo[V]#In[M]#K](key: Key[K]): V

	final def apply[V, K <: String : MapsTo[V]#In[M]#K](key: Key[K]): V = get(key)

}
