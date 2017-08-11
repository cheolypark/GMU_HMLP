/*
 * Decompiled with CFR 0_118.
 */
package mebn_rm.MEBN.MTheory;

public class OVariable {
    public String name;
    public String entityType;
    public String originMFrag;
    public String originKey;

    public OVariable(String f, String k, String e) { 
        this.originMFrag = f;
        this.originKey = k;
        this.entityType = e.toUpperCase();
        this.name = String.valueOf(f) + "_" + k;
    }

    public String toString() {
        return String.valueOf(this.name) + ", " + this.entityType;
    }
}

