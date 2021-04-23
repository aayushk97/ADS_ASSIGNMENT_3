class Account{
	public int pseudoNym;
	public float currentBalance;
	public PublicKey pubkey;

	public Account(int pseudonym, PublicKey pk){
		this.pseudoNym = pseudonyms;
		this.pubkey = pk;
		this.currentBalance = 0.0;
	}

	public boolean debitAccount(float bal){
		temp = currentBalance - bal;
		if(temp >= 0){
			currentBalance = temp;
			return true;
		}else{
			return false;
		}
	}

	public void creditAccount(float amt){
		currentBalance += amt;
	}
}