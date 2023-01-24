package dev.kyzel.game.entity;

/**
 * The interface for entity that is able to attack.
 */
public interface IAttackable {

    /**
     * Inflicts damage on the given target.
     * 
     * @param target the given target
     */
    void inflictDamage(Entity target);
    
    /**
     * Set the attack value of the entity to the given value.
     * 
     * @param value the given value
     */
    void setAttackValue(int value);

    /**
     * Gets the attack value of the entity.
     * 
     * @return the attack value of the entity
     */
    int getAttackValue();

    /**
     * Set the current health value of the entity to the given value.
     * 
     * @param value the given value
     */
    void setHealthValue(int value);

    /**
     * Gets the current health value of the entity.
     * 
     * @return the current health value of the entity
     */
    int getHealthValue();

    /**
     * Set the max health value of the entity to the given value.
     * 
     * @param value the given value
     */
    void setMaxHealthValue(int value);
    
    /**
     * Gets the max health value of the entity.
     * 
     * @return the max health value of the entity
     */
    int getMaxHealthValue();

}
