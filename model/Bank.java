package model;

public class Bank {

    private int bank_id;
    private String bank_name;
    private String account_number;
    private boolean state;

    public Bank(int bank_id, String bank_name, String account_number, boolean state) {
        this.bank_id = bank_id;
        this.bank_name = bank_name;
        this.account_number = account_number;
        this.state = state;
    }

    public int getBank_id() {
        return bank_id;
    }

    public void setBank_id(int bank_id) {
        this.bank_id = bank_id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bank_id=" + bank_id +
                ", bank_name='" + bank_name + '\'' +
                ", account_number='" + account_number + '\'' +
                ", state=" + state +
                '}';
    }
}
