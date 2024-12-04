package net.phileasfogg3.petlife.Pets;

public class PetAttributes {

    private final int attack;
    private final int hearts;
    private final double speed;
    private final String ability;


    public PetAttributes(int attack, int hearts, double speed, String ability) {
        this.attack = attack;
        this.hearts = hearts;
        this.speed = speed;
        this.ability = ability;
    }

    public int getAttack() {return attack;}
    public int getHearts() {return hearts;}
    public double getSpeed() {return speed;}
    public String getAbility() {return ability;}

}
