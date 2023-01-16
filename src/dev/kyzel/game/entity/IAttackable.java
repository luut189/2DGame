package dev.kyzel.game.entity;

public interface IAttackable {

    void inflictDamage(Entity target);
    
    void setAttackValue(int value);
    int getAttackValue();

    void setHealthValue(int value);
    int getHealthValue();

    void setMaxHealthValue(int value);
    int getMaxHealthValue();

}
