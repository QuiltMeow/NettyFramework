package ew.sr.x1c.quilt.meow.util;

import java.io.Serializable;
import lombok.Data;

@Data
public class Triple<E, F, G> implements Serializable {

    public E left;
    public F middle;
    public G right;

    public Triple(E left, F middle, G right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    @Override
    public String toString() {
        return left.toString() + " - " + middle.toString() + " - " + right.toString();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (left == null ? 0 : left.hashCode());
        result = prime * result + (middle == null ? 0 : middle.hashCode());
        result = prime * result + (right == null ? 0 : right.hashCode());
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }

        Triple other = (Triple) object;
        if (left == null) {
            if (other.left != null) {
                return false;
            }
        } else if (!left.equals(other.left)) {
            return false;
        }

        if (middle == null) {
            if (other.middle != null) {
                return false;
            }
        } else if (!middle.equals(other.middle)) {
            return false;
        }

        if (right == null) {
            if (other.right != null) {
                return false;
            }
        } else if (!right.equals(other.right)) {
            return false;
        }
        return true;
    }
}
