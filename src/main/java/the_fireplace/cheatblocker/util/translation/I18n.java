package the_fireplace.cheatblocker.util.translation;

class I18n {
    private static final CheatBlockerLanguageMap localizedName = CheatBlockerLanguageMap.getInstance();
    private static final CheatBlockerLanguageMap fallbackTranslator = new CheatBlockerLanguageMap("en_us");

    static String translateToLocalFormatted(String key, Object... format) {
        return canTranslate(key) ? localizedName.translateKeyFormat(key, format) : translateToFallback(key, format);
    }

    private static String translateToFallback(String key, Object... format) {
        return fallbackTranslator.translateKeyFormat(key, format);
    }

    private static boolean canTranslate(String key) {
        return localizedName.isKeyTranslated(key);
    }
}