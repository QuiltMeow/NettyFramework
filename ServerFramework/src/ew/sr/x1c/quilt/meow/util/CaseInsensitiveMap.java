package ew.sr.x1c.quilt.meow.util;

import gnu.trove.map.hash.TCustomHashMap;
import java.util.Map;

public class CaseInsensitiveMap<T> extends TCustomHashMap<String, T> {

    public CaseInsensitiveMap() {
        super(CaseInsensitiveHashingStrategy.INSTANCE);
    }

    public CaseInsensitiveMap(Map<? extends String, ? extends T> map) {
        super(CaseInsensitiveHashingStrategy.INSTANCE, map);
    }
}
