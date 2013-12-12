package app

import app.ratpack.FoaasRatpackServerBuilder
import ratpack.launch.LaunchConfig
import ratpack.launch.LaunchConfigFactory
import ratpack.launch.RatpackMain
import ratpack.server.RatpackServer
/**
 * User: danielwoods
 * Date: 9/25/13
 */
class FuckOffMain extends RatpackMain {

  Properties overrideProperties
  Properties defaultProperties

  public static void main(args) {
    new FuckOffMain().start()
  }

  public RatpackServer server(Properties overrideProperties, Properties defaultProperties) {
    this.overrideProperties = overrideProperties
    this.defaultProperties = defaultProperties

    addImpliedDefaults defaultProperties

    FoaasRatpackServerBuilder.build launchConfig
  }

  private LaunchConfig getLaunchConfig() {
    LaunchConfigFactory.createFromGlobalProperties(RatpackMain.class.getClassLoader(), overrideProperties, defaultProperties);
  }

}
