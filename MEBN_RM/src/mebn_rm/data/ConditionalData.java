/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.data;

import java.io.Serializable;
import java.util.ArrayList;

public class ConditionalData
implements Serializable {
    private static final long serialVersionUID = 2088114453116034965L;
    public ArrayList<String> arrayParent = new ArrayList();
    public String Y;

    public ConditionalData() {
    }

    public /* varargs */ ConditionalData(String ... rvs) {
        boolean b = true;
        String[] arrstring = rvs;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String rv = arrstring[n2];
            if (b) {
                this.Y = rv;
                b = false;
            } else {
                this.arrayParent.add(rv);
            }
            ++n2;
        }
    }

    public void setY(String y) {
        this.Y = y;
    }

    public Integer getParentSize() {
        return this.arrayParent.size();
    }

    public String toString() {
        return String.valueOf(this.Y) + "|" + this.arrayParent.toString();
    }

    public boolean hasEmptyParents() {
        if (this.arrayParent.size() > 0) {
            return false;
        }
        return true;
    }

    public boolean equal(ConditionalData cd) {
        if (!this.Y.equalsIgnoreCase(cd.Y)) {
            return false;
        }
        if (this.arrayParent.size() != cd.arrayParent.size()) {
            return false;
        }
        int count = 0;
        for (String p1 : this.arrayParent) {
            for (String p2 : cd.arrayParent) {
                if (!p1.equalsIgnoreCase(p2)) continue;
                ++count;
            }
        }
        if (this.arrayParent.size() != count) {
            return false;
        }
        return true;
    }
}

