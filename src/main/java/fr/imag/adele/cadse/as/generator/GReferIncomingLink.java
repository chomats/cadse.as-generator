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

import java.util.Collection;

import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemType;
import fr.imag.adele.cadse.core.LinkType;

/**
 * Toutes les sources des liens incomings de type lt sont chang�.
 * 
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
public class GReferIncomingLink extends GRefer {

	/**
	 * type du lien recherch�
	 */
	LinkType lt;

	/**
	 * 
	 * @param typeSource type source � attacher l'adapter
	 * @param lt type du lien recherch�
	 */
	public GReferIncomingLink(ItemType typeSource, LinkType lt) {
		super(typeSource);
		this.lt = lt;
	}
	
	@Override
	public Item[] refers(Item currentItem) {
		Collection<Item> items = currentItem.getIncomingItems(lt);
		return (Item[]) items.toArray(new Item[items.size()]);
	}
}
