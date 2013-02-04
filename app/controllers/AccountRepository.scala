package controllers

case class Account(id: String, bankName: String, owner: String, amount: Int)

object AccountRepository extends App {

  val banks: Map[String, Set[Account]] =
    Map(
      "GNB" -> Set(Account("CC1", "GNB", "Robin", 5000), Account("CC2", "GNB", "Barney", 9015000)),
      "La Boob Postale" -> Set(Account("CP1", "La Boob Postale", "Robin", 2000)),
      "Tortue Genaiale" -> Set(Account("TGN1", "Tortue Genaiale", "Ted", 150)),
      "Credit Rural" -> Set(Account("CRL1", "Credit Rural", "Lily & Marshall", 50000))
    )

  def findAccountsByOwner(owner: String): List[(String, Int)] = {
    //banks.values.flatMap( _ => _.filter( bank=> bank.exists( account=>account==owner ) ) )

    //for( bank<-banks.keys; accounts<-banks.get(bank); account<-accounts if( accounts.exists( account=>account==owner ) ) )
    //  yield( bank, account.amount)

    banks.values.toList.flatMap {
      case (accounts) => findAccountsByOwnerInThisBank(owner, accounts.toList)
    }
  }

  private def findAccountsByOwnerInThisBank(owner: String, accounts: List[Account]): List[(String, Int)] = {
    for (account <- accounts if (account.owner == owner))
    yield (account.bankName, account.amount)
  }

  AccountRepository.findAccountsByOwner ("Robin") foreach println

}

