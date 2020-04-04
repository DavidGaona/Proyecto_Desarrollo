package model;

public class Bank {

    private String bankNIT;
    private String bankName;
    private String accountNumber;
    private boolean state;

    public boolean isNotBlank() {
        if (bankName == null || accountNumber == null)
            return false;
        else
            return !bankName.isBlank() && !accountNumber.isBlank();
    }

    public Bank()
    {

    }

    public Bank(String bankName, String accountNumber, boolean state, String bankNIT) {
        this.bankNIT = bankNIT;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.state = state;
    }

    public String getNIT() {
        return bankNIT;
    }

    public void setNIT(String bankNIT) {
        this.bankNIT = bankNIT;
    }

    public String getName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setaccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bank_id=" + bankNIT +
                ", bank_name='" + bankName + '\'' +
                ", account_number='" + accountNumber + '\'' +
                ", state=" + state +
                '}';
    }
}
