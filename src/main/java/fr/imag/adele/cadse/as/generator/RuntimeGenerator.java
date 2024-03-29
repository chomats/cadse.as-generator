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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import fede.workspace.tool.view.WSPlugin;
import fr.imag.adele.cadse.core.CadseException;
import fr.imag.adele.cadse.core.CadseGCST;
import fr.imag.adele.cadse.core.GenContext;
import fr.imag.adele.cadse.core.IItemNode;
import fr.imag.adele.cadse.core.IMenuAction;
import fr.imag.adele.cadse.core.Item;
import fr.imag.adele.cadse.core.ItemType;
import fr.imag.adele.cadse.core.Menu;
import fr.imag.adele.cadse.core.impl.CadseCore;
import fr.imag.adele.cadse.core.transaction.AbstractLogicalWorkspaceTransactionListener;
import fr.imag.adele.cadse.core.transaction.LogicalWorkspaceTransaction;
import fr.imag.adele.cadse.core.transaction.delta.ItemDelta;
import fr.imag.adele.cadse.core.ui.AbstractActionContributor;
import fr.imag.adele.cadse.core.ui.view.ViewDescription;

@Component(name = "fr.imag.adele.cadse.runtimeGenerator", immediate = true, architecture = true)
@Provides(specifications = { IRuntimeGenerator.class })
@Instantiate(name = "fr.imag.adele.cadse.runtimeGenerator.instance")
public class RuntimeGenerator implements IRuntimeGenerator, Runnable {
	GListener generatorListener = null;
	private final static int DELAY = 10000;
	GenerateMenuContributor generateMenu = null;

	private final class GenerateMenuContributor extends AbstractActionContributor {
		@Override
		public void contributeMenuAction(ViewDescription viewDescription, Menu menu, IItemNode[] selection) {
			List<Item> generateContent = getGenerateObject(selection);
			if (generateContent.size() != 0) {
				menu.insert(IMenuAction.CONTEXT_2_MENU, new GenerateAction(RuntimeGenerator.this, generateContent),
						true);
			}
		}

		protected List<Item> getGenerateObject(IItemNode[] ssel) {
			List<Item> ret = new ArrayList<Item>();
			for (IItemNode iiv : ssel) {
				try {
					Item item = iiv.getItem();
					if (item != null && item.isResolved()) {
						if (item.getType().adapt(GGenFile.class) != null)
							ret.add(item);

						if (item.getType().adapt(GAction.class) != null)
							ret.add(item);
					}

				} catch (Throwable e) {
					WSPlugin.logException(e);
				}
			}
			return ret;
		}
	}

	public static class Entry {
		Item item;
		GGenFile<?> file;
		GAction action;

		public Entry(Item item, GGenFile<?> file) {
			super();
			this.item = item;
			this.file = file;
		}

		public Entry(Item item, GAction action) {
			super();
			this.item = item;
			this.action = action;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Entry) {
				Entry e = (Entry) obj;
				return e.action == action && e.file == file && e.item == item;
			}
			return false;
		}

	}

	@Requires(proxy = false, nullable = false)
	IGenerator[] _generators;
	private boolean end;
	BlockingQueue<Entry> _filesToGenrate = new LinkedBlockingQueue<Entry>();

	public RuntimeGenerator() {
	}

	@Override
	public IGenerator get(UUID id) {
		if (_generators != null)
			for (IGenerator g : _generators) {
				if (g.getID().equals(id))
					return g;
			}
		return null;
	}

	@Override
	public IGenerator[] getGenerators() {
		return _generators;
	}

	@Validate
	public void starting() {
		Thread T = new Thread(this);
		end = false;
		T.start();
	}

	@Invalidate
	public void stopping() {
		end = true;
		if (_generators != null) {
			for (IGenerator g : _generators) {
				g.unload(this);
			}
		}
		ItemType it = CadseGCST.ITEM;
		if (it != null && generateMenu != null)
			it.removeActionContributeur(generateMenu);
	}

	@Override
	public void run() {
		while (!CadseCore.isCadseRuntimeRunning())
			try {
				Thread.sleep(10);
				if (end)
					return;
			} catch (InterruptedException e) {
				return;
			}
		if (_generators != null) {
			for (IGenerator g : _generators) {
				try {
					g.load(this);
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		generatorListener = new GListener(this);
		generateMenu = new GenerateMenuContributor();
		CadseGCST.ITEM.addActionContributeur(generateMenu);

		CadseCore.getLogicalWorkspace().addLogicalWorkspaceTransactionListener(
				new AbstractLogicalWorkspaceTransactionListener() {
					@Override
					public void notifyCommitTransaction(LogicalWorkspaceTransaction wc) throws CadseException {
						for (ItemDelta d : wc.getItemsDelta()) {
							if (d.isLoaded())
								generate(d.getBaseItem());
						}
					}
				});

		while (!end) {
			Entry e = null;
			try {
				e = _filesToGenrate.take();
			} catch (InterruptedException e1) {
				continue;
			}

			if (e == null)
				continue;

			if (e.file != null) {
				GenContext cxt = new GenContext(null);
				GAction ga = e.item.getType().adapt(GAction.class);
				if (ga == null)
					ga = new GAction();
				try {
					ga.generate(e.file, e.item, cxt);
				} catch (Throwable ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			} else {
				GenContext cxt = new GenContext(null);
				try {
					e.action.generate(e.item, cxt);
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void generate(Item item) {
		if (item == null)
			return;
		GGenFile<?>[] gf = item.getType().adapts(GGenFile.class);
		if (gf != null) {
			for (GGenFile<?> gGenFile : gf) {
				generate(item, gGenFile);
			}
		} else {
			GAction a = item.getType().adapt(GAction.class);
			_filesToGenrate.add(new Entry(item, a));
		}
	}

	@Override
	synchronized public void generate(Item item, GGenFile<?> file) {
		Entry e = new Entry(item, file);
		if (!_filesToGenrate.contains(e))
			_filesToGenrate.add(e);
	}
}
