package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.entities.DangerZone;
import com.mygdx.game.entities.Ground;
import com.mygdx.game.entities.Player;


public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact cntct) {

        Fixture fa = cntct.getFixtureA();
        Fixture fb = cntct.getFixtureB();

        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null) return;

        if (isGroundContact(fa, fb)) {
            Player player = (Player) fb.getUserData();
            player.setJumping(false);
        }

        if (isDangerContact(fa, fb)) {
            Player player = (Player) fb.getUserData();
            player.setJumping(false);
            player.hit();
        }
    }

    @Override
    public void endContact(Contact cntct) {
        Fixture fa = cntct.getFixtureA();
        Fixture fb = cntct.getFixtureB();

        if (fa == null || fb == null) return;
        if (fa.getUserData() == null || fb.getUserData() == null) return;

        if (isGroundContact(fa, fb)) {
            Player player = (Player) fb.getUserData();
            player.setJumping(true);
        }

        if (isDangerContact(fa, fb)) {
            Player player = (Player) fb.getUserData();
            player.setJumping(true);
        }
    }

    private boolean isDangerContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof DangerZone && b.getUserData() instanceof Player);
    }

    private boolean isGroundContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Ground && b.getUserData() instanceof Player);
    }

    @Override
    public void preSolve(Contact cntct, Manifold mnfld) {
    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {

    }

}
