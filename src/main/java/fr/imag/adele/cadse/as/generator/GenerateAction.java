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
 */
package fr.imag.adele.cadse.as.generator;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.core.runtime.Status;

import fede.workspace.tool.view.WSPlugin;
import fede.workspace.tool.view.actions.AbstractEclipseMenuAction;
import fr.imag.adele.cadse.core.CadseException;
import fr.imag.adele.cadse.core.IItemNode;
import fr.imag.adele.cadse.core.Item;

/**
 * Action pour le menu generate.
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
class GenerateAction extends AbstractEclipseMenuAction {

	List<Item> arrayOfgenerateObj;
	private RuntimeGenerator _rg;

	GenerateAction(RuntimeGenerator rg, List<Item> arrayOfgenerateObj) {
		this.arrayOfgenerateObj = arrayOfgenerateObj;
		_rg = rg;
	}
	
	
	@Override
	public String getLabel() {
		if ( arrayOfgenerateObj.size() == 1 ) {
			return "Generate "+arrayOfgenerateObj.get(0).getDisplayName();
		}
		return "Generate ...";
	}

	@Override
	public void run(IItemNode[] selection) throws CadseException {
		for (Item aItem : arrayOfgenerateObj) {
			try {
				_rg.generate(aItem);
			} catch (Throwable e) {
		    	String id = aItem.getName();
		        WSPlugin.log(new Status(Status.ERROR,"Tool.Workspace.View",0,
		        		MessageFormat.format("Cannot generate {0} : {1}.",id,e.getMessage()),e));
		    } 
		}
	}
	

}
