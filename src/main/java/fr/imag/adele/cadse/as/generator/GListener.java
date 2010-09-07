/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Copyright (C) 2006-2010 Adele Team/LIG/Grenoble University, France
 */
package fr.imag.adele.cadse.as.generator;

import java.util.HashSet;

import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemState;
import fr.imag.adele.cadse.core.ItemType;
import fr.imag.adele.cadse.core.WorkspaceListener;
import fr.imag.adele.cadse.core.impl.CadseCore;
import fr.imag.adele.cadse.core.transaction.delta.ImmutableItemDelta;
import fr.imag.adele.cadse.core.transaction.delta.ImmutableWorkspaceDelta;

/**
 * TODO mettre un pattron visiteur
 * @author chomats
 *
 */
class GListener extends WorkspaceListener {
	//GAction ga = new GAction();
	RuntimeGenerator runtimeGenerator;
	
	GListener(RuntimeGenerator runtimeGenerator) {
		setKind(ListenerKind.BUILD);
		CadseCore.getLogicalWorkspace().addListener(this, 0xFFFFF);
		this.runtimeGenerator = runtimeGenerator;
	}

	
	
	@Override
	public void workspaceChanged(ImmutableWorkspaceDelta wd) {
		HashSet<Item> itemsToGenerate = new HashSet<Item>();
		
		for (ImmutableItemDelta itemDelta : wd.getItems()) {
			Item item = itemDelta.getItem();
			if (item == null || item.getState() == ItemState.DELETED)
				continue;
		
			computeToGenerate(itemsToGenerate, item);
			ItemType it = item.getType();
			
			GRefer[] refers = it.adapts(GRefer.class);
			if (refers != null) {
				for (GRefer gRefer : refers) {
					Item[] itemsToCompute = gRefer.refers(item, itemDelta);
					if (itemsToCompute == null) continue;
					for (Item itemToC : itemsToCompute) {
						computeToGenerate(itemsToGenerate, itemToC);	
					}
				}
			}
			
		}

		for (Item currentItem : itemsToGenerate) {
			runtimeGenerator.generate(currentItem);
		}
	}

	private void addToGenerate(HashSet<Item> toRegenerate, Item item) {
		if (item != null && item.getState() != ItemState.DELETED) {
			toRegenerate.add(item);
		}
	}

	/**
	 * true find item to generate and stop cycle
	 * @param toRegenerate set of item to regenerate
	 * @param item the current item
	 * @return true if stop analyse
	 */
	void computeToGenerate(HashSet<Item> toRegenerate, Item item) {
		if (item == null) {
			return;
		}
		if (toRegenerate.contains(item))
			return;
		
		addToGenerate(toRegenerate, item);
		
		ItemType it = item.getType();
		
		GRefer[] refers = it.adapts(GRefer.class);
		if (refers != null) {
			for (GRefer gRefer : refers) {
				Item[] itemsToCompute = gRefer.refers(item);
				if (itemsToCompute == null) continue;
				for (Item itemToC : itemsToCompute) {
					computeToGenerate(toRegenerate, itemToC);	
				}
			}
		}
		
		while (item.getPartParent() != null) {
			item = item.getPartParent();
			computeToGenerate(toRegenerate, item);
		}
	}

}
