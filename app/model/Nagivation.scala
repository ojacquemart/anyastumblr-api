package model

sealed trait NavigationOrder {}

object NavigationOrder {

  case object Descending extends NavigationOrder
  case object Ascending extends NavigationOrder
}
