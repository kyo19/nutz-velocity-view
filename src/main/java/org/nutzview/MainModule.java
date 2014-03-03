package org.nutzview;

import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.LoadingBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.Views;
import org.nutz.mvc.ioc.provider.ComboIocProvider;
import org.nutzview.view.VelocityViewMaker;

@Fail("raw")
@Modules(packages = { "org.nutzview" }, scanPackage = true)
@IocBy(type = ComboIocProvider.class, args = { "*org.nutz.ioc.loader.json.JsonLoader", "/aop.js",
		"*org.nutz.ioc.loader.annotation.AnnotationIocLoader", "org.nutzview" })
@SetupBy(ICommunitySetup.class)
@Encoding(input = "UTF-8", output = "UTF-8")
@Views({ VelocityViewMaker.class })
public class MainModule {

}
