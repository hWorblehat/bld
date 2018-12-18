package org.bld.api

import org.bld.util.mapping._

import scala.concurrent.Future

trait Transform[+T] extends (() => T) {

}
