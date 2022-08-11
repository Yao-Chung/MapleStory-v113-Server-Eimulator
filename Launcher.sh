#!/bin/sh
export CLASSPATH=".:dist/*:dist/lib/*"
java -Xmx512M -server -Dnet.sf.odinms.wzpath=wz  server.Start
