package entity;

public interface IAttackable {

    void inflictAttack(Entity target);
    
    void setAttackValue(int value);
    int getAttackValue();

    void setHealthValue(int value);
    int getHealthValue();

    void setMaxHealthValue(int value);
    int getMaxHealthValue();

}
