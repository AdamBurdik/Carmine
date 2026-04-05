package xyz.carmine.raven.gamemode.siege.phase;

public interface Phase {
    void onStart();
    void onTick();
    void onEnd();
}
