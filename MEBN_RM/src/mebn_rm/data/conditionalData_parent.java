/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class conditionalData_parent
implements Serializable {
    private static final long serialVersionUID = -1808310324002374246L;
    private ArrayList<String> arrayParent = new ArrayList();

    public void add(ArrayList<String> s) {
        this.arrayParent.addAll(s);
    }

    public ArrayList<String> getArray() {
        return this.arrayParent;
    }

    public String toString() {
        return this.arrayParent.toString();
    }

    public Boolean equal(conditionalData_parent cdp) {
        int i = 0;
        while (i < this.arrayParent.size()) {
            if (!this.arrayParent.get(i).equalsIgnoreCase(cdp.arrayParent.get(i))) {
                return false;
            }
            ++i;
        }
        return true;
    }
}

