package the_fireplace.cheatblocker.abstraction;

public interface IConfig {
    //General mod configuration
    String getLocale();

    //NoFall prevention
    boolean preventNofall();
}
