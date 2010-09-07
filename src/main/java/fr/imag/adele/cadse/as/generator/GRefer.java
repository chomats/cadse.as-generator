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

import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemType;
import fr.imag.adele.cadse.core.transaction.delta.ImmutableItemDelta;
import fr.imag.adele.cadse.objectadapter.ObjectAdapter;

/**
 * Un type d'adapter permétant de configurer l'impact d'un changement pour le générateur.
 * Il est possible d'implementé {@link #refer(Item)} ou {@link #refers(Item)}.
 * Si le calcul dépend d'un delta ({@link ImmutableItemDelta}), il faut implémenter {@link #refers(Item, ImmutableItemDelta)}.
 * 
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
public abstract class GRefer extends ObjectAdapter<GRefer> {

	@Override
	public Class<GRefer> getClassAdapt() {
		return GRefer.class;
	}
	
	public GRefer(ItemType sourceType) {
		sourceType.addAdapter(this);
	}
	
	public GRefer() {
	}
	
	protected Item refer(Item currentItem) {
		return null;
	}
	
	public Item[] refers(Item currentItem) {
		Item r = refer(currentItem);
		if( r!= null)
			return new Item[]{r};
		return null;
	}
	
	public Item[] refers(Item currentItem, ImmutableItemDelta itemDelta){
		return refers(currentItem);
	}

}
