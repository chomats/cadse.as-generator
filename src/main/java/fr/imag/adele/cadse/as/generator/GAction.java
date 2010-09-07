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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import fede.workspace.eclipse.composition.java.EclipsePluginContentManger;
import fede.workspace.tool.eclipse.MappingManager;
import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.objectadapter.ObjectAdapter;
import fr.imag.adele.fede.workspace.si.view.View;
/**
 * 
 * @author <a href="http://cadse.imag.fr">cadse team</a>
 *
 */
public class GAction extends ObjectAdapter<GAction> {

	@Override
	public Class<GAction> getClassAdapt() {
		return GAction.class;
	}

	public void generate(GGenerator g, Item currentItem, GenContext cxt) throws CoreException {
		GGenFile[] gf = currentItem.adapts(GGenFile.class);
		if (gf == null)
			return;
		for (GGenFile gGenFile : gf) {
			generate(gGenFile, currentItem, cxt);
		}
	}
	
	public void generate(Item currentItem, GenContext cxt) throws CoreException {
		GGenFile[] gf = currentItem.adapts(GGenFile.class);
		if (gf == null)
			return;
		for (GGenFile gGenFile : gf) {
			generate(gGenFile, currentItem, cxt);
		}
	}
	
	public void generate(GGenFile gf, Item currentItem, GenContext cxt) throws CoreException {
		if (gf == null)
			return;
		GGenerator g = gf.getGenerator();
		if (g == null)
			return;
		generate(g, currentItem, cxt, gf, gf.getKey());
	}

	
	private void generate(GGenerator g, Item currentItem, GenContext cxt, 
			GGenFile gFile, GToken fileToken)
			throws CoreException {
	
		IFile file = g.getFile(currentItem, fileToken, cxt);
		String content = gFile.generate(g, currentItem, cxt);
		if (content == null || file == null) 
			return;
		try {
			if ("java".equals(file.getFileExtension()))
				EclipsePluginContentManger.generateJava(file, content, View.getDefaultMonitor());
			else
				MappingManager.generate(file.getProject(), file.getParent().getProjectRelativePath(), file.getName(), content, View.getDefaultMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	

	
}
