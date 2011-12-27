/**
 * Copyright 2011 Matt Weagle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.custom.karafservlet;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Demonstrates using the PAX whiteboard registration technique for Servlet registration.
 * 
 * @author mweagle
 * @see http://team.ops4j.org/wiki/display/paxweb/Whiteboard+Extender
 */
public class Activator implements BundleActivator
{
	public void start(BundleContext context) throws Exception
	{
		Dictionary<String, String> props = new Hashtable<String, String>();
		props.put("alias", HelloWorldServlet.ALIAS);
		props.put("servlet-name", HelloWorldServlet.class.getName());
		context.registerService(javax.servlet.Servlet.class.getName(), new HelloWorldServlet(), props);

		System.out.println("Karaf PAX Whiteboard servlet started");
	}

	public void stop(BundleContext context) throws Exception
	{
		System.out.println("Karaf PAX Whiteboard servlet stopped");
	}
}