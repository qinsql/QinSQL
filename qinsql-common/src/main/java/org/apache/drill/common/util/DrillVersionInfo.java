

package org.apache.drill.common.util;

import org.apache.drill.common.Version;

/*
 * This file is generated with Freemarker using the template src/main/codegen/templates/DrillVersionInfo.java
 */
/**
 * Give access to Drill version as captured during the build
 *
 * <strong>Caution</strong> don't rely on major, minor and patch versions only to compare two 
 * Drill versions. Instead you should use the whole string, and apply the same semver algorithm
 * as Maven (see {@code org.apache.maven.artifact.versioning.ComparableVersion}).
 *
 */
public class DrillVersionInfo {
  /**
   * The version extracted from Maven POM file at build time.
   */
  public static final Version VERSION = new Version(
      "1.16.0",
      1,
      16,
      0,
      0,
      ""
  );

  /**
   * Get the Drill version from pom
   * @return the version number as x.y.z
   */
  public static String getVersion() {
    return VERSION.getVersion();
  }

  /**
   *  Get the Drill major version from pom
   *  @return x if assuming the version number is x.y.z
   */
  public static int getMajorVersion() {
    return VERSION.getMajorVersion();
  }

  /**
   *  Get the Drill minor version from pom
   *  @return y if assuming the version number is x.y.z
   */
  public static int getMinorVersion() {
    return VERSION.getMinorVersion();
  }

  /**
   *  Get the Drill patch version from pom
   *  @return z if assuming the version number is x.y.z(-suffix)
   */
  public static int getPatchVersion() {
    return VERSION.getPatchVersion();
  }

  /**
   *  Get the Drill build number from pom
   *  @return z if assuming the version number is x.y.z(.b)(-suffix)
   */
  public static int getBuildNumber() {
    return VERSION.getPatchVersion();
  }

  /**
   *  Get the Drill version qualifier from pom
   *  @return suffix if assuming the version number is x.y.z(-suffix), or an empty string
   */
  public static String getQualifier() {
    return VERSION.getQualifier();
  }
}

