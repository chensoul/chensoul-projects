${logger.info("Groovy audit manager running...")}
${
    def bean = applicationContext.getBean("groovyAuditManagerRegistryConfigurer")
    logger.debug(bean.class.name)
}
username: ${username},
action: ${action},
operateTime: ${operate_time},
ip: ${client_ip}
