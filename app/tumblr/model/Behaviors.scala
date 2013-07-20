package tumblr.model

trait Slugifiable {
  def slug(): String
}

trait Sortable {
  def ordinal(): Int
}

trait Enabled {
  def enabled(): Boolean
}
