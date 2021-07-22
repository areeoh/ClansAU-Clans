package com.areeoh.clans.shops.npc;

import net.minecraft.server.v1_8_R3.*;

public class ShopZombie extends EntityZombie {

    public ShopZombie(World world) {
        super(world);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 0.6D));
        this.goalSelector.a(9, new PathfinderGoalInteract(this, net.minecraft.server.v1_8_R3.EntityHuman.class, 3.0F, 1.0F));
        this.goalSelector.a(9, new PathfinderGoalInteract(this, net.minecraft.server.v1_8_R3.EntityVillager.class, 5.0F, 0.02F));
        this.goalSelector.a(9, new PathfinderGoalRandomStroll(this, 0.6D));
        this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, net.minecraft.server.v1_8_R3.EntityHuman.class, 8.0F));
    }


    public void move(double d0, double d1, double d2) {
    }


    public void collide(Entity entity) {
    }


    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }


    public void g(double d0, double d1, double d2) {
    }


    protected String z() {
        return "";
    }


    protected String bo() {
        return "";
    }


    protected String bp() {
        return "";
    }
}