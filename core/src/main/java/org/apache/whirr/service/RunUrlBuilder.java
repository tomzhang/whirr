/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.whirr.service;

import java.net.MalformedURLException;
import java.net.URL;

import static org.jclouds.scriptbuilder.domain.Statements.exec;

import org.jclouds.scriptbuilder.ScriptBuilder;

/**
 * A convenience class for building scripts to run on nodes.
 */
public class RunUrlBuilder {

  // Need to be able to specify base URL
  // Perhaps make these scripts parameterizable?
  // e.g. just java/install then base url is .../openjdk or .../sun or
  // .../apache or .../cloudera
  public static byte[] runUrls(String... urls) throws MalformedURLException {
    ScriptBuilder scriptBuilder = new ScriptBuilder().addStatement(
      exec("wget -qO/usr/bin/runurl run.alestic.com/runurl")).addStatement(
      exec("chmod 755 /usr/bin/runurl"));

    // Note that the runurl scripts should be checked in to whirr/scripts/
    String runUrlBase = System.getProperty("whirr.runurl.base", "http://whirr.s3.amazonaws.com/");
    for (String url : urls) {
      scriptBuilder.addStatement(exec("runurl " + new URL(new URL(runUrlBase), url)));
    }

    return scriptBuilder.build(org.jclouds.scriptbuilder.domain.OsFamily.UNIX)
      .getBytes();
  }

}
