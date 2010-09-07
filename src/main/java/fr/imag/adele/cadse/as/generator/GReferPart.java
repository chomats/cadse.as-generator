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

/**
 * Un item a été changé alors un parent de type <code>it</code> est changé.
 * 
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
public class GReferPart extends GRefer {
	ItemType it;
	
	/**
	 * 
	 * @param it le type parent souhaité.
	 */
	public GReferPart(ItemType it) {
		this.it = it;
	}
	
	public GReferPart(ItemType typeSource, ItemType typeParent) {
		this(typeParent);
		typeSource.addAdapter(this);
	}

	@Override
	protected Item refer(Item currentItem) {
		return currentItem.getPartParent(it);
	}
}
