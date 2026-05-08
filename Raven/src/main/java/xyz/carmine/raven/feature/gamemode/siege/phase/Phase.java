package xyz.carmine.raven.feature.gamemode.siege.phase;

public interface Phase {
    void onStart();
    void onTick();
    void onEnd();
}
