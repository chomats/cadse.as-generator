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

import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;

/**
 * Un aggregator des donnée recu des sous token.
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
public class GAggregator {

	protected GResult _result;

	protected GenState _state;
	
	public void beginAll(Item currentItem, GGenFile gf, GToken kind,
			GenContext context, GGenerator gGenerator) {
	}

	public void endAll(Item currentItem, GGenFile gf, GToken kind,
			GenContext context, GGenerator gGenerator) {
	}

	public void append(GResult result) {
		_result.append(result);
	}

	public GResult setResult(GGenerator gGenerator, Item currentItem,
			GGenFile gf, GToken kind, GenContext context) {
		_result = new GResult(gGenerator, currentItem, gf, kind, context);
		return _result;
	}
	
	public GResult getResult() {
		return _result;
	}

	public void append(GResult result, GToken cToken) {
		_result.append(result);
	}

	public void setState(GenState state) {
		_state = state;
	}
}
