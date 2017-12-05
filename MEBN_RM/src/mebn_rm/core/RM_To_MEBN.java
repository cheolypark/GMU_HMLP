/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.MultiMap
 */
package mebn_rm.core;
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List; 

import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MCNode;
import mebn_rm.MEBN.MNode.MDNode;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.MEBN.MTheory.OVariable;
import mebn_rm.RDB.RDB;
import mebn_rm.util.StringUtil; 

public class RM_To_MEBN {
	RDB rdb = null;

	public RM_To_MEBN(RDB r) {
		this.rdb = r;
	}

	public MTheory run() {
		MFrag f;
		String resNodeName;
		List<String> keys;
		OVariable ov;
		String prefix;
		ArrayList<OVariable> ovs;
		Object origin;
		Integer numEntity = 0;
		Integer numRV = 0;
		Integer numMFrag = 0;
		StringUtil sutil = new StringUtil();
		MTheory mTheory = null;
		String nameMTheory = this.rdb.schema;
		
		mTheory = new MTheory(nameMTheory);
		mTheory.rdb = this.rdb;
		
		ArrayList<String> entityTables = (ArrayList) this.rdb.mapTableTypesAndTables.get((Object) "EntityTable");
		
		for (String table : entityTables) {
			mTheory.entities.add(table);
			numEntity = numEntity + 1;
			List attrs = (List) this.rdb.mapTableAndAttributes.get((Object) table);
			if (attrs == null)
				continue;
			f = new MFrag(mTheory, table);
			numMFrag = numMFrag + 1;
			prefix = sutil.createAbbreviation(table);
			keys = (List) this.rdb.mapTableAndKeys.get((Object) table);
			ovs = new ArrayList();
			for (String key : keys) {
				origin = this.rdb.mapKeysOrigins.get(key);
				ov = new OVariable(f.getTableName(), key, (String) origin);
				ovs.add(ov);
				new mebn_rm.MEBN.MNode.MIsANode(f, ov);
			}
			Iterator iterator = attrs.iterator();
			while (iterator.hasNext()) {
				String attr = (String) iterator.next();
				List<String> domains = this.rdb.mapDomainVaules.get(attr);
				resNodeName = String.valueOf(prefix) + "_" + attr;
				if (domains != null) {
					MDNode md = new MDNode(f, resNodeName, ovs);
					md.setAttributeName(attr);
					f.arrayResidentNodes.add(md);
					numRV = numRV + 1;
					continue;
				}
				MCNode mc = new MCNode(f, resNodeName, ovs);
				mc.setAttributeName(attr);
				f.arrayResidentNodes.add(mc);
				numRV = numRV + 1;
			}
		}
		
		List<String> relationshipTables = (List) this.rdb.mapTableTypesAndTables.get((Object) "RelationshipTable");
		
		if (relationshipTables != null) {
			for (String table2 : relationshipTables) {
				f = new MFrag(mTheory, table2);
				numMFrag = numMFrag + 1;
				prefix = sutil.createAbbreviation(table2);
				keys = (List) this.rdb.mapTableAndKeys.get((Object) table2);
				ovs = new ArrayList<OVariable>();
				for (String key : keys) {
					origin = this.rdb.mapKeysOrigins.get(key);
					ov = new OVariable(f.name, key, (String) origin);
					ovs.add(ov);
					new mebn_rm.MEBN.MNode.MIsANode(f, ov);
				}
				List<String> attrs = (List) this.rdb.mapTableAndAttributes.get((Object) table2);
				if (attrs != null) {
					for (String attr : attrs) {
						resNodeName = String.valueOf(prefix) + "_" + attr;
						List<String> domains = this.rdb.mapDomainVaules.get(attr);
						if (domains != null) {
							MDNode md = new mebn_rm.MEBN.MNode.MDNode(f, resNodeName, ovs);
							md.setAttributeName(attr);
							f.arrayResidentNodes.add(md);
							numRV = numRV + 1;
							continue;
						}
						MCNode mc = new mebn_rm.MEBN.MNode.MCNode(f, resNodeName, ovs);
						mc.setAttributeName(attr);
						f.arrayResidentNodes.add(mc);
						numRV = numRV + 1;
					}
				}
				
				if (this.rdb.mapTableAndAttributes.get((Object) table2) != null)
					continue;
				MDNode md = new mebn_rm.MEBN.MNode.MDNode(f, table2, ovs);
				md.setAttributeName(table2);
				f.arrayResidentNodes.add(md);				
				numRV = numRV + 1;
				f.mFragType = MFrag.MFragType.REFERENCE;
			}
		}
		System.out.println(mTheory);
		System.out.println("=========================================================================================");
		System.out.println("numEntity\tnumRV\tnumMFrag");
		System.out.println(numEntity + "\t" + numRV + "\t" + numMFrag);
		System.out.println("=========================================================================================");
		return mTheory;
	}
}
