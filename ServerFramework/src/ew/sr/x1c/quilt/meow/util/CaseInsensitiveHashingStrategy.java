package ew.sr.x1c.quilt.meow.util;

import gnu.trove.strategy.HashingStrategy;
import java.util.Locale;

public class CaseInsensitiveHashingStrategy implements HashingStrategy {

    public static final CaseInsensitiveHashingStrategy INSTANCE = new CaseInsensitiveHashingStrategy();

    @Override
    public int computeHashCode(Object object) {
        return ((String) object).toLowerCase(Locale.ROOT).hashCode();
    }

    @Override
    public boolean equals(Object left, Object right) {
        return left.equals(right) || (left instanceof String && right instanceof String && ((String) left).toLowerCase(Locale.ROOT).equals(((String) right).toLowerCase(Locale.ROOT)));
    }
}
