#!/usr/bin/env bash

proxies=$(realpath native-image-agent/proxy-config.json)
reflection=$(realpath native-image-agent/reflect-config-filtered.json)

rm -rf target/native
mkdir -p target/native
cd target/native
jar -xvf ../app-1.0.0.jar >/dev/null 2>&1
cp -R META-INF BOOT-INF/classes
native-image -H:Name=app-nat \
-H:DynamicProxyConfigurationFiles=$proxies \
-H:ReflectionConfigurationFiles=$reflection \
-cp BOOT-INF/classes:`find BOOT-INF/lib | tr '\n' ':'` \
--trace-class-initialization=org.springframework.boot.logging.logback.ColorConverter \
--initialize-at-build-time=org.springframework.boot.logging.logback.ColorConverter
mv app-nat ../

