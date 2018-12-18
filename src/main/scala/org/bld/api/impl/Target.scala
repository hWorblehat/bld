package org.bld.api
package impl

import scala.concurrent._
import org.bld.util.mapping._

final class Target[+T] (
	private val config: Configurer[*] => () => T
) {

	def start(ec: ExecutionContext): Future[T] = {
		return configure(ec).map({_.apply})(ec)
	}

	private def configure(implicit ec: ExecutionContext): Future[() => T] = Future {
		config(null)
	}


}
