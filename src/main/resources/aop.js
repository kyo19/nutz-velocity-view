var ioc = {
	time : {
		type :'org.nutzview.util.HandleTimeInterceptor'
	},
	$aop : {
		type : 'org.nutz.ioc.aop.config.impl.JsonAopConfigration',
		fields : {
			itemList : [
				['org\\.nutzview\\..+','.+','ioc:time']
			]
		}
	}
};
