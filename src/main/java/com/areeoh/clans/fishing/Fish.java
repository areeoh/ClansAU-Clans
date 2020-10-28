package com.areeoh.clans.fishing;

import com.areeoh.core.utility.UtilFormat;

public enum Fish {

    COD,
    SEA_ROBIN,
    CAT_FISH,
    SALMON,
    ANGLER,
    HERRING,
    CARP,
    SHARK,
    BARRACUDA,
    BARRAMUNDI,
    BREAM,
    BASS,
    SWORDFISH,
    TROUT,
    BOWFIN,
    EEL,
    FLAT_HEAD,
    STINGRAY,
    GROUPER,
    DOG_FISH,
    HAMMERHEAD_SHARK,
    PUFFER_FISH,
    PIKE,
    SHRIMP,
    TUNA,
    LOBSTER,
    CLOWNFISH;

    public String getFormattedName() {
        return UtilFormat.cleanString(name());
    }
}