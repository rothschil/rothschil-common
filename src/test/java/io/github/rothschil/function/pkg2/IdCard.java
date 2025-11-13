package io.github.rothschil.function.pkg2;

public class IdCard {

    String type;
    String cardNum;

    public IdCard(String type, String cardNum) {
        this.type = type;
        this.cardNum = cardNum;
    }


    public String getType() {
        return type;
    }

    public String getCardNum() {
        return cardNum;
    }

    @Override
    public String toString() {
        return "IdCard{" +
                "type='" + type + '\'' +
                ", cardNum='" + cardNum + '\'' +
                '}';
    }
}
